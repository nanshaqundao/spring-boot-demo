package com.example.concurrentcallclient.service;

import com.example.concurrentcallclient.client.DataSourceClient;
import com.example.concurrentcallclient.model.TargetObjectFromLargeObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Service
public class ApiService {
  private final Logger logger = LoggerFactory.getLogger(ApiService.class);
  private final DataSourceClient dataSourceClient;

  public ApiService(DataSourceClient dataSourceClient) {
    this.dataSourceClient = dataSourceClient;
  }

  public Mono<String> fetchData(int totalPages, int pageSize) {
    return Flux.range(0, totalPages)
            .flatMap(pageNum -> dataSourceClient.fetchData(pageNum, pageSize)
                    .onErrorResume(e -> {
                      logger.error("Error fetching data for page {}: {}", pageNum, e.getMessage());
                      return Mono.empty();
                    }))
            .collectList()
            .map(results -> determineOverallResult(results, totalPages))
            .doOnSuccess(result -> logger.info("Completed fetchData: {}", result))
            .onErrorResume(error -> {
              logger.error("Unexpected error in fetchData: {}", error.getMessage(), error);
              return Mono.just("Partial Success");
            });
  }

  String determineOverallResult(List<TargetObjectFromLargeObject> results, int expectedSize) {
    boolean allSuccessful = results.size() == expectedSize && results.stream().allMatch(Objects::nonNull);
    return allSuccessful ? "Overall Success" : "Partial Success";
  }
}