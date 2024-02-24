package com.nansha.largobjecttransclient.service;

import com.nansha.largobjecttransclient.client.PublishingServiceClient;
import com.nansha.largobjecttransclient.model.MessageState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportGeneratorV1 {
    private final PublishingServiceClient client;
    private final int pageSize = 4; // Configurable page size

    private static final Logger logger = LoggerFactory.getLogger(ReportGenerator.class);

    public ReportGeneratorV1(PublishingServiceClient client) {
        this.client = client;
    }


    public Mono<List<MessageState>> fetchAndProcessPages(int pageNumber, List<MessageState> accumulatedStates) {
        return client.getMessageStates(pageNumber, pageSize)
                .flatMap(messageStates -> {
                    List<MessageState> newAccumulatedStates = new ArrayList<>(accumulatedStates);
                    newAccumulatedStates.addAll(messageStates);
                    if (!messageStates.isEmpty() && messageStates.size() == pageSize) {
                        // Recursive call to fetch next page
                        return fetchAndProcessPages(pageNumber + 1, newAccumulatedStates);
                    } else {
                        // Return the accumulated states when no more pages are left to fetch
                        return Mono.just(newAccumulatedStates);
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
