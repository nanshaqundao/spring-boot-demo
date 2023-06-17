package com.example.demo.service;

import com.example.demo.exception.ClientException;
import com.example.demo.exception.OtherException;
import com.example.demo.exception.ServerException;
import com.example.demo.model.DummyWrapper;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

import java.time.Duration;

@Service
public class DummyClient {

  private final WebClient webClientWithTimeout;

  public DummyClient(WebClient webClientWithTimeout) {
    this.webClientWithTimeout = webClientWithTimeout;

    System.out.println("DummyService: " + webClientWithTimeout);
  }

  public Mono<DummyWrapper> getIsin(String name) {

    return webClientWithTimeout
        .post()
        .uri((UriBuilder uriBuilder) -> uriBuilder.path("/api/isin").build())
        .body(BodyInserters.fromValue(name))
        .exchangeToMono(
            clientResponse -> {
              if (clientResponse.statusCode().is5xxServerError()
                  || clientResponse.statusCode().isSameCodeAs(HttpStatus.REQUEST_TIMEOUT)
                  || clientResponse.statusCode().isSameCodeAs(HttpStatus.TOO_MANY_REQUESTS)) {
                System.out.println(
                    "Call get isin failed with error: " + clientResponse.statusCode());
                return clientResponse
                    .bodyToMono(String.class)
                    .flatMap(body -> Mono.error(new ServerException("Server error: " + body)));
              } else {
                return clientResponse
                    .bodyToMono(String.class)
                    .map(x -> new DummyWrapper(clientResponse.statusCode(), x));
              }
            })
        .retryWhen(retrySPec());
  }

  private RetryBackoffSpec retrySPec() {
    return Retry.backoff(3, Duration.ofSeconds(1))
        .jitter(0.5)
        .doBeforeRetry(
            info ->
                System.out.println(
                    "doBeforeRetry: about to start retry: " + (info.totalRetriesInARow() + 1)))
        .filter(throwable -> (throwable instanceof ServerException))
        .doAfterRetry(
            info -> {
              System.out.println(
                  "doAfterRetry: just finished retry: "
                      + (info.totalRetriesInARow() + 1)
                      + " "
                      + info);
            })
        .onRetryExhaustedThrow(
            (retryBackoffSpec, retrySignal) -> {
              System.out.println("Retry exhausted");
              return new OtherException("Retry exhausted");
            });
  }
}
