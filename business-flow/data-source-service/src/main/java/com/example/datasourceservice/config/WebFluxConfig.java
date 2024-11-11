package com.example.datasourceservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class WebFluxConfig implements WebFluxConfigurer {

  @Override
  public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
    // Increase the memory size to accommodate large payloads
    configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024); // 16 MB
  }
}
