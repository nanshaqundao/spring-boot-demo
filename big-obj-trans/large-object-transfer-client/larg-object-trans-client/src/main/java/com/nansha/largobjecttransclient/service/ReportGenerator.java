package com.nansha.largobjecttransclient.service;

import com.nansha.largobjecttransclient.client.PublishingServiceClient;
import com.nansha.largobjecttransclient.client.SftpClient;
import com.nansha.largobjecttransclient.model.MessageState;
import com.nansha.largobjecttransclient.model.UploadResult;
import com.nansha.largobjecttransclient.util.CsvUtils;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

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

  private Mono<UploadResult> finalizeReport(List<MessageState> messageStates) {

    if (messageStates.isEmpty()) {
      logger.info("No data to report.");
      return Mono.just(new UploadResult("", false)); // Consider how you want to handle this case.
    }

    return Mono.fromCallable(
            () -> {
              logger.info("Finalizing report with {} entries.", messageStates.size());
              logger.info("Converting to CSV the following message states: {}", messageStates);
              String csvContent = CsvUtils.convertListToCsv(messageStates, MessageState.class);
              // Assuming csvContent is transformed into a byte array within uploadFile method
              UploadResult uploadResult = sftpClient.uploadFile(csvContent, "report.csv");
              logger.info("Upload result: {}", uploadResult);
              return uploadResult;
            })
        .subscribeOn(Schedulers.boundedElastic()) // Ensures blocking I/O runs on a separate thread
        .onErrorResume(
            e -> {
              logger.error("Failed to upload report", e);
              return Mono.just(new UploadResult("filename", false)); // Indicate failure.
            });
  }

  // new ways
  public Mono<UploadResult> generateCombinedReport() {
    Mono<List<MessageState>> call1 = client.getMessageStatesFromServer1(0, 4, new ArrayList<>());
    Mono<List<MessageState>> call2 = client.getMessageStatesFromServer2(0, 4, new ArrayList<>());

    return Mono.zip(call1, call2)
        .flatMap(
            tuple -> {
              List<MessageState> combinedList = new ArrayList<>();
              combinedList.addAll(tuple.getT1());
              combinedList.addAll(tuple.getT2());

              return finalizeReport(combinedList);
            });
  }
}
