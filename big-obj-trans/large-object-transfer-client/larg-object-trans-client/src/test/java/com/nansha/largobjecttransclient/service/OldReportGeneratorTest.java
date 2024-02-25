package com.nansha.largobjecttransclient.service;

import com.nansha.largobjecttransclient.client.PublishingServiceClient;
import com.nansha.largobjecttransclient.model.MessageState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class OldReportGeneratorTest {
    @Mock
    private PublishingServiceClient publishingServiceClient;

    @InjectMocks
    private ReportGeneratorV0 reportGenerator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFetchAndProcessPage() {
        // Setup mock response
        MessageState messageState1 = new MessageState();
        messageState1.setFieldA("A1");
        MessageState messageState2 = new MessageState();
        messageState2.setFieldA("A2");
        List<MessageState> mockResponse = Arrays.asList(messageState1, messageState2);

        when(publishingServiceClient.getMessageStates(anyInt(), anyInt()))
                .thenReturn(Mono.just(mockResponse))
                .thenReturn(Mono.just(List.of())); // Simulate an empty page for the second call

        // Execute the method to be tested
        reportGenerator.fetchAndProcessPage(0);

        // Verify interactions and assertions
        verify(publishingServiceClient, times(1)).getMessageStates(anyInt(), anyInt());
        // Additional assertions for finalizeReport logic can go here, depending on what finalizeReport does

        // Since `finalizeReport` and `createReport` involve side effects (e.g., logging, writing to a file),
        // you might want to verify those side effects or ensure that `allMessages` contains the expected data.
        // This might require changing `finalizeReport` and `createReport` to be more test-friendly,
        // such as by allowing injection of dependencies or using callback mechanisms to observe effects.
    }

    @Test
    void testFetchAndProcessPage2() {
        // Setup mock response
        MessageState messageState1 = new MessageState();
        messageState1.setFieldA("A1");
        MessageState messageState2 = new MessageState();
        messageState2.setFieldA("A2");
        List<MessageState> mockResponse = Arrays.asList(messageState1, messageState2, messageState2, messageState2);

        when(publishingServiceClient.getMessageStates(anyInt(), anyInt()))
                .thenReturn(Mono.just(mockResponse))
                .thenReturn(Mono.just(Arrays.asList())); // Simulate an empty page for the second call

        // Execute the method to be tested
        reportGenerator.fetchAndProcessPage(0);

        // Verify interactions and assertions
        verify(publishingServiceClient, times(2)).getMessageStates(anyInt(), anyInt());
        // Additional assertions for finalizeReport logic can go here, depending on what finalizeReport does

        // Since `finalizeReport` and `createReport` involve side effects (e.g., logging, writing to a file),
        // you might want to verify those side effects or ensure that `allMessages` contains the expected data.
        // This might require changing `finalizeReport` and `createReport` to be more test-friendly,
        // such as by allowing injection of dependencies or using callback mechanisms to observe effects.
    }

    @Test
    void testFetchAndProcessPage3() {
        // Setup mock response
        MessageState messageState1 = new MessageState();
        messageState1.setFieldA("A1");
        MessageState messageState2 = new MessageState();
        messageState2.setFieldA("A2");
        List<MessageState> mockResponse = Arrays.asList(messageState1, messageState2, messageState2, messageState2);
        List<MessageState> mockResponse2 = Arrays.asList(messageState1, messageState2);
        when(publishingServiceClient.getMessageStates(anyInt(), anyInt()))
                .thenReturn(Mono.just(mockResponse))
                .thenReturn(Mono.just(mockResponse2)); // Simulate an empty page for the second call

        // Execute the method to be tested
        reportGenerator.fetchAndProcessPage(0);

        // Verify interactions and assertions
        verify(publishingServiceClient, times(2)).getMessageStates(anyInt(), anyInt());
        // Additional assertions for finalizeReport logic can go here, depending on what finalizeReport does

        // Since `finalizeReport` and `createReport` involve side effects (e.g., logging, writing to a file),
        // you might want to verify those side effects or ensure that `allMessages` contains the expected data.
        // This might require changing `finalizeReport` and `createReport` to be more test-friendly,
        // such as by allowing injection of dependencies or using callback mechanisms to observe effects.
    }
}