package com.example.concurrentcallclient.rest;

import com.example.concurrentcallclient.model.GranularResponse;
import com.example.concurrentcallclient.service.ApiService;
import com.example.concurrentcallclient.service.GranularApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ApiControllerTest {

  @MockBean
  private ApiService apiService;

  @MockBean
  private GranularApiService granularApiService;

  private ApiController apiController;

  @BeforeEach
  public void setup() {
    apiController = new ApiController(apiService, granularApiService);
  }

  @Test
  @DisplayName("fetchData delegates to ApiService and returns its result")
  public void fetchDataDelegatesToApiService() {
    when(apiService.fetchData(anyInt(), anyInt())).thenReturn(Mono.just("Overall Success"));

    Mono<String> result = apiController.fetchData();

    StepVerifier.create(result)
            .expectNext("Overall Success")
            .verifyComplete();
  }

  @Test
  @DisplayName("fetchDataGranular returns OK status when all responses are successful and match expected size")
  public void fetchDataGranularReturnsOkStatusWhenAllSuccessfulAndMatchSize() {
    int expectedSize = 15;
    List<GranularResponse> mockResponses = IntStream.range(0, expectedSize)
            .mapToObj(i -> new GranularResponse("Test" + i, "Data" + i, true, ""))
            .collect(Collectors.toList());

    when(granularApiService.fetchDataGranular(anyInt(), anyInt()))
            .thenReturn(Mono.just(mockResponses));

    Mono<ResponseEntity<List<GranularResponse>>> result = apiController.fetchDataGranular();

    StepVerifier.create(result)
            .expectNextMatches(response ->
                    response.getStatusCode() == HttpStatus.OK &&
                            Objects.requireNonNull(response.getBody()).size() == expectedSize &&
                            response.getBody().stream().allMatch(GranularResponse::result)
            )
            .verifyComplete();
  }

  @Test
  @DisplayName("fetchDataGranular returns PARTIAL_CONTENT status when responses don't match expected size")
  public void fetchDataGranularReturnsPartialContentStatusWhenSizeMismatch() {
    List<GranularResponse> mockResponses = IntStream.range(0, 10)  // Only 10 responses instead of expected 15
            .mapToObj(i -> new GranularResponse("Test" + i, "Data" + i, true, ""))
            .collect(Collectors.toList());

    when(granularApiService.fetchDataGranular(anyInt(), anyInt()))
            .thenReturn(Mono.just(mockResponses));

    Mono<ResponseEntity<List<GranularResponse>>> result = apiController.fetchDataGranular();

    StepVerifier.create(result)
            .expectNextMatches(response ->
                    response.getStatusCode() == HttpStatus.PARTIAL_CONTENT &&
                            Objects.requireNonNull(response.getBody()).size() == 10
            )
            .verifyComplete();
  }

  @Test
  @DisplayName("fetchDataGranular returns PARTIAL_CONTENT status when some responses are not successful")
  public void fetchDataGranularReturnsPartialContentStatusWhenSomeResponsesFail() {
    List<GranularResponse> mockResponses = IntStream.range(0, 15)
            .mapToObj(i -> new GranularResponse("Test" + i, "Data" + i, i % 2 == 0, i % 2 != 0 ? "Error" : ""))
            .collect(Collectors.toList());

    when(granularApiService.fetchDataGranular(anyInt(), anyInt()))
            .thenReturn(Mono.just(mockResponses));

    Mono<ResponseEntity<List<GranularResponse>>> result = apiController.fetchDataGranular();

    StepVerifier.create(result)
            .expectNextMatches(response ->
                    response.getStatusCode() == HttpStatus.PARTIAL_CONTENT &&
                            Objects.requireNonNull(response.getBody()).size() == 15 &&
                            response.getBody().stream().anyMatch(r -> !r.result())
            )
            .verifyComplete();
  }
}