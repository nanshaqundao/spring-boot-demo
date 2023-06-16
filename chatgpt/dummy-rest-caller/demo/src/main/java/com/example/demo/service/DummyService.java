package com.example.demo.service;

import com.example.demo.exception.OtherException;
import com.example.demo.model.DummyWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DummyService {
  private final CacheManager cacheManager;
  private final DummyClient dummyClient;

  private final String CACHE_NAME = "dummyCache";

  public DummyService(CacheManager cacheManager, DummyClient dummyClient) {
    this.cacheManager = cacheManager;
    this.dummyClient = dummyClient;
  }

  public Mono<DummyWrapper> getIsin(String name) {
    DummyWrapper dummyWrapper = cacheManager.getCache(CACHE_NAME).get(name, DummyWrapper.class);
    if (dummyWrapper == null) {
      return dummyClient
          .getIsin(name)
          .doOnNext(
              entry -> {
                System.out.println("Caching added for " + name + " with value " + entry);
                cacheManager.getCache(CACHE_NAME).put(name, entry);
              })
          .doOnError(
              error -> {
                System.out.println("doOnError: " + error);
                throw new OtherException("Other error");
              });
    }
    System.out.println(
        "Taking previously cached value for " + name + " with value " + dummyWrapper);
    return Mono.just(dummyWrapper);
  }
}
