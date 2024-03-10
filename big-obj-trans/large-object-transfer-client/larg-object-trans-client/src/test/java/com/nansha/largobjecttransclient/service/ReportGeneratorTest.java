package com.nansha.largobjecttransclient.service;

import com.nansha.largobjecttransclient.client.PublishingServiceClient;
import com.nansha.largobjecttransclient.client.SftpClient;
import com.nansha.largobjecttransclient.model.MessageState;
import com.nansha.largobjecttransclient.model.UploadResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class ReportGeneratorTest {

  @Mock private PublishingServiceClient client;

  @Mock private SftpClient sftpClient;

  @InjectMocks private ReportGenerator reportGenerator;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGenerateCombinedReportWithSuccess() {
    List<MessageState> messageStates1 = new ArrayList<>();
    messageStates1.add(new MessageState("valueA1", "valueB1", "valueC1", "valueD1"));
    List<MessageState> messageStates2 = new ArrayList<>();
    messageStates2.add(new MessageState("valueA2", "valueB2", "valueC2", "valueD2"));
    UploadResult expectedResult = new UploadResult("report.csv", true);

    when(client.getMessageStatesFromServer1(anyInt(), anyInt(), anyList()))
        .thenReturn(Mono.just(messageStates1));
    when(client.getMessageStatesFromServer2(anyInt(), anyInt(), anyList()))
        .thenReturn(Mono.just(messageStates2));
    when(sftpClient.uploadFile(anyString(), eq("report.csv"))).thenReturn(expectedResult);

    StepVerifier.create(reportGenerator.generateCombinedReport())
        .expectNextMatches(
            uploadResult ->
                uploadResult.getFileName().equals("report.csv") && uploadResult.isSuccess())
        .verifyComplete();
  }

  @Test
  void testGenerateCombinedReportWithEmptyResult() {
    when(client.getMessageStatesFromServer1(anyInt(), anyInt(), anyList()))
        .thenReturn(Mono.just(new ArrayList<>()));
    when(client.getMessageStatesFromServer2(anyInt(), anyInt(), anyList()))
        .thenReturn(Mono.just(new ArrayList<>()));

    StepVerifier.create(reportGenerator.generateCombinedReport())
        .expectNextMatches(
            uploadResult -> uploadResult.getFileName().isEmpty() && !uploadResult.isSuccess())
        .verifyComplete();
  }

  @Test
  void testGenerateCombinedReportWithError() {
    when(client.getMessageStatesFromServer1(anyInt(), anyInt(), anyList()))
        .thenReturn(Mono.error(new RuntimeException("Fetching error from server 1")));
    when(client.getMessageStatesFromServer2(anyInt(), anyInt(), anyList()))
        .thenReturn(Mono.just(new ArrayList<>()));

    StepVerifier.create(reportGenerator.generateCombinedReport())
        .expectErrorMatches(
            throwable -> throwable.getMessage().contains("Fetching error from server 1"))
        .verify();
  }

  //    @Test
  //    void testFetchAndProcessPageWithSuccess() {
  //        List<MessageState> messageStates = new ArrayList<>();
  //        messageStates.add(new MessageState("valueA", "valueB", "valueC", "valueD"));
  //        UploadResult expectedResult = new UploadResult("report.csv", true);
  //
  //        when(client.getMessageStates(anyInt(), anyInt())).thenReturn(Mono.just(messageStates));
  //        when(sftpClient.uploadFile(any(String.class),
  // any(String.class))).thenReturn(expectedResult);
  //
  //        StepVerifier.create(reportGenerator.fetchAndProcessPage(0, new ArrayList<>()))
  //                .expectNextMatches(uploadResult ->
  //                        uploadResult.getFileName().equals("report.csv") &&
  // uploadResult.isSuccess())
  //                .verifyComplete();
  //    }
  //
  //    @Test
  //    void testFetchAndProcessPageWithEmptyResult() {
  //        when(client.getMessageStates(anyInt(), anyInt())).thenReturn(Mono.just(new
  // ArrayList<>()));
  //
  //        StepVerifier.create(reportGenerator.fetchAndProcessPage(0, new ArrayList<>()))
  //                .expectNextMatches(uploadResult ->
  //                        uploadResult.getFileName().isEmpty() && !uploadResult.isSuccess())
  //                .verifyComplete();
  //    }
  //
  //    @Test
  //    void testFetchAndProcessPageWithError() {
  //        when(client.getMessageStates(anyInt(), anyInt()))
  //                .thenReturn(Mono.error(new RuntimeException("Fetching error")));
  //
  //        StepVerifier.create(reportGenerator.fetchAndProcessPage(0, new ArrayList<>()))
  //                .expectErrorMatches(throwable -> throwable.getMessage().contains("Fetching
  // error"))
  //                .verify();
  //    }
}
