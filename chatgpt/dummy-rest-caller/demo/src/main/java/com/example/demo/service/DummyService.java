package com.example.demo.service;

import com.example.demo.exception.ClientException;
import com.example.demo.exception.OtherException;
import com.example.demo.exception.ServerException;
import com.example.demo.model.DummyWrapper;
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
  public Mono<DummyWrapper> getIsin() {
    var monoX =
        webClientWithTimeout
            .post()
            .uri(uriBuilder -> uriBuilder.path("/api/isin").build())
            .body(BodyInserters.fromValue("dummy"))
            .exchangeToMono(
                clientResponse -> {
                  if (clientResponse.statusCode().is2xxSuccessful()
                      || clientResponse.statusCode().is4xxClientError()) {
                    System.out.println("2xx or 4xx within exchangeToMono");
                    return clientResponse
                        .bodyToMono(String.class)
                        .map(x -> new DummyWrapper(clientResponse.statusCode(), x));
                  } else if (clientResponse.statusCode().is5xxServerError()) {
                    System.out.println("5xx within exchangeToMono");
                    return clientResponse
                        .bodyToMono(String.class)
                        .flatMap(body -> Mono.error(new ServerException("Server error: " + body)));
                  } else {
                    return clientResponse
                        .bodyToMono(String.class)
                        .map(x -> new DummyWrapper(clientResponse.statusCode(), x));
                  }
                })
            .retryWhen(retrySpec)
            .doOnNext(
                result -> {
                  System.out.println("doOnNext: " + result);
                  if (result.getStatusCode().is2xxSuccessful()) {
                    System.out.println("Will do Caching");
                  } else {
                    System.out.println("Will not do Caching");
                  }
                })
            .doOnError(
                error -> {
                  System.out.println("doOnError: " + error);
                  throw new OtherException("Other error");
                });
    return monoX;
  }

  private RetryBackoffSpec retrySpec =
      Retry.backoff(3, Duration.ofSeconds(3))
          .jitter(0.5)
          .doBeforeRetry(info -> System.out.println("doBeforeRetry: " + info.totalRetriesInARow()))
          .filter(throwable -> !(throwable instanceof ClientException))
          .onRetryExhaustedThrow(
              (retryBackoffSpec, retrySignal) -> new OtherException("Retry exhausted"));
}
