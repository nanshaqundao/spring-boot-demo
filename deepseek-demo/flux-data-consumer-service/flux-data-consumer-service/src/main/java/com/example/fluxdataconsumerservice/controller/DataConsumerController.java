package com.example.fluxdataconsumerservice.controller;

import com.example.fluxdataconsumerservice.service.DataConsumerService;
import com.example.fluxdataconsumerservice.service.DataProcessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/consumer")
public class DataConsumerController {
    private final DataConsumerService dataConsumerService;
    private final DataProcessor dataProcessor;

    public DataConsumerController(DataConsumerService dataConsumerService, DataProcessor dataProcessor) {
        this.dataConsumerService = dataConsumerService;
        this.dataProcessor = dataProcessor;
    }

    @GetMapping("/stream")
    public String startStream() {
        dataProcessor.processStream(dataConsumerService.consumeStream());
        return "Stream processing started";
    }

    @GetMapping("/batch")
    public String startBatch() {
        dataProcessor.processBatches(dataConsumerService.consumeBatches());
        return "Batch processing started";
    }

    // 有限单个对象流接口
    @GetMapping("/stream-limited")
    public String startLimitedStream(
            @RequestParam(name = "count", defaultValue = "100") int count) {
        dataProcessor.processStream(dataConsumerService.consumeLimitedStream(count));
        return "Limited stream started (count: " + count + ")";
    }

    // 有限批量流接口
    @GetMapping("/batch-limited")
    public String startLimitedBatches(
            @RequestParam(name = "count", defaultValue = "100") int count,
            @RequestParam(name = "batchSize", defaultValue = "50") int batchSize) {
        dataProcessor.processBatches(dataConsumerService.consumeLimitedBatches(count, batchSize));
        return "Limited batches started (count: " + count + ", batchSize: " + batchSize + ")";
    }
}