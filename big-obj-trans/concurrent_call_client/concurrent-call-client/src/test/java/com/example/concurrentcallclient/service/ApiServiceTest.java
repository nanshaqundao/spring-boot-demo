package com.example.concurrentcallclient.service;

import com.example.concurrentcallclient.model.LargeJsonObject;
import com.example.concurrentcallclient.model.TargetObjectFromLargeObject;
import com.example.concurrentcallclient.service.publishier.QueuePublisher;
import com.example.concurrentcallclient.util.DataUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApiServiceTest {

  private static MockWebServer mockWebServer;
  private ApiService apiService;

  @Mock private QueuePublisher queuePublisher;

  private final ObjectMapper objectMapper = new ObjectMapper();

//  @BeforeAll
//  static void setUp() throws IOException {
//    mockWebServer = new MockWebServer();
//    mockWebServer.start();
//  }
//
//  @AfterAll
//  static void tearDown() throws IOException {
//    mockWebServer.shutdown();
//  }

  @BeforeEach
  void initialize() throws IOException {
    mockWebServer = new MockWebServer();
    mockWebServer.start();
    String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
    WebClient webClient = WebClient.create(baseUrl);
    apiService = new ApiService(webClient, Schedulers.immediate(), queuePublisher);

  }

  @AfterEach
    void reset() throws IOException {
        mockWebServer.shutdown();
    }


  @Test
  void fetchDataFromApiReturnsTargetObjectWhenApiCallIsSuccessful() throws Exception {
    // Arrange
    LargeJsonObject largeJsonObject = new LargeJsonObject("Test Name", "Test Data");
    String jsonResponse = objectMapper.writeValueAsString(largeJsonObject);

    mockWebServer.enqueue(
            new MockResponse().setBody(jsonResponse).addHeader("Content-Type", "application/json"));


    TargetObjectFromLargeObject expectedTargetObject =
            DataUtils.processLargeJsonObject(largeJsonObject);

    // Act & Assert
    StepVerifier.create(apiService.fetchDataFromApi(0, 10))
            .consumeNextWith(result -> {
              System.out.println("Received result: " + result);
              assertThat(result.name()).isEqualTo("Test Name");
              assertThat(result.data()).isEqualTo("Test Data");
              assertThat(result.processedData()).isEqualTo(Base64.getEncoder().encodeToString("Test Data".getBytes()));
            })
            .verifyComplete();

    verify(queuePublisher).publish(any(TargetObjectFromLargeObject.class));
  }

  @Test
  void fetchDataFromApiHandlesErrorResponseCorrectly() {
    // Arrange
    mockWebServer.enqueue(new MockResponse().setResponseCode(500).setBody("Server Error"));

    // Act & Assert
    StepVerifier.create(apiService.fetchDataFromApi(0, 10))
        .verifyComplete(); // Expecting empty result due to error handling

    verify(queuePublisher, never()).publish(any(TargetObjectFromLargeObject.class));
  }

  @Test
  void fetchDataFromApiHandlesTimeoutCorrectly() {
    // Arrange
    mockWebServer.enqueue(
        new MockResponse()
            .setBodyDelay(6000, java.util.concurrent.TimeUnit.MILLISECONDS)
            .setBody("Delayed Response"));

    // Act & Assert
    StepVerifier.create(apiService.fetchDataFromApi(0, 10))
        .verifyComplete(); // Expecting empty result due to timeout

    verify(queuePublisher, never()).publish(any(TargetObjectFromLargeObject.class));
  }

  @Test
  void fetchDataFromApiHandlesInvalidJsonCorrectly() throws Exception {
    // Arrange
    mockWebServer.enqueue(
        new MockResponse().setBody("Invalid JSON").addHeader("Content-Type", "application/json"));

    // Act & Assert
    StepVerifier.create(apiService.fetchDataFromApi(0, 10))
        .verifyComplete(); // Expecting empty result due to JSON parsing error

    verify(queuePublisher, never()).publish(any(TargetObjectFromLargeObject.class));
  }
}
