package com.example.concurrentcallclient.service;

import com.example.concurrentcallclient.client.DataSourceClient;
import com.example.concurrentcallclient.model.GranularResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Service
public class GranularApiService {
  private final Logger logger = LoggerFactory.getLogger(GranularApiService.class);
  private final DataSourceClient dataSourceClient;

  public GranularApiService(DataSourceClient dataSourceClient) {
    this.dataSourceClient = dataSourceClient;
  }

  public Mono<ResponseEntity<List<GranularResponse>>> fetchDataGranular(int totalPages, int pageSize) {
    return Flux.range(0, totalPages)
            .flatMap(pageNum -> dataSourceClient.fetchDataGranular(pageNum, pageSize))
            .collectList()
            .map(this::createResponseEntity)
            .doOnSuccess(result -> logger.info("Completed fetchDataGranular: {}", result))
            .doOnError(error -> logger.error("Error in fetchDataGranular: {}", error.getMessage(), error));
  }

  private ResponseEntity<List<GranularResponse>> createResponseEntity(List<GranularResponse> responses) {
    boolean allSuccessful = responses != null && responses.size() == 15 && responses.stream().allMatch(Objects::nonNull);
    HttpStatus status = allSuccessful ? HttpStatus.OK : HttpStatus.PARTIAL_CONTENT;
    return ResponseEntity.status(status).body(responses);
  }
}