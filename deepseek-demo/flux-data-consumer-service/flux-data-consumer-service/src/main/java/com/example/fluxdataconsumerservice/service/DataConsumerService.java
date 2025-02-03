package com.example.fluxdataconsumerservice.service;

import com.example.fluxdataconsumerservice.model.MyObj;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DataConsumerService {
    private final WebClient webClient;

    public DataConsumerService(WebClient webClient) {
        this.webClient = webClient;
    }

    // 消费单个对象流
    public Flux<MyObj> consumeStream() {
        return webClient.get()
                .uri("/api/data/stream")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class)
                .map(this::parseObject);
    }

    // 消费批量对象流
    public Flux<List<MyObj>> consumeBatches() {
        return webClient.get()
                .uri("/api/data/batch")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class)
                .flatMap(this::parseBatch);
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
                .map(this::parseObject);
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
                .flatMap(this::parseBatch);
    }


    // 解析单个对象字符串
    private MyObj parseObject(String line) {
        // 解析示例：MyObj{id=0, content='...'}
        Pattern pattern = Pattern.compile("MyObj\\{id=(\\d+), content='(.*)'\\}");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            long id = Long.parseLong(matcher.group(1));
            String content = matcher.group(2);
            return new MyObj(id, content);
        }
        throw new IllegalArgumentException("Invalid format: " + line);
    }

    // 解析批量数据
    private Flux<List<MyObj>> parseBatch(String batchBlock) {
        return Flux.fromStream(
                Arrays.stream(batchBlock.split("\n"))
                        .filter(line -> !line.trim().isEmpty() && !line.contains("---Batch End---"))
                        .map(this::parseObject)
        ).collectList().flux();
    }
}