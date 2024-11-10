package com.example.businessclientservice.service;

import com.example.businessclientservice.protobuf.BusinessPayload;
import com.google.protobuf.InvalidProtocolBufferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;


@Service
public class BusinessClientService {
  private static final Logger logger = LoggerFactory.getLogger(BusinessClientService.class);

  private final WebClient webClient;
  private final String dataSourceServiceUrl;

  public BusinessClientService(
          WebClient webClient,
          @Value("${datasource.service.url}") String dataSourceServiceUrl) {
    this.webClient = webClient;
    this.dataSourceServiceUrl = dataSourceServiceUrl;
  }

  public Flux<BusinessPayload> getBusinessPayloadStream() {
    return webClient.get()
            .uri(dataSourceServiceUrl + "/api/mvc/business-info/test-protobuf-flux")
            .accept(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE))
            .retrieve()
            .bodyToFlux(byte[].class)
            .doOnNext(bytes -> {
              // Enhanced logging
              logger.debug("Received bytes length: {}", bytes.length);
              if (bytes.length > 0) {
                logger.debug("First few bytes: {}",
                        Arrays.toString(Arrays.copyOfRange(bytes, 0, Math.min(10, bytes.length))));
                logger.debug("As string: '{}'", new String(bytes));
              }
            })
            .filter(bytes -> bytes.length > 10)  // Filter out SSE formatting bytes
            .map(bytes -> {
              try {
                BusinessPayload payload = BusinessPayload.parseFrom(bytes);
                logger.debug("Successfully parsed payload: name={}, size={}",
                        payload.getName(), payload.getSerializedSize());
                return payload;
              } catch (InvalidProtocolBufferException e) {
                logger.error("Failed to parse protobuf message, bytes length: {}",
                        bytes.length, e);
                return BusinessPayload.getDefaultInstance(); // Return empty payload instead of null
              }
            })
            .filter(payload -> !payload.equals(BusinessPayload.getDefaultInstance())); // Filter out empty payloads
  }
}