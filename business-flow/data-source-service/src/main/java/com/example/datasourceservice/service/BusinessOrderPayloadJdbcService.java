package com.example.datasourceservice.service;

import com.example.datasourceservice.entity.BusinessOrderPayload;
import com.example.datasourceservice.protobuf.BusinessPayload;
import com.example.datasourceservice.repository.BusinessOrderPayloadJdbcRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Service
public class BusinessOrderPayloadJdbcService {
  private static final Logger logger =
      LoggerFactory.getLogger(BusinessOrderPayloadJdbcService.class);

  private final BusinessOrderPayloadJdbcRepository businessOrderPayloadJdbcRepository;

  public BusinessOrderPayloadJdbcService(
      BusinessOrderPayloadJdbcRepository businessOrderPayloadJdbcRepository) {
    this.businessOrderPayloadJdbcRepository = businessOrderPayloadJdbcRepository;
  }

  public Flux<BusinessOrderPayload> findAllByNameReactive(String name) {
    return Flux.defer(() -> Flux.fromIterable(businessOrderPayloadJdbcRepository.findByName(name)))
        .subscribeOn(Schedulers.boundedElastic()); // Run on a non-blocking thread pool
  }

  public Flux<BusinessPayload> findAllByNameReactiveAsProto(String name) {
    return findAllByNameReactive(name)
        .map(
            entity ->
                BusinessPayload.newBuilder()
                    .setName(entity.getName())
                    .setOrder(entity.getOrderNumber() != null ? entity.getOrderNumber() : "")
                    .setContent(entity.getContent() != null ? entity.getContent() : "")
                    .build())
        .doOnSubscribe(
            s -> logger.info("Started converting entities to protobuf for name: {}", name))
        .doOnComplete(() -> logger.info("Completed protobuf conversion for name: {}", name))
        .doOnError(e -> logger.error("Error converting to protobuf for name: {}", name, e))
        .publishOn(Schedulers.boundedElastic());
  }
}
