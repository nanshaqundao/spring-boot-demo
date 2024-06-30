package com.example.concurrentcallclient.rest;

import com.example.concurrentcallclient.model.GranularResponse;
import com.example.concurrentcallclient.service.ApiService;
import com.example.concurrentcallclient.service.GranularApiService;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class ApiController {
  private final Logger logger = org.slf4j.LoggerFactory.getLogger(ApiController.class);
  private final ApiService apiService;
  private final GranularApiService granularApiService;

  public ApiController(ApiService apiService, GranularApiService granularApiService) {
    this.apiService = apiService;
    this.granularApiService = granularApiService;
  }

  @GetMapping("/fetch")
  public Mono<String> fetchData() {
    int pageSize = 50;
    int totalPages = 15; // Assuming we want to fetch 15 pages

    return Flux.range(0, totalPages)
        .flatMap(pageNum -> apiService.fetchDataFromApi(pageNum, pageSize))
        .collectList()
        .map(
            results -> {
              boolean allSuccessful = results.stream().allMatch(Objects::nonNull);
              return allSuccessful ? "Overall Success" : "Partial Success";
            })
        .onErrorResume(
            error -> {
              System.err.println("Overall failure: " + error.getMessage());
              return Mono.just("Overall Failure");
            });
  }

  @GetMapping("/fetch-granular")
  public Mono<ResponseEntity<List<GranularResponse>>> fetchDataGranular() {
    int pageSize = 50;
    int totalPages = 15; // Assuming we want to fetch 15 pages

    return Flux.range(0, totalPages)
        .flatMap(
            pageNum ->
                granularApiService
                    .fetchDataFromApiGranular(pageNum, pageSize)
                    .onErrorResume(
                        error -> {
                          // This ensures we always get a GranularResponse, even if the service
                          // method throws an unexpected error
                          logger.error(
                              "Unexpected error in controller: {}", error.getMessage(), error);
                          return Mono.just(
                              new GranularResponse(
                                  "Unknown", "", false, "Unexpected error: " + error.getMessage()));
                        }))
        .collectList()
        .map(
            responses -> {
              boolean allSuccessful =
                  responses != null
                      && responses.size() == 15
                      && responses.stream().allMatch(Objects::nonNull);
              HttpStatus status = allSuccessful ? HttpStatus.OK : HttpStatus.PARTIAL_CONTENT;

              return ResponseEntity.status(status).body(responses);
            })
        .onErrorResume(
            error -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
  }
}
