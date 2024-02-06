package com.nansha.largobjecttransclient.scheduler;

import com.nansha.largobjecttransclient.model.MessageState;
import com.nansha.largobjecttransclient.service.ReportGenerator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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
        allMessages.clear(); // Resetting the list for a new report
        reportGenerator.fetchAndProcessPage(0);
        allMessages = reportGenerator.getAllMessages();
    }
}
