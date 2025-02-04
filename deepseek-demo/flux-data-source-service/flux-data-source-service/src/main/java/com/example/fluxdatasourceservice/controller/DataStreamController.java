package com.example.fluxdatasourceservice.controller;

import com.example.fluxdatasourceservice.model.MyObj;
import com.example.fluxdatasourceservice.repository.MyObjRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/api/data")
public class DataStreamController {
    private static final Logger log = LoggerFactory.getLogger(DataStreamController.class);
    private final MyObjRepository myObjRepository;
    private final ObjectMapper objectMapper;

    public DataStreamController(MyObjRepository myObjRepository, ObjectMapper objectMapper) {
        this.myObjRepository = myObjRepository;
        this.objectMapper = objectMapper;
    }

    // 原始无限流端点
    @GetMapping(path = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamObjects() {
        return generateObjectStream(null).map(MyObj::toString);
    }

    // 有限单个对象流
    @GetMapping(path = "/stream-limited", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamLimitedObjects(@RequestParam(name = "count", defaultValue = "100") int count) {
        return generateObjectStream(count).map(MyObj::toString);
    }

    // 原始批量流端点
    @GetMapping(path = "/batch", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamBatches() {
        return generateObjectStream(null).map(MyObj::toString).bufferTimeout(50, Duration.ofMillis(500)).map(this::formatBatch);
    }

    // 有限批量流
    @GetMapping(path = "/batch-limited", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamLimitedBatches(@RequestParam(name = "count", defaultValue = "100") int count, @RequestParam(name = "batchSize", defaultValue = "50") int batchSize) {
        return generateObjectStream(count).map(MyObj::toString).bufferTimeout(batchSize, Duration.ofMillis(500)).map(this::formatBatch);
    }

    // 统一数据生成逻辑
    private Flux<MyObj> generateObjectStream(Integer maxCount) {
        Flux<MyObj> baseStream = Flux.interval(Duration.ofMillis(50)).map(seq -> new MyObj("demo", generateLargeContent())).doOnNext(obj -> log.info("Generated object: {}", obj)).onBackpressureBuffer(1000);

        return maxCount != null ? baseStream.take(maxCount) : baseStream;
    }

    // 格式化批次数据
    private String formatBatch(List<String> batch) {
        return String.join("\n", batch) + "\n---Batch End---\n";
    }

    private String generateLargeContent() {
        return "{data: '" + RandomStringUtils.randomAlphanumeric(9500) + "'}";
    }


    // 按名称流式传输
    @GetMapping(path = "/stream-on-name", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "100") int pageSize) {

        return Flux.defer(() -> {
                    log.info("Starting stream by name: {}, pageSize: {}", name, pageSize);

                    return Mono.fromCallable(() -> myObjRepository.countByName(name))
                            .subscribeOn(Schedulers.boundedElastic())
                            .flatMapMany(total -> {
                                int totalPages = (int) Math.ceil((double) total / pageSize);
                                log.info("Total records: {}, pages: {}", total, totalPages);

                                return Flux.range(0, totalPages)
                                        .delayElements(Duration.ofMillis(100))
                                        .flatMap(page ->
                                                        Mono.fromCallable(() -> {
                                                                    log.debug("Fetching page {} of {}", page, totalPages);
                                                                    return myObjRepository.findByName(name, PageRequest.of(page, pageSize));
                                                                })
                                                                .subscribeOn(Schedulers.boundedElastic())
                                                                .flatMapIterable(list -> list)
                                                                .map(obj -> {
                                                                    try {
                                                                        return objectMapper.writeValueAsString(obj);
                                                                    } catch (JsonProcessingException e) {
                                                                        log.error("Error serializing object: {}", obj, e);
                                                                        throw new RuntimeException(e);
                                                                    }
                                                                }),
                                                1
                                        );
                            })
                            .doOnNext(str -> {
                                if (log.isDebugEnabled()) {
                                    log.debug("Sending: {}", str);
                                }
                            })
                            .publishOn(Schedulers.boundedElastic());
                })
                .timeout(Duration.ofMinutes(5))
                .doOnError(e -> log.error("Error streaming for name: {}", name, e))
                .doOnComplete(() -> log.info("Completed streaming for name: {}", name));
    }


    @GetMapping(path = "/batch-on-name", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> batchByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "100") int batchSize) {

        return Flux.defer(() -> {
                    log.info("Starting batch stream by name: {}, batchSize: {}", name, batchSize);

                    return Mono.fromCallable(() -> myObjRepository.countByName(name))
                            .subscribeOn(Schedulers.boundedElastic())
                            .flatMapMany(total -> {
                                int totalPages = (int) Math.ceil((double) total / batchSize);
                                log.info("Total records: {}, pages: {}", total, totalPages);

                                return Flux.range(0, totalPages)
                                        .delayElements(Duration.ofMillis(100))
                                        .flatMap(page ->
                                                        Mono.fromCallable(() -> {
                                                                    log.debug("Fetching page {} of {}", page, totalPages);
                                                                    List<MyObj> batch = myObjRepository.findByName(name,
                                                                            PageRequest.of(page, batchSize));
                                                                    try {
                                                                        // 将整个批次序列化为一个 JSON 字符串
                                                                        return objectMapper.writeValueAsString(batch);
                                                                    } catch (JsonProcessingException e) {
                                                                        log.error("Error serializing batch: {}", batch, e);
                                                                        throw new RuntimeException(e);
                                                                    }
                                                                })
                                                                .subscribeOn(Schedulers.boundedElastic()),
                                                1
                                        );
                            })
                            .doOnNext(str -> {
                                if (log.isDebugEnabled()) {
                                    log.debug("Sending batch");
                                }
                            })
                            .publishOn(Schedulers.boundedElastic());
                })
                .timeout(Duration.ofMinutes(5))
                .doOnError(e -> log.error("Error in batch stream for name: {}", name, e))
                .doOnComplete(() -> log.info("Completed batch stream for name: {}", name));
    }
}