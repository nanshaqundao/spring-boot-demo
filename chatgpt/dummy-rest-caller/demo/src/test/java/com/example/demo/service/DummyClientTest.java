package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.demo.exception.OtherException;
import java.io.IOException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

public class DummyClientTest {

  private DummyClient dummyClient;
  private MockWebServer mockWebServer;

  @BeforeEach
  void setUp() throws IOException {
    mockWebServer = new MockWebServer();
    mockWebServer.start();

    WebClient webClient = WebClient.builder().baseUrl(mockWebServer.url("/").toString()).build();

    dummyClient = new DummyClient(webClient);
  }

  @Test
  public void getIsin_serverError() {
    // Enqueue 4 server error responses: the initial one plus 3 for the retries
    for (int i = 0; i < 4; i++) {
      mockWebServer.enqueue(
          new MockResponse()
              .setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
              .setBody("Server error " + (i + 1)));
    }

    StepVerifier.create(dummyClient.getIsin("name"))
        .expectErrorMatches(
            x ->
                x
                    instanceof
                    OtherException) // after retry exhausted, it should throw OtherException
        .verify();
  }

  @Test
  public void getIsin_2xx4xx_withRetry() {

    for (int i = 0; i < 3; i++) {
      mockWebServer.enqueue(
          new MockResponse()
              .setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
              .setBody("Server error " + (i + 1)));
    }

    mockWebServer.enqueue(
        new MockResponse().setResponseCode(HttpStatus.OK.value()).setBody("Server good " + (4)));

    StepVerifier.create(dummyClient.getIsin("name"))
        .assertNext(
            x -> {
              assertEquals(HttpStatus.OK, x.getStatusCode());
              assertEquals("Server good 4", x.getBody());
            }) // after retry exhausted, it should throw OtherException
        .verifyComplete();
  }

  @Test
  public void getIsin_2xx4xx() {


    mockWebServer.enqueue(
            new MockResponse().setResponseCode(HttpStatus.OK.value()).setBody("Server good" ));

    StepVerifier.create(dummyClient.getIsin("name"))
            .assertNext(
                    x -> {
                      assertEquals(HttpStatus.OK, x.getStatusCode());
                      assertEquals("Server good", x.getBody());
                    }) // after retry exhausted, it should throw OtherException
            .verifyComplete();
  }
}
