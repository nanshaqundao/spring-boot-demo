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
    // Arrange
    when(apiService.fetchData(anyInt(), anyInt())).thenReturn(Mono.just("Overall Success"));

    // Act
    Mono<String> result = apiController.fetchData();

    // Assert
    StepVerifier.create(result)
            .expectNext("Overall Success")
            .verifyComplete();
  }

  @Test
  @DisplayName("fetchDataGranular delegates to GranularApiService and returns its result")
  public void fetchDataGranularDelegatesToGranularApiService() {
    // Arrange
    List<GranularResponse> mockResponses = List.of(
            new GranularResponse("Test1", "Data1", true, ""),
            new GranularResponse("Test2", "Data2", false, "Error")
    );
    ResponseEntity<List<GranularResponse>> mockResponseEntity =
            ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(mockResponses);

    when(granularApiService.fetchDataGranular(anyInt(), anyInt()))
            .thenReturn(Mono.just(mockResponseEntity));

    // Act
    Mono<ResponseEntity<List<GranularResponse>>> result = apiController.fetchDataGranular();

    // Assert
    StepVerifier.create(result)
            .expectNextMatches(response ->
                    response.getStatusCode() == HttpStatus.PARTIAL_CONTENT &&
                            Objects.requireNonNull(response.getBody()).size() == 2 &&
                            response.getBody().get(0).name().equals("Test1") &&
                            response.getBody().get(1).name().equals("Test2")
            )
            .verifyComplete();
  }
}