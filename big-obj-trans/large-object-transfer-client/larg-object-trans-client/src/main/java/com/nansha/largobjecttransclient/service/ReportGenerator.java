package com.nansha.largobjecttransclient.service;

import com.nansha.largobjecttransclient.client.PublishingServiceClient;
import com.nansha.largobjecttransclient.model.MessageState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;


@Service
public class ReportGenerator {
    private final PublishingServiceClient client;
    private final int pageSize = 4; // Configurable page size

    private static final Logger logger = LoggerFactory.getLogger(ReportGenerator.class);

    public ReportGenerator(PublishingServiceClient client) {
        this.client = client;
    }




    public Mono<Void> fetchAndProcessPage(int pageNumber, List<MessageState> accumulatedStates) {
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
                            return fetchAndProcessPage(pageNumber + 1,newAccumulatedStates);
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

    private Mono<Void> finalizeReport(List<MessageState> messageStates) {
        // Here, implement your logic to finalize the report with the accumulated messageStates
        // For example, saving the report, logging, or sending it over SFTP
        // Return Mono<Void> to signify completion of this step
        // Implement the finalization logic here, working with all accumulated message states
        return Mono.fromRunnable(() -> {
            if (!messageStates.isEmpty()) {
                logger.info("Finalizing report with {} entries.", messageStates.size());
                // Further processing, such as generating and sending the report
            } else {
                logger.info("No data to report.");
            }
        });
    }
}
