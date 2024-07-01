package com.example.concurrentcallclient.client;

import com.example.concurrentcallclient.model.GranularResponse;
import com.example.concurrentcallclient.model.LargeJsonObject;
import com.example.concurrentcallclient.model.TargetObjectFromLargeObject;
import com.example.concurrentcallclient.service.publishier.QueuePublisher;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DataSourceClientTest {

  private MockWebServer mockWebServer;
  private DataSourceClient dataSourceClient;
  private ObjectMapper objectMapper;

  @Mock private QueuePublisher queuePublisher;

  @BeforeEach
  void setUp() throws IOException {
    mockWebServer = new MockWebServer();
    mockWebServer.start();

    WebClient webClient = WebClient.create(mockWebServer.url("/").toString());
    dataSourceClient = new DataSourceClient(webClient, Schedulers.immediate(), queuePublisher);
    objectMapper = new ObjectMapper();
  }

  @AfterEach
  void tearDown() throws IOException {
    mockWebServer.shutdown();
  }

  @Test
  void fetchDataSuccessful() throws Exception {
    String jsonResponse = objectMapper.writeValueAsString(new LargeJsonObject("Test", "TestData"));
    mockWebServer.enqueue(
        new MockResponse().setBody(jsonResponse).addHeader("Content-Type", "application/json"));

    StepVerifier.create(dataSourceClient.fetchData(0, 10))
        .expectNextMatches(
            result ->
                "Test".equals(result.name())
                    && "TestData".equals(result.data())
                    && result.processedData() != null)
        .verifyComplete();

    verify(queuePublisher).publish(any(TargetObjectFromLargeObject.class));
  }

  @Test
  void fetchDataHandlesClientError() {
    mockWebServer.enqueue(new MockResponse().setResponseCode(400).setBody("Bad Request"));

    StepVerifier.create(dataSourceClient.fetchData(0, 10)).verifyComplete();
  }

  @Test
  void fetchDataHandlesServerError() {
    mockWebServer.enqueue(new MockResponse().setResponseCode(500).setBody("Internal Server Error"));

    StepVerifier.create(dataSourceClient.fetchData(0, 10)).verifyComplete();
  }

  @Test
  void fetchDataGranularSuccessful() throws Exception {
    String jsonResponse = objectMapper.writeValueAsString(new LargeJsonObject("Test", "TestData"));
    mockWebServer.enqueue(
        new MockResponse().setBody(jsonResponse).addHeader("Content-Type", "application/json"));

    StepVerifier.create(dataSourceClient.fetchDataGranular(0, 10))
        .expectNextMatches(
            result ->
                "Test".equals(result.name())
                    && result.updatedData().startsWith("VGVzdERhdG")
                    && result.result()
                    && result.failureMessage().isEmpty())
        .verifyComplete();

    verify(queuePublisher).publish(any(TargetObjectFromLargeObject.class));
  }

  @Test
  void fetchDataGranularHandlesClientError() {
    mockWebServer.enqueue(new MockResponse().setResponseCode(400).setBody("Bad Request"));

    StepVerifier.create(dataSourceClient.fetchDataGranular(0, 10))
        .expectNextMatches(
            result ->
                "Unknown".equals(result.name())
                    && result.updatedData().isEmpty()
                    && !result.result()
                    && result.failureMessage().contains("Client error (4xx)"))
        .verifyComplete();
  }

  @Test
  void fetchDataGranularHandlesServerError() {
    for (int i = 0; i < 4; i++) {
      mockWebServer.enqueue(
          new MockResponse().setResponseCode(500).setBody("Internal Server Error"));
    }

    StepVerifier.create(dataSourceClient.fetchDataGranular(0, 10))
        .expectNextMatches(
            result ->
                "Unknown".equals(result.name())
                    && result.updatedData().isEmpty()
                    && !result.result()
                    && result.failureMessage().contains("Server error: 500"))
        .verifyComplete();
  }

  @Test
  void fetchDataGranularHandlesUnexpectedError() {
    mockWebServer.enqueue(
        new MockResponse()
            .setSocketPolicy(okhttp3.mockwebserver.SocketPolicy.DISCONNECT_AFTER_REQUEST));

    StepVerifier.create(dataSourceClient.fetchDataGranular(0, 10))
        .expectNextMatches(
            result ->
                "Unknown".equals(result.name())
                    && result.updatedData().isEmpty()
                    && !result.result()
                    && (result.failureMessage().contains("Unknown error")
                        || result.failureMessage().contains("Connection")))
        .verifyComplete();
  }
}
