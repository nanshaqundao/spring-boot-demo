package com.example.fluxdataconsumerservice.service;

import com.example.fluxdataconsumerservice.model.MyObj;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class DataProcessor {
    private static final Logger log = LoggerFactory.getLogger(DataProcessor.class);

    public void processStream(Flux<MyObj> stream) {
        stream.subscribe(
                obj -> log.info("Processing object: {}", obj),
                err -> log.error("Stream error: {}", err.getMessage()),
                () -> log.info("Stream completed")
        );
    }

    public void processBatches(Flux<List<MyObj>> batches) {
        batches.subscribe(
                batch -> log.info("Processing batch of {} items", batch.size()),
                err -> log.error("Batch error: {}", err.getMessage()),
                () -> log.info("Batch processing completed")
        );
    }
}