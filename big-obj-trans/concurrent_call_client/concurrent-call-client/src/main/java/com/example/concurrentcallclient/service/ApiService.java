package com.example.concurrentcallclient.service;

import com.example.concurrentcallclient.model.LargeJsonObject;
import com.example.concurrentcallclient.model.TargetObjectFromLargeObject;
import com.example.concurrentcallclient.service.publishier.QueuePublisher;
import com.example.concurrentcallclient.util.DataUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataUnit;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.util.retry.Retry;
import java.time.Duration;
import java.util.Objects;

@Service
public class ApiService {
  private final WebClient webClient;
  private final Scheduler webClientScheduler;
  private final QueuePublisher queuePublisher;
  private final ObjectMapper objectMapper;

  public ApiService(
      WebClient webClient,
      Scheduler webClientScheduler,
      QueuePublisher queuePublisher,
      ObjectMapper objectMapper) {
    this.webClient = webClient;
    this.webClientScheduler = webClientScheduler;
    this.queuePublisher = queuePublisher;
    this.objectMapper = objectMapper;
  }

  public Mono<TargetObjectFromLargeObject> fetchDataFromApi(int pageNum, int pageSize) {
    return webClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("/message/getLargeJsonObject")
                    .queryParam("pageNum", pageNum)
                    .queryParam("pageSize", pageSize)
                    .build())
        .retrieve()
        .onStatus(
            status -> status.is4xxClientError() || status.is5xxServerError(),
            response ->
                response
                    .bodyToMono(String.class)
                    .flatMap(
                        body ->
                            Mono.error(
                                new RuntimeException(
                                    "Error: " + response.statusCode() + ", Body: " + body))))
        .bodyToMono(String.class)
        .timeout(Duration.ofSeconds(5))
        .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(2)))
        .map(this::processJson)
        .filter(Objects::nonNull) // Filter out null values
        .flatMap(this::publishToQueue)
        .doOnError(
            error -> {
              if (error instanceof WebClientResponseException ex) {
                System.err.println("Error response code: " + ex.getStatusCode());
                System.err.println("Error response body: " + ex.getResponseBodyAsString());
              } else {
                System.err.println("Error: " + error.getMessage());
              }
            })
        .onErrorResume(error -> Mono.empty())
        .subscribeOn(webClientScheduler);
  }

  private TargetObjectFromLargeObject processJson(String json) {
    LargeJsonObject largeObjectData = null;
    try {
      largeObjectData = objectMapper.readValue(json, LargeJsonObject.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    return DataUtils.processLargeJsonObject(largeObjectData);
  }

  private Mono<TargetObjectFromLargeObject> publishToQueue(
      TargetObjectFromLargeObject largeJsonObject) {
    return Mono.fromCallable(
        () -> {
          queuePublisher.publish(largeJsonObject);
          return largeJsonObject;
        });
  }
}
