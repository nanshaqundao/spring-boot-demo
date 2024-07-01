package com.example.concurrentcallclient.service;

import com.example.concurrentcallclient.client.DataSourceClient;
import com.example.concurrentcallclient.model.TargetObjectFromLargeObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApiServiceTest {

  @Mock
  private DataSourceClient dataSourceClient;

  private ApiService apiService;

  @BeforeEach
  void setUp() {
    apiService = new ApiService(dataSourceClient);
  }

  @Test
  void fetchDataReturnsOverallSuccessWhenAllCallsSucceed() {
    // Arrange
    when(dataSourceClient.fetchData(anyInt(), anyInt()))
            .thenReturn(Mono.just(new TargetObjectFromLargeObject("Test", "Data", "ProcessedData")));

    // Act & Assert
    StepVerifier.create(apiService.fetchData(15, 50))
            .expectNext("Overall Success")
            .verifyComplete();
  }

  @Test
  void fetchDataReturnsPartialSuccessWhenSomeCallsFail() {
    // Arrange
    when(dataSourceClient.fetchData(anyInt(), anyInt()))
            .thenReturn(Mono.just(new TargetObjectFromLargeObject("Test", "Data", "ProcessedData")))
            .thenReturn(Mono.empty())
            .thenReturn(Mono.just(new TargetObjectFromLargeObject("Test", "Data", "ProcessedData")));

    // Act & Assert
    StepVerifier.create(apiService.fetchData(15, 50))
            .expectNext("Partial Success")
            .verifyComplete();
  }

  @Test
  void fetchDataHandlesErrorsGracefully() {
    // Arrange
    when(dataSourceClient.fetchData(anyInt(), anyInt()))
            .thenReturn(Mono.error(new RuntimeException("Test error")));

    // Act & Assert
    StepVerifier.create(apiService.fetchData(15, 50))
            .expectNext("Partial Success")
            .verifyComplete();
  }

  @Test
  void determineOverallResultReturnsOverallSuccessWhenAllSuccessful() {
    // Arrange
    List<TargetObjectFromLargeObject> results = Arrays.asList(
            new TargetObjectFromLargeObject("Test1", "Data1", "ProcessedData1"),
            new TargetObjectFromLargeObject("Test2", "Data2", "ProcessedData2")
    );

    // Act
    String result = apiService.determineOverallResult(results, 2);

    // Assert
    assertEquals("Overall Success", result);
  }

  @Test
  void determineOverallResultReturnsPartialSuccessWhenSizeMismatch() {
    // Arrange
    List<TargetObjectFromLargeObject> results = Collections.singletonList(
            new TargetObjectFromLargeObject("Test", "Data", "ProcessedData")
    );

    // Act
    String result = apiService.determineOverallResult(results, 2);

    // Assert
    assertEquals("Partial Success", result);
  }

  @Test
  void determineOverallResultReturnsPartialSuccessWhenNullPresent() {
    // Arrange
    List<TargetObjectFromLargeObject> results = Arrays.asList(
            new TargetObjectFromLargeObject("Test", "Data", "ProcessedData"),
            null
    );

    // Act
    String result = apiService.determineOverallResult(results, 2);

    // Assert
    assertEquals("Partial Success", result);
  }
}