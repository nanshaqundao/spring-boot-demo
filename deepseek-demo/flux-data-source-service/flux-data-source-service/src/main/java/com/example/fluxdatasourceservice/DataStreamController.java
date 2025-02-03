package com.example.fluxdatasourceservice;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/api/data")
public class DataStreamController {
    private static final Logger log = LoggerFactory.getLogger(DataStreamController.class);

    // 原始无限流端点
    @GetMapping(path = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamObjects() {
        return generateObjectStream(null)
                .map(MyObj::toString);
    }

    // 有限单个对象流
    @GetMapping(path = "/stream-limited", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamLimitedObjects(
            @RequestParam(name = "count", defaultValue = "100") int count) {
        return generateObjectStream(count)
                .map(MyObj::toString);
    }

    // 原始批量流端点
    @GetMapping(path = "/batch", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamBatches() {
        return generateObjectStream(null)
                .map(MyObj::toString)
                .bufferTimeout(50, Duration.ofMillis(500))
                .map(this::formatBatch);
    }

    // 有限批量流
    @GetMapping(path = "/batch-limited", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamLimitedBatches(
            @RequestParam(name = "count", defaultValue = "100") int count,
            @RequestParam(name = "batchSize", defaultValue = "50") int batchSize) {
        return generateObjectStream(count)
                .map(MyObj::toString)
                .bufferTimeout(batchSize, Duration.ofMillis(500))
                .map(this::formatBatch);
    }

    // 统一数据生成逻辑
    private Flux<MyObj> generateObjectStream(Integer maxCount) {
        Flux<MyObj> baseStream = Flux.interval(Duration.ofMillis(50))
                .map(seq -> new MyObj(seq, generateLargeContent()))
                .doOnNext(obj -> log.info("Generated object: {}", obj))
                .onBackpressureBuffer(1000);

        return maxCount != null ? baseStream.take(maxCount) : baseStream;
    }

    // 格式化批次数据
    private String formatBatch(List<String> batch) {
        return String.join("\n", batch) + "\n---Batch End---\n";
    }

    private String generateLargeContent() {
        return "{data: '" + RandomStringUtils.randomAlphanumeric(9500) + "'}";
    }
}