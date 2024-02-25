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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class ReportGeneratorTest {

    @Mock
    private PublishingServiceClient client;

    @Mock
    private SftpClient sftpClient;

    @InjectMocks
    private ReportGenerator reportGenerator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFetchAndProcessPageWithSuccess() {
        List<MessageState> messageStates = new ArrayList<>();
        messageStates.add(new MessageState("valueA", "valueB", "valueC", "valueD"));
        UploadResult expectedResult = new UploadResult("report.csv", true);

        when(client.getMessageStates(anyInt(), anyInt())).thenReturn(Mono.just(messageStates));
        when(sftpClient.uploadFile(any(String.class), any(String.class))).thenReturn(expectedResult);

        StepVerifier.create(reportGenerator.fetchAndProcessPage(0, new ArrayList<>()))
                .expectNextMatches(uploadResult ->
                        uploadResult.getFileName().equals("report.csv") && uploadResult.isSuccess())
                .verifyComplete();
    }

    @Test
    void testFetchAndProcessPageWithEmptyResult() {
        when(client.getMessageStates(anyInt(), anyInt())).thenReturn(Mono.just(new ArrayList<>()));

        StepVerifier.create(reportGenerator.fetchAndProcessPage(0, new ArrayList<>()))
                .expectNextMatches(uploadResult ->
                        uploadResult.getFileName().isEmpty() && !uploadResult.isSuccess())
                .verifyComplete();
    }

    @Test
    void testFetchAndProcessPageWithError() {
        when(client.getMessageStates(anyInt(), anyInt()))
                .thenReturn(Mono.error(new RuntimeException("Fetching error")));

        StepVerifier.create(reportGenerator.fetchAndProcessPage(0, new ArrayList<>()))
                .expectErrorMatches(throwable -> throwable.getMessage().contains("Fetching error"))
                .verify();
    }
}