package com.example.concurrentcallclient.client;

import com.example.concurrentcallclient.exception.*;
import com.example.concurrentcallclient.model.GranularResponse;
import com.example.concurrentcallclient.model.TargetObjectFromLargeObject;
import com.example.concurrentcallclient.service.publishier.QueuePublisher;
import com.example.concurrentcallclient.util.DataUtils;
import io.netty.handler.timeout.TimeoutException;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.function.Function;

@Component
public class DataSourceClient {
  private final Logger logger = LoggerFactory.getLogger(DataSourceClient.class);
  private final WebClient webClient;
  private final Scheduler webClientScheduler;
  private final QueuePublisher queuePublisher;

  public DataSourceClient(
      WebClient webClient, Scheduler webClientScheduler, QueuePublisher queuePublisher) {
    this.webClient = webClient;
    this.webClientScheduler = webClientScheduler;
    this.queuePublisher = queuePublisher;
  }

  public Mono<TargetObjectFromLargeObject> fetchData(int pageNum, int pageSize) {
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
        .bodyToMono(String.class)
        .timeout(Duration.ofSeconds(1))
        .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
        .map(DataUtils::processJson)
        .filter(java.util.Objects::nonNull)
        .flatMap(this::publishToQueue)
        .doOnError(error -> logger.error("Error in fetchData: {}", error.getMessage(), error))
        .onErrorResume(error -> Mono.empty())
        .subscribeOn(webClientScheduler);
  }

  public Mono<GranularResponse> fetchDataGranular(int pageNum, int pageSize) {
    return webClient
        .get()
        .uri(buildUri(pageNum, pageSize))
        .exchangeToMono(this::handleResponse)
        .timeout(Duration.ofSeconds(1))
        .retryWhen(buildRetrySpec())
        .onErrorResume(this::handleError)
        .subscribeOn(webClientScheduler);
  }

  private Function<org.springframework.web.util.UriBuilder, java.net.URI> buildUri(
      int pageNum, int pageSize) {
    return uriBuilder ->
        uriBuilder
            .path("/message/getLargeJsonObject")
            .queryParam("pageNum", pageNum)
            .queryParam("pageSize", pageSize)
            .build();
  }

  private Mono<GranularResponse> handleResponse(ClientResponse response) {
    if (response.statusCode().is2xxSuccessful()) {
      return processSuccessfulResponse(response);
    } else if (response.statusCode().is5xxServerError()) {
      return Mono.error(new ServerErrorException(response.statusCode().value()));
    } else if (response.statusCode().is4xxClientError()) {
      return Mono.error(new ClientErrorException(response.statusCode().value()));
    } else {
      return Mono.error(new UnexpectedStatusException(response.statusCode().value()));
    }
  }

  private Mono<GranularResponse> processSuccessfulResponse(ClientResponse response) {
    return response
        .bodyToMono(String.class)
        .flatMap(this::processAndPublish)
        .onErrorMap(this::wrapProcessingError);
  }

  private Mono<GranularResponse> processAndPublish(String jsonString) {
    try {
      TargetObjectFromLargeObject processedObject = DataUtils.processJson(jsonString);
      return publishToQueue(processedObject).map(this::createSuccessResponse);
    } catch (DataProcessingException e) {
      return Mono.error(e);
    }
  }

  private GranularResponse createSuccessResponse(TargetObjectFromLargeObject publishedObject) {
    return new GranularResponse(
        publishedObject.name(), Strings.left(publishedObject.processedData(), 10), true, "");
  }

  private Throwable wrapProcessingError(Throwable error) {
    if (error instanceof QueuePublishingException) {
      return new QueuePublishingException("Queue publishing failed", error);
    }
    return error;
  }

  private Retry buildRetrySpec() {
    return Retry.fixedDelay(3, Duration.ofSeconds(1))
        .filter(
            throwable ->
                throwable instanceof ServerErrorException || throwable instanceof TimeoutException)
        .onRetryExhaustedThrow(
            (retryBackoffSpec, retrySignal) ->
                new CustomRetryExhaustedException(
                    "Retry exhausted after 3 attempts", retrySignal.failure()));
  }

  private Mono<GranularResponse> handleError(Throwable error) {
    logger.error("Error in fetchDataGranular: {}", error.getMessage(), error);
    String failureMessage = determineFailureMessage(error);
    return Mono.just(new GranularResponse("Unknown", "", false, failureMessage));
  }

  private String determineFailureMessage(Throwable error) {
    if (error instanceof TimeoutException) {
      return "Timeout occurred after 3 retry attempts";
    } else if (error instanceof ServerErrorException) {
      return "Server error (5xx) persisted after 3 retry attempts: "
          + ((ServerErrorException) error).getStatusCode();
    } else if (error instanceof ClientErrorException) {
      return "Client error (4xx), no retry attempted: "
          + ((ClientErrorException) error).getStatusCode();
    } else if (error instanceof CustomRetryExhaustedException) {
      return "Retries exhausted: " + error.getCause().getMessage();
    } else if (error instanceof UnexpectedStatusException) {
      return "Unexpected HTTP status: " + ((UnexpectedStatusException) error).getStatusCode();
    } else if (error instanceof DataProcessingException) {
      return "Data processing failed: " + error.getMessage();
    } else if (error instanceof QueuePublishingException) {
      return "Queue publishing failed: " + error.getCause().getMessage();
    } else {
      return "Unknown error: " + error.getMessage();
    }
  }

  private Mono<TargetObjectFromLargeObject> publishToQueue(
      TargetObjectFromLargeObject largeJsonObject) {
    return Mono.fromCallable(
            () -> {
              queuePublisher.publish(largeJsonObject);
              return largeJsonObject;
            })
        .subscribeOn(Schedulers.boundedElastic())
        .doOnSuccess(result -> logger.debug("Successfully published to queue: {}", result.name()))
        .doOnError(error -> logger.error("Error publishing to queue: {}", error.getMessage()))
        .onErrorMap(error -> new QueuePublishingException("Failed to publish to queue", error));
  }
}
