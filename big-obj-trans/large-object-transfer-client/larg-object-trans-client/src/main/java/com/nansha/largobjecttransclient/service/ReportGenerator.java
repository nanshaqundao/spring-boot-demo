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

    public List<MessageState> getAllMessages() {
        return allMessages;
    }

    private List<MessageState> allMessages;
    private static final Logger logger = LoggerFactory.getLogger(ReportGenerator.class);

    public ReportGenerator(PublishingServiceClient client) {
        this.client = client;
        this.allMessages = new ArrayList<>();
    }


    public void fetchAndProcessPage(int pageNumber) {
        Mono<List<MessageState>> messageStatesMono = client.getMessageStates(pageNumber, pageSize);

        messageStatesMono.subscribe(
                messageStates -> {
                    if (!messageStates.isEmpty()) {
                        allMessages.addAll(messageStates);
                        if (messageStates.size() == pageSize) {
                            fetchAndProcessPage(pageNumber + 1); // Fetch next page if the current page is full
                        } else {
                            finalizeReport(); // Finalize the report if this page is not full, indicating it's the last page
                        }
                    } else {
                        finalizeReport(); // Finalize the report if no messages are returned
                    }
                },
                error -> handleError(error)
        );
    }

    private void handleError(Throwable throwable) {
        logger.error("Error fetching message states: ", throwable);
        // Implement further error handling logic here, such as alerting or retry mechanisms
    }

    private void finalizeReport() {
        if (!allMessages.isEmpty()) {
            createReport(allMessages);
        } else {
            logger.info("No messages to report.");
            // Handle the case where there are no messages to report
        }
    }

    private void createReport(List<MessageState> messages) {
        // Logic to create and send the report
        // This is a placeholder, replace it with actual report generation logic
        logger.info("Creating report with {} messages.", messages.size());
        messages.forEach(message -> logger.info("Message: {}", message));
        // For example, format messages into a CSV, generate a PDF, or aggregate data into a summary
        // For demonstration, let's assume a simple CSV format
        StringBuilder reportBuilder = new StringBuilder("FieldA,FieldB,FieldC,FieldD\n");
        for (MessageState message : messages) {
            reportBuilder.append(message.getFieldA()).append(",")
                    .append(message.getFieldB()).append(",")
                    .append(message.getFieldC()).append(",")
                    .append(message.getFieldD()).append("\n");
        }

        // Example: Save the report to a file, send it via email, or store in a database
        // For simplicity, let's just print the report content
        System.out.println(reportBuilder.toString());
    }
}
