package com.nansha.largobjecttransclient.scheduler;

import com.nansha.largobjecttransclient.model.MessageState;
import com.nansha.largobjecttransclient.service.ReportGenerator;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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

    reportGenerator
        .generateCombinedReport()
        .subscribe(
            uploadResult -> System.out.println("Report generation completed: " + uploadResult),
            error -> System.out.println("Error fetching message states: " + error),
            () -> {
              System.out.println("--------Report generation completed");
            });
  }
}
