package com.nansha.largobjecttransclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientProvider {
  @Bean
  public WebClient webClient1() {
    return WebClient.builder()
        .baseUrl("http://localhost:8080/message") // Default base URL
        .build();
  }

  @Bean
  public WebClient webClient2() {
    return WebClient.builder()
            .baseUrl("http://localhost:9081/message")
            .build();
  }
}
