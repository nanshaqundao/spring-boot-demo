package com.example.businessclientservice.controller;

import com.example.businessclientservice.model.BusinessPayloadDto;
import com.example.businessclientservice.model.BusinessPayloadFluxResponse;
import com.example.businessclientservice.service.BusinessClientService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/client/business")
public class BusinessClientController {
  private static final Logger logger = LoggerFactory.getLogger(BusinessClientController.class);
  private final BusinessClientService businessClientService;
  private final ObjectMapper objectMapper;

  public BusinessClientController(
      BusinessClientService businessClientService,
      ObjectMapper objectMapper) { // Spring Boot auto-configures ObjectMapper
    this.businessClientService = businessClientService;
    this.objectMapper = objectMapper;
  }

  @GetMapping("/payload-flux")
  public Mono<BusinessPayloadFluxResponse> getBusinessPayloadFlux() {
    return businessClientService
        .getBusinessPayloadStream()
        .<String>handle(
            (proto, sink) -> {
              BusinessPayloadDto dto = BusinessPayloadDto.fromProto(proto);
              try {
                sink.next(objectMapper.writeValueAsString(dto));
              } catch (JsonProcessingException e) {
                logger.error("Error converting payload to JSON", e);
                sink.error(new RuntimeException("Error processing payload", e));
              }
            })
        .collectList()
        .map(jsonPayloads -> new BusinessPayloadFluxResponse(jsonPayloads.size(), jsonPayloads));
  }
}
