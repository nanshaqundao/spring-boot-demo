package com.nansha.largobjecttransclient.client;

import com.nansha.largobjecttransclient.exception.TransferException;
import com.nansha.largobjecttransclient.model.MessageState;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
public class PublishingServiceClient {

  private final WebClient webClient1;
  private final WebClient webClient2;

  public PublishingServiceClient(WebClient webClient1, WebClient webClient2) {
    this.webClient1 = webClient1;
    this.webClient2 = webClient2;
  }

  public Mono<List<MessageState>> getMessageStates(int page, int size) {
    return this.webClient1
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

  public Mono<List<MessageState>> fetchAndProcessPages(
      WebClient webClient, int pageNumber, int pageSize, List<MessageState> accumulatedStates) {
    return webClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("/get")
                    .queryParam("pageNum", pageNumber)
                    .queryParam("pageSize", pageSize)
                    .build())
        .retrieve()
        .onStatus(
            status -> status.is4xxClientError() || status.is5xxServerError(),
            clientResponse -> Mono.error(new TransferException("Error fetching message states")))
        .bodyToFlux(MessageState.class)
        .collectList()
        .flatMap(
            messageStates -> {
              List<MessageState> newAccumulatedStates = new ArrayList<>(accumulatedStates);
              newAccumulatedStates.addAll(messageStates);
              if (!messageStates.isEmpty() && messageStates.size() == pageSize) {
                return fetchAndProcessPages(
                    webClient, pageNumber + 1, pageSize, newAccumulatedStates);
              } else {
                return Mono.just(newAccumulatedStates);
              }
            });
  }

  public Mono<List<MessageState>> getMessageStatesFromServer1(
      int pageNumber, int pageSize, List<MessageState> accumulatedStates) {
    return fetchAndProcessPages(webClient1, pageNumber, pageSize, accumulatedStates);
  }

  public Mono<List<MessageState>> getMessageStatesFromServer2(
      int pageNumber, int pageSize, List<MessageState> accumulatedStates) {
    return fetchAndProcessPages(webClient2, pageNumber, pageSize, accumulatedStates);
  }
}
