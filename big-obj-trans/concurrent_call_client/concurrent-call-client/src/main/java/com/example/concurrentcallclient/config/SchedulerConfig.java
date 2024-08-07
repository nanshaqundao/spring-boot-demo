package com.example.concurrentcallclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Configuration
public class SchedulerConfig {
  @Bean
  public Scheduler webClientScheduler() {
    return Schedulers.boundedElastic();
  }
}
