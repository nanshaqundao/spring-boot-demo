package com.example.demo.service;

import com.example.demo.exception.OtherException;
import com.example.demo.model.DummyWrapper;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DummyService {

  private final CaffeineCache dummyCache;
  private final DummyClient dummyClient;

  private final String CACHE_NAME = "dummyCache";

  public DummyService(CaffeineCache dummyCache, DummyClient dummyClient) {
    this.dummyCache = dummyCache;
    this.dummyClient = dummyClient;
  }

  public Mono<DummyWrapper> getIsin(String name) {
    DummyWrapper dummyWrapper = dummyCache.get(name, DummyWrapper.class);
    if (dummyWrapper == null) {
      return dummyClient
          .getIsin(name)
          .doOnNext(
              entry -> {
                System.out.println("Caching added for " + name + " with value " + entry);
                dummyCache.put(name, entry);
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
