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
public class DummyClient {

  private final WebClient webClientWithTimeout;
  private final RetryBackoffSpec retrySpec;

  public DummyClient(WebClient webClientWithTimeout, RetryBackoffSpec retrySpec) {
    this.webClientWithTimeout = webClientWithTimeout;
    this.retrySpec = retrySpec;

    System.out.println("DummyService: " + webClientWithTimeout);
  }

  public Mono<DummyWrapper> getIsin(String name) {

    return webClientWithTimeout
        .post()
        .uri(uriBuilder -> uriBuilder.path("/api/isin").build())
        .body(BodyInserters.fromValue(name))
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
        .retryWhen(retrySpec);
  }
}
