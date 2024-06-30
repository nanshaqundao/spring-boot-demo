package com.example.concurrentcallclient.service;

import com.example.concurrentcallclient.model.TargetObjectFromLargeObject;
import com.example.concurrentcallclient.service.publishier.QueuePublisher;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class NewApiServiceTest {

  private ApiService apiService;
  private static MockWebServer mockWebServer;
  private Scheduler webClientScheduler;
  private QueuePublisher queuePublisher;
  private ObjectMapper objectMapper;

  @BeforeAll
  public static void setUp() throws IOException {
    mockWebServer = new MockWebServer();
    mockWebServer.start();
  }

  @AfterAll
  public static void tearDown() throws IOException {
    mockWebServer.shutdown();
  }

  @BeforeEach
  public void initialize() {
    webClientScheduler = Schedulers.boundedElastic();
    queuePublisher = mock(QueuePublisher.class);
    objectMapper = new ObjectMapper();

    WebClient webClient = WebClient.builder().baseUrl(mockWebServer.url("/").toString()).build();

    apiService = new ApiService(webClient, webClientScheduler, queuePublisher, objectMapper);
  }

  @Test
  @DisplayName("fetchDataFromApi processes and publishes data successfully")
  public void fetchDataFromApiProcessesAndPublishesDataSuccessfully() {
    // Mock response
    String mockResponse = "{\"name\":\"name\",\"data\":\"data\"}";
    mockWebServer.enqueue(
        new MockResponse().setBody(mockResponse).addHeader("Content-Type", "application/json"));

    // Mock the QueuePublisher
    Mockito.doNothing().when(queuePublisher).publish(any(TargetObjectFromLargeObject.class));

    // Test the method
    Mono<TargetObjectFromLargeObject> result = apiService.fetchDataFromApi(1, 10);

    StepVerifier.create(result)
        .expectNextMatches(
            targetObject ->
                "name".equals(targetObject.name()) && "data".equals(targetObject.data()))
        .verifyComplete();
  }

  @Test
  @DisplayName("fetchDataFromApi handles API call failures")
  public void fetchDataFromApiHandlesApiCallFailures() {
    // Mock error response
    mockWebServer.enqueue(new MockResponse().setResponseCode(500).setBody("Internal Server Error"));

    // Test the method
    Mono<TargetObjectFromLargeObject> result = apiService.fetchDataFromApi(1, 10);

    StepVerifier.create(result).expectComplete().verify();
  }
}
