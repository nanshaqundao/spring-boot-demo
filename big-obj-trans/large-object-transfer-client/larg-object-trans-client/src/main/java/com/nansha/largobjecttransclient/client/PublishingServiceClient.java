package com.nansha.largobjecttransclient.client;

import com.nansha.largobjecttransclient.exception.TransferException;
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

    public PublishingServiceClient(WebClient webClient) {
        this.webClient = webClient;
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
            clientResponse -> Mono.error(new TransferException("Error fetching message states")))
        .bodyToFlux(MessageState.class)
        .retryWhen(
            Retry.fixedDelay(3, Duration.ofSeconds(3))
                .filter(throwable -> throwable instanceof TransferException))
        .collectList();
  }
}
