package com.example.concurrentcallclient.service;

import com.example.concurrentcallclient.client.DataSourceClient;
import com.example.concurrentcallclient.model.GranularResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class GranularApiService {
  private final Logger logger = LoggerFactory.getLogger(GranularApiService.class);
  private final DataSourceClient dataSourceClient;

  public GranularApiService(DataSourceClient dataSourceClient) {
    this.dataSourceClient = dataSourceClient;
  }

  public Mono<List<GranularResponse>> fetchDataGranular(int totalPages, int pageSize) {
    return Flux.range(0, totalPages)
            .flatMap(pageNum -> dataSourceClient.fetchDataGranular(pageNum, pageSize))
            .collectList()
            .doOnSuccess(result -> logger.info("Completed fetchDataGranular: {} responses", result.size()))
            .doOnError(error -> logger.error("Error in fetchDataGranular: {}", error.getMessage(), error));
  }
}