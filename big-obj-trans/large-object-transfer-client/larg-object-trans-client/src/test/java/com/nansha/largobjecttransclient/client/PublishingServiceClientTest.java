package com.nansha.largobjecttransclient.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nansha.largobjecttransclient.exception.TransferException;
import java.io.IOException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

class PublishingServiceClientTest {

  private MockWebServer mockWebServer;
  private PublishingServiceClient publishingServiceClient;

  @BeforeEach
  void setUp() throws IOException {
    mockWebServer = new MockWebServer();
    mockWebServer.start();
    WebClient webClient = WebClient.builder().baseUrl(mockWebServer.url("/").toString()).build();
    publishingServiceClient = new PublishingServiceClient(webClient);
  }

  @Test
  void testGetMessageStates() throws IOException {
    // Prepare a mock response
    mockWebServer.enqueue(
        new MockResponse()
            .setBody(
                "[{\"fieldA\":\"valueA\",\"fieldB\":\"valueB\",\"fieldC\":\"valueC\",\"fieldD\":\"valueD\"}]")
            .addHeader("Content-Type", "application/json"));

    StepVerifier.create(publishingServiceClient.getMessageStates(0, 10))
        .expectNextMatches(
            states -> !states.isEmpty() && states.get(0).getFieldA().equals("valueA"))
        .verifyComplete();
  }

  @Test
  void testGetMessageStates_handles400ErrorWithRetries() {
    // Enqueue a 400 Bad Request response four times to simulate the initial attempt plus three
    // retries
    for (int i = 0; i < 4; i++) {
      mockWebServer.enqueue(new MockResponse().setResponseCode(400).setBody("Bad Request"));
    }

    StepVerifier.create(publishingServiceClient.getMessageStates(0, 10))
        .expectErrorMatches(
            throwable ->
                throwable instanceof IllegalStateException
                    && throwable.getCause() instanceof TransferException
                    && throwable.getCause().getMessage().contains("Error fetching message states"))
        .verify();

    assertEquals(
        4, mockWebServer.getRequestCount(), "Expected four attempts (1 initial + 3 retries)");
  }

  @Test
  void testGetMessageStates_handles500ErrorWithRetries() {
    // Enqueue a 500 Internal Server Error response four times
    for (int i = 0; i < 4; i++) {
      mockWebServer.enqueue(
          new MockResponse().setResponseCode(500).setBody("Internal Server Error"));
    }

    StepVerifier.create(publishingServiceClient.getMessageStates(0, 10))
        .expectErrorMatches(
            throwable ->
                throwable instanceof IllegalStateException
                    && throwable.getCause() instanceof TransferException
                    && throwable.getCause().getMessage().contains("Error fetching message states"))
        .verify();

    assertEquals(
        4, mockWebServer.getRequestCount(), "Expected four attempts (1 initial + 3 retries)");
  }

  @Test
  void testGetMessageStates_retriesAndSucceedsOnFourthAttempt() throws IOException {
    // Enqueue three error responses
    for (int i = 0; i < 3; i++) {
      mockWebServer.enqueue(
          new MockResponse().setResponseCode(500).setBody("Internal Server Error"));
    }

    // Enqueue a successful response for the fourth attempt
    mockWebServer.enqueue(
        new MockResponse()
            .setBody(
                "[{\"fieldA\":\"successValueA\",\"fieldB\":\"successValueB\",\"fieldC\":\"successValueC\",\"fieldD\":\"successValueD\"}]")
            .addHeader("Content-Type", "application/json"));

    StepVerifier.create(publishingServiceClient.getMessageStates(0, 10))
        .expectNextMatches(
            states ->
                !states.isEmpty()
                    && states.get(0).getFieldA().equals("successValueA")
                    && states.size() == 1) // Ensure the successful response is as expected
        .verifyComplete();

    assertEquals(
        4,
        mockWebServer.getRequestCount(),
        "Expected three failed attempts followed by a successful attempt");
  }

  @AfterEach
  void tearDown() throws IOException {
    mockWebServer.shutdown();
  }
}
