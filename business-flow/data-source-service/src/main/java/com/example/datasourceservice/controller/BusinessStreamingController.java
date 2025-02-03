package com.example.datasourceservice.controller;

import com.example.datasourceservice.service.BusinessOrderPayloadJdbcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/stream")
public class BusinessStreamingController {
  private static final Logger logger = LoggerFactory.getLogger(BusinessStreamingController.class);
  private final BusinessOrderPayloadJdbcService businessOrderPayloadJdbcService;

  public BusinessStreamingController(
      BusinessOrderPayloadJdbcService businessOrderPayloadJdbcService) {
    this.businessOrderPayloadJdbcService = businessOrderPayloadJdbcService;
  }

  // Alternative SSE endpoint if needed
  @GetMapping(path = "/content/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<String> streamContentSSE(@RequestParam String name) {
    return businessOrderPayloadJdbcService
        .findAllContentByNameReactive(name)
        .doOnSubscribe(s -> logger.info("Client subscribed to SSE stream for name: {}", name))
        .doOnCancel(() -> logger.info("Client cancelled SSE stream for name: {}", name))
        .doOnComplete(() -> logger.info("Client completed SSE stream for name: {}", name));
  }
}
