package com.example.demo.config;

import com.example.demo.exception.ClientException;
import com.example.demo.exception.OtherException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

import java.time.Duration;

@Configuration
public class RetryConfig {
  @Bean
  public RetryBackoffSpec retrySPec() {
    return Retry.backoff(3, Duration.ofSeconds(3))
        .jitter(0.5)
        .doBeforeRetry(info -> System.out.println("doBeforeRetry: " + info.totalRetriesInARow()))
        .filter(throwable -> !(throwable instanceof ClientException))
        .onRetryExhaustedThrow(
            (retryBackoffSpec, retrySignal) -> {
              System.out.println("Retry exhausted");
              return new OtherException("Retry exhausted");
            });
  }
}
