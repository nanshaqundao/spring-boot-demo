package com.example.concurrentcallclient.rest;

import com.example.concurrentcallclient.model.TargetObjectFromLargeObject;
import com.example.concurrentcallclient.service.ApiService;
import com.example.concurrentcallclient.service.GranularApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;

@SpringBootTest
public class ApiControllerTest {

  @MockBean private ApiService apiService;
  @MockBean private GranularApiService granularApiService;

  private ApiController apiController;

  @BeforeEach
  public void setup() {
    apiController = new ApiController(apiService, granularApiService);
  }

  @Test
  @DisplayName("fetchData returns Overall Success when all API calls are successful")
  public void fetchDataReturnsOverallSuccessWhenAllApiCallsAreSuccessful() {
    TargetObjectFromLargeObject mockObject = Mockito.mock(TargetObjectFromLargeObject.class);
    Mockito.when(apiService.fetchDataFromApi(anyInt(), anyInt())).thenReturn(Mono.just(mockObject));

    Mono<String> result = apiController.fetchData();

    assertEquals("Overall Success", result.block());
  }

  @Test
  @DisplayName("fetchData returns Overall Failure when any API call fails")
  public void fetchDataReturnsOverallFailureWhenAnyApiCallFails() {
    Mockito.when(apiService.fetchDataFromApi(anyInt(), anyInt()))
        .thenReturn(Mono.error(new RuntimeException("API call failed")));

    Mono<String> result = apiController.fetchData();

    assertEquals("Overall Failure", result.block());
  }
}
