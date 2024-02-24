package com.nansha.largobjecttransclient.service;

import com.nansha.largobjecttransclient.client.PublishingServiceClient;
import com.nansha.largobjecttransclient.client.SftpClient;
import com.nansha.largobjecttransclient.model.MessageState;
import com.nansha.largobjecttransclient.model.UploadResult;
import com.nansha.largobjecttransclient.util.CsvUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


@Service
public class ReportGenerator {
    private final PublishingServiceClient client;
    private final SftpClient sftpClient;
    private final int pageSize = 4; // Configurable page size

    private static final Logger logger = LoggerFactory.getLogger(ReportGenerator.class);

    public ReportGenerator(PublishingServiceClient client, SftpClient sftpClient) {
        this.client = client;
        this.sftpClient = sftpClient;
    }


    public Mono<UploadResult> fetchAndProcessPage(int pageNumber, List<MessageState> accumulatedStates) {
        // Directly working with Mono<List<MessageState>>
        return client.getMessageStates(pageNumber, pageSize)
                .flatMap(messageStates -> {
                    List<MessageState> newAccumulatedStates = new ArrayList<>(accumulatedStates);
                    newAccumulatedStates.addAll(messageStates);
                    if (!messageStates.isEmpty()) {
                        // Process the fetched page
                        // Here, add your logic to handle messageStates, like saving them or further processing
                        if (messageStates.size() == pageSize) {
                            // If full page, there might be more to fetch
                            return fetchAndProcessPage(pageNumber + 1, newAccumulatedStates);
                        } else {
                            // This was the last page
                            return finalizeReport(newAccumulatedStates);
                        }
                    } else {
                        // No data found, possibly finalize or handle as needed
                        return finalizeReport(newAccumulatedStates);
                    }
                });
    }

    private Mono<UploadResult> finalizeReport(List<MessageState> messageStates) {

        if (messageStates.isEmpty()) {
            logger.info("No data to report.");
            return Mono.just(new UploadResult("", false)); // Consider how you want to handle this case.
        }

        return Mono.fromCallable(() -> {
            logger.info("Finalizing report with {} entries.", messageStates.size());
            String csvContent = CsvUtils.convertListToCsv(messageStates, MessageState.class);
            // Assuming csvContent is transformed into a byte array within uploadFile method
            UploadResult uploadResult = sftpClient.uploadFile(csvContent, "report.csv");
            logger.info("Upload result: {}", uploadResult);
            return uploadResult;
        }).subscribeOn(Schedulers.boundedElastic()); // Ensures blocking I/O runs on a separate thread

    }
}
