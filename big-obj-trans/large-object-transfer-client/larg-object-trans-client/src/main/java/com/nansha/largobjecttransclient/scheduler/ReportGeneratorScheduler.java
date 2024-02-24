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

    @Scheduled(cron = "*/10 * * * * ?") // Runs at 1 AM every day
    public void generateReport() {
        System.out.println("Starting report generation");
        allMessages = new ArrayList<>();
        reportGenerator.fetchAndProcessPage(0, allMessages)
                .subscribe(
                        uploadResult -> System.out.println("Report generation completed: " + uploadResult),
                        error -> System.out.println("Error fetching message states: " + error),
                        () -> {
                            System.out.println("--------Report generation completed");
                        }
                );

    }
}
