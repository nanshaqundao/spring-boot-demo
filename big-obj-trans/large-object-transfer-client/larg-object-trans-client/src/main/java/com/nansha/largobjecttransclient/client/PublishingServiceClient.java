package com.nansha.largobjecttransclient.client;

import com.nansha.largobjecttransclient.model.MessageState;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;

@Component
public class PublishingServiceClient {

  private final WebClient webClient;

  public PublishingServiceClient(WebClient.Builder webClientBuilder) {
    this.webClient =
        webClientBuilder
            .baseUrl("http://localhost:8080/message")
            .build(); // Adjust the base URL accordingly
  }

  public Mono<List<MessageState>> getMessageStates(int page, int size) {
    return this.webClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("/get")
                    .queryParam("pageNum", page)
                    .queryParam("pageSize", size)
                    .build())
        .retrieve()
        .onStatus(
            status -> status.is4xxClientError() || status.is5xxServerError(),
            clientResponse -> Mono.error(new RuntimeException("Error fetching message states")))
        .bodyToFlux(MessageState.class)
        .retryWhen(
            Retry.fixedDelay(5, Duration.ofSeconds(5))
                .filter(throwable -> throwable instanceof RuntimeException))
        .collectList();
  }
}
