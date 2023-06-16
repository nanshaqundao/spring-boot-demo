package com.example.demo.service;

import com.example.demo.exception.ClientException;
import com.example.demo.exception.OtherException;
import com.example.demo.exception.ServerException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

import java.time.Duration;

@Service
public class DummyService {

  private final WebClient webClientWithTimeout;

  public DummyService(WebClient webClientWithTimeout) {
    this.webClientWithTimeout = webClientWithTimeout;

    System.out.println("DummyService: " + webClientWithTimeout);
  }

  //    public Mono<String> getIsin() {
  //        ///api/isin

  //        var monoX = webClientWithTimeout.post()
  //                .uri(uriBuilder -> uriBuilder.path("/api/isin").build())
  //                .body(BodyInserters.fromValue("dummy"))
  //                .retrieve()
  //
  //                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
  //                    System.out.println("4xx");
  //                    return Mono.error(new ClientException("Client error"));
  //                })
  //                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
  //                    System.out.println("5xx");
  //                    return Mono.error(new ServerException("Server error"));
  //                })
  //                .bodyToMono(String.class)
  //                .timeout(Duration.ofSeconds(5))
  //                .doOnError(throwable -> System.out.println("doOnError: " + throwable))
  //                .retryWhen(Retry.backoff(3,
  // Duration.ofSeconds(3)).doBeforeRetry(System.out::println).filter(throwable -> !(throwable
  // instanceof ClientException || throwable instanceof ServerException)));
  public Mono<String> getIsin() {
    var monoX =
        webClientWithTimeout
            .post()
            .uri(uriBuilder -> uriBuilder.path("/api/isin").build())
            .body(BodyInserters.fromValue("dummy"))
            .exchangeToMono(
                clientResponse -> {
                  if (clientResponse.statusCode().is4xxClientError()) {
                    System.out.println("4xx is not considered an error");
                    return clientResponse.bodyToMono(String.class);
                  } else if (clientResponse.statusCode().is5xxServerError()) {
                    System.out.println("5xx");
                    return clientResponse
                        .bodyToMono(String.class)
                        .flatMap(body -> Mono.error(new ServerException("Server error: " + body)));
                  } else {
                    return clientResponse.bodyToMono(String.class);
                  }
                })
            .doOnNext(
                result -> {
                  System.out.println("doOnNext: " + result);
                })
            .doOnError(
                error -> {
                  System.out.println("doOnError: " + error);
                })
            .retryWhen(retrySpec);
    return monoX;
  }

  private RetryBackoffSpec retrySpec =
      Retry.backoff(3, Duration.ofSeconds(3))
          .jitter(0.5)
          .doBeforeRetry(info -> System.out.println("doBeforeRetry: " + info.totalRetriesInARow()))
          .filter(
              throwable ->
                  !(throwable instanceof ClientException || throwable instanceof ServerException))
          .onRetryExhaustedThrow(
              (retryBackoffSpec, retrySignal) -> new OtherException("Retry exhausted"));
}
