package com.nansha.largobjecttransclient.scheduler;

import com.nansha.largobjecttransclient.model.MessageState;
import com.nansha.largobjecttransclient.service.ReportGenerator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportGeneratorScheduler {
    private final int pageSize = 50; // Configurable page size
    private final ReportGenerator reportGenerator;
    private List<MessageState> allMessages;

    public ReportGeneratorScheduler(ReportGenerator reportGenerator) {
        this.reportGenerator = reportGenerator;
    }

    @Scheduled(cron = "0 0 1 * * ?") // Runs at 1 AM every day
    public void generateReport() {
        allMessages = new ArrayList<>();
        reportGenerator.fetchAndProcessPage(0, allMessages)
                .subscribe(
                        null,
                        error -> System.out.println("Error fetching message states: " + error),
                        () -> {
                            allMessages.forEach(messageState -> System.out.println("Message: " + messageState));
                        }
                );

    }
}
