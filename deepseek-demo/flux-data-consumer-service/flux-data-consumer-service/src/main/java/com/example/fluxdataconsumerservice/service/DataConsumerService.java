package com.example.fluxdataconsumerservice.service;

import com.example.fluxdataconsumerservice.model.MyObj;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class DataConsumerService {
    private static final Logger log = LoggerFactory.getLogger(DataConsumerService.class);
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public DataConsumerService(WebClient webClient, ObjectMapper objectMapper) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
    }

    // 消费单个对象流
    public Flux<MyObj> consumeStream() {
        return webClient.get()
                .uri("/api/data/stream")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class)
                .map(this::parseJsonObject)
                .doOnNext(obj -> {
                    if (log.isDebugEnabled()) {
                        log.debug("Received stream object: id={}", obj.getId());
                    }
                });
    }

    // 消费批量对象流
    public Flux<List<MyObj>> consumeBatches() {
        return webClient.get()
                .uri("/api/data/batch")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class)
                .map(this::parseJsonBatch)
                .doOnNext(batch -> {
                    if (log.isDebugEnabled()) {
                        log.debug("Received batch of size: {}", batch.size());
                    }
                });
    }

    // 有限单个对象流
    public Flux<MyObj> consumeLimitedStream(int count) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/data/stream-limited")
                        .queryParam("count", count)
                        .build())
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class)
                .map(this::parseJsonObject)
                .doOnNext(obj -> {
                    if (log.isDebugEnabled()) {
                        log.debug("Received limited stream object: id={}", obj.getId());
                    }
                });
    }

    // 有限批量流
    public Flux<List<MyObj>> consumeLimitedBatches(int count, int batchSize) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/data/batch-limited")
                        .queryParam("count", count)
                        .queryParam("batchSize", batchSize)
                        .build())
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class)
                .map(this::parseJsonBatch)
                .doOnNext(batch -> {
                    if (log.isDebugEnabled()) {
                        log.debug("Received limited batch of size: {}", batch.size());
                    }
                });
    }

    // 按名称的流式传输
    public Flux<MyObj> consumeStreamByName(String name, int pageSize) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/data/stream-on-name")
                        .queryParam("name", name)
                        .queryParam("pageSize", pageSize)
                        .build())
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class)
                .map(this::parseJsonObject)
                .doOnSubscribe(s -> log.info("Starting stream for name: {}", name))
                .doOnNext(obj -> {
                    if (log.isDebugEnabled()) {
                        log.debug("Received: id={}, name={}", obj.getId(), obj.getName());
                    }
                })
                .doOnError(e -> log.error("Stream error for name: {}", name, e))
                .doOnComplete(() -> log.info("Completed stream for name: {}", name));
    }

    // 按名称的批量传输
    public Flux<List<MyObj>> consumeBatchByName(String name, int batchSize) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/data/batch-on-name")
                        .queryParam("name", name)
                        .queryParam("batchSize", batchSize)
                        .build())
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class)
                .map(this::parseJsonBatch)
                .doOnSubscribe(s -> log.info("Starting batch stream for name: {}", name))
                .doOnNext(batch -> log.info("Received batch of {} items for name {}",
                        batch.size(), name))
                .doOnError(e -> log.error("Error in batch stream for name {}: {}",
                        name, e.getMessage()))
                .doOnComplete(() -> log.info("Completed batch stream for name: {}", name));
    }

    // 统一的单个对象 JSON 解析方法
    private MyObj parseJsonObject(String jsonStr) {
        try {
            return objectMapper.readValue(jsonStr, MyObj.class);
        } catch (JsonProcessingException e) {
            log.error("Error parsing JSON object: {}", jsonStr, e);
            throw new RuntimeException("Error parsing JSON object: " + e.getMessage(), e);
        }
    }

    // 统一的批量对象 JSON 解析方法
    private List<MyObj> parseJsonBatch(String jsonBatch) {
        try {
            return objectMapper.readValue(jsonBatch, new TypeReference<List<MyObj>>() {});
        } catch (JsonProcessingException e) {
            log.error("Error parsing JSON batch: {}", jsonBatch, e);
            throw new RuntimeException("Error parsing JSON batch: " + e.getMessage(), e);
        }
    }
}