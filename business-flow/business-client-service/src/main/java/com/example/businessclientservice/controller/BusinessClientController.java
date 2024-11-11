package com.example.businessclientservice.controller;

import com.example.businessclientservice.model.BusinessPayloadDto;
import com.example.businessclientservice.model.BusinessPayloadFluxResponse;
import com.example.businessclientservice.service.BusinessClientService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/client/business")
public class BusinessClientController {
  private static final Logger logger = LoggerFactory.getLogger(BusinessClientController.class);
  private final BusinessClientService businessClientService;
  private final ObjectMapper objectMapper;

  public BusinessClientController(
      BusinessClientService businessClientService,
      ObjectMapper objectMapper) { // Spring Boot auto-configures ObjectMapper
    this.businessClientService = businessClientService;
    this.objectMapper = objectMapper;
  }

  @GetMapping("/payload-flux")
  public Mono<BusinessPayloadFluxResponse> getBusinessPayloadFlux() {
    return businessClientService
        .getBusinessPayloadStreamWorksBelow10000()
        .<String>handle(
            (proto, sink) -> {
              BusinessPayloadDto dto = BusinessPayloadDto.fromProto(proto);
              try {
                sink.next(objectMapper.writeValueAsString(dto));
              } catch (JsonProcessingException e) {
                logger.error("Error converting payload to JSON", e);
                sink.error(new RuntimeException("Error processing payload", e));
              }
            })
        .collectList()
        .map(jsonPayloads -> new BusinessPayloadFluxResponse(jsonPayloads.size(), jsonPayloads));
  }

  @GetMapping("/payload-flux-simple")
  public Mono<String> getBusinessPayloadFluxSimple() {
    final AtomicInteger counter = new AtomicInteger(0);
    final long startTime = System.currentTimeMillis();

    return businessClientService
        .getBusinessPayloadStreamWorksBelow10000()
        .doOnNext(
            proto -> {
              int current = counter.incrementAndGet();
              if (current % 100 == 0) { // Log progress every 100 messages
                long currentTime = System.currentTimeMillis();
                long elapsed = currentTime - startTime;
                logger.info("Progress: {} messages processed in {} ms", current, elapsed);
              }

              try {
                BusinessPayloadDto dto = BusinessPayloadDto.fromProto(proto);
                String json = objectMapper.writeValueAsString(dto);
                logger.debug(
                    "Processed payload {}: {}",
                    current,
                    json); // Using debug level for detailed logs
              } catch (JsonProcessingException e) {
                logger.error("Error converting payload {} to JSON", current, e);
              }
            })
        .doOnComplete(
            () -> {
              long totalTime = System.currentTimeMillis() - startTime;
              logger.info(
                  "Completed processing {} messages in {} ms (avg: {} ms/msg)",
                  counter.get(),
                  totalTime,
                  String.format("%.2f", totalTime / (double) counter.get()));
            })
        .doOnError(
            error ->
                logger.error(
                    "Error processing stream after {} messages: {}",
                    counter.get(),
                    error.getMessage()))
        .then(Mono.just(String.format("Processing started for %d messages", 1000)));
  }

  @GetMapping("/payload-flux-simple-large")
  public Mono<String> getBusinessPayloadFluxSimpleLarge() {
    final AtomicInteger counter = new AtomicInteger(0);
    final long startTime = System.currentTimeMillis();

    return businessClientService
        .getBusinessPayloadStreamLarge()
        .doOnNext(
            proto -> {
              int current = counter.incrementAndGet();
              if (current % 100 == 0) { // Log progress every 100 messages
                long currentTime = System.currentTimeMillis();
                long elapsed = currentTime - startTime;
                logger.info("Progress: {} messages processed in {} ms", current, elapsed);
              }

              try {
                BusinessPayloadDto dto = BusinessPayloadDto.fromProto(proto);
                String json = objectMapper.writeValueAsString(dto);
                logger.debug(
                    "Processed payload {}: {}",
                    current,
                    json); // Using debug level for detailed logs
              } catch (JsonProcessingException e) {
                logger.error("Error converting payload {} to JSON", current, e);
              }
            })
        .doOnComplete(
            () -> {
              long totalTime = System.currentTimeMillis() - startTime;
              logger.info(
                  "Completed processing {} messages in {} ms (avg: {} ms/msg)",
                  counter.get(),
                  totalTime,
                  String.format("%.2f", totalTime / (double) counter.get()));
            })
        .doOnError(
            error ->
                logger.error(
                    "Error processing stream after {} messages: {}",
                    counter.get(),
                    error.getMessage()))
        .then(Mono.just(String.format("Processing started for %d messages", 1000)));
  }
}
