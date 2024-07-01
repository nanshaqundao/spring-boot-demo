package com.example.concurrentcallclient.service;

import com.example.concurrentcallclient.client.DataSourceClient;
import com.example.concurrentcallclient.model.TargetObjectFromLargeObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
public class NewApiServiceTest {

  @Mock
  private DataSourceClient dataSourceClient;

  private ApiService apiService;

  @BeforeEach
  public void initialize() {
    apiService = new ApiService(dataSourceClient);
  }

  @Test
  @DisplayName("fetchData returns 'Overall Success' when all calls succeed")
  public void fetchDataReturnsOverallSuccessWhenAllCallsSucceed() {
    // Arrange
    when(dataSourceClient.fetchData(anyInt(), anyInt()))
            .thenReturn(Mono.just(new TargetObjectFromLargeObject("name", "data", "processedData")));

    // Act & Assert
    StepVerifier.create(apiService.fetchData(15, 50))
            .expectNext("Overall Success")
            .verifyComplete();
  }

  @Test
  @DisplayName("fetchData returns 'Partial Success' when some calls fail")
  public void fetchDataReturnsPartialSuccessWhenSomeCallsFail() {
    // Arrange
    when(dataSourceClient.fetchData(anyInt(), anyInt()))
            .thenReturn(Mono.just(new TargetObjectFromLargeObject("name", "data", "processedData")))
            .thenReturn(Mono.empty())
            .thenReturn(Mono.just(new TargetObjectFromLargeObject("name", "data", "processedData")));

    // Act & Assert
    StepVerifier.create(apiService.fetchData(3, 50))
            .expectNext("Partial Success")
            .verifyComplete();
  }

  @Test
  @DisplayName("fetchData handles API call failures gracefully")
  public void fetchDataHandlesApiCallFailuresGracefully() {
    // Arrange
    when(dataSourceClient.fetchData(anyInt(), anyInt()))
            .thenReturn(Mono.error(new RuntimeException("API call failed")));

    // Act & Assert
    StepVerifier.create(apiService.fetchData(1, 50))
            .expectNext("Partial Success")
            .verifyComplete();
  }

  @Test
  @DisplayName("fetchData handles mixed success, failures, and errors")
  public void fetchDataHandlesMixedSuccessFailuresAndErrors() {
    // Arrange
    when(dataSourceClient.fetchData(anyInt(), anyInt()))
            .thenReturn(Mono.just(new TargetObjectFromLargeObject("name1", "data1", "processedData1")))
            .thenReturn(Mono.empty())
            .thenReturn(Mono.error(new RuntimeException("API call failed")))
            .thenReturn(Mono.just(new TargetObjectFromLargeObject("name2", "data2", "processedData2")));

    // Act & Assert
    StepVerifier.create(apiService.fetchData(4, 50))
            .expectNext("Partial Success")
            .verifyComplete();
  }

  @Test
  @DisplayName("determineOverallResult returns 'Overall Success' when all successful")
  public void determineOverallResultReturnsOverallSuccessWhenAllSuccessful() {
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
  @DisplayName("determineOverallResult returns 'Partial Success' when size mismatch")
  public void determineOverallResultReturnsPartialSuccessWhenSizeMismatch() {
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
  @DisplayName("determineOverallResult returns 'Partial Success' when null present")
  public void determineOverallResultReturnsPartialSuccessWhenNullPresent() {
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