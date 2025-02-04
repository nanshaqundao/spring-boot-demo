package com.example.fluxdataconsumerservice.controller;

import com.example.fluxdataconsumerservice.service.DataConsumerService;
import com.example.fluxdataconsumerservice.service.DataProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/consumer")
public class DataConsumerController {
    private static final Logger log = LoggerFactory.getLogger(DataConsumerController.class);
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

    // 新增：按名称的流式传输
    @GetMapping("/stream-by-name")
    public String startStreamByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "100") int pageSize) {
        log.info("Starting stream by name: {}, pageSize: {}", name, pageSize);
        dataProcessor.processStream(
                dataConsumerService.consumeStreamByName(name, pageSize)
        );
        return "Stream by name started: " + name;
    }

    // 新增：按名称的批量传输
    @GetMapping("/batch-by-name")
    public String startBatchByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "100") int batchSize) {
        log.info("Starting batch by name: {}, batchSize: {}", name, batchSize);
        dataProcessor.processBatches(
                dataConsumerService.consumeBatchByName(name, batchSize)
        );
        return "Batch by name started: " + name;
    }
}