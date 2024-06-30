package com.example.concurrentcallclient.rest;

import com.example.concurrentcallclient.service.ApiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@RestController
@RequestMapping("/api")
public class ApiController {
  private final ApiService apiService;

  public ApiController(ApiService apiService) {
    this.apiService = apiService;
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
}
