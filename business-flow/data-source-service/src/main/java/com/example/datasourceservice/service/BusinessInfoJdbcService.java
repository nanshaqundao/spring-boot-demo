package com.example.datasourceservice.service;

import java.util.List;

import com.example.datasourceservice.entity.BusinessInfo;
import com.example.datasourceservice.repository.BusinessInfoJdbcRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Service
public class BusinessInfoJdbcService {

  private final BusinessInfoJdbcRepository repository;

  public BusinessInfoJdbcService(BusinessInfoJdbcRepository repository) {
    this.repository = repository;
  }

  public List<BusinessInfo> findAllByBusinessName(String name) {
    return repository.findByName(name);
  }

  public Flux<BusinessInfo> findAllByNameReactive(String name) {
    return Flux.defer(() -> Flux.fromIterable(repository.findByName(name)))
        .subscribeOn(Schedulers.boundedElastic()); // Run on a non-blocking thread pool
  }

  public Flux<BusinessInfo> findAllByNameReactiveWithBuffer(String name, int bufferSize) {
    return Flux.defer(() -> Flux.fromIterable(repository.findByName(name)))
        .buffer(bufferSize) // Sends data in chunks of the specified buffer size
        .flatMap(Flux::fromIterable)
        .switchIfEmpty(
            Flux.error(
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No businesses found with name: " + name)))
        .subscribeOn(Schedulers.boundedElastic()); // Run on a non-blocking thread pool
  }
}
