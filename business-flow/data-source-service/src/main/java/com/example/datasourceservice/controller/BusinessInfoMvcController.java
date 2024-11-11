package com.example.datasourceservice.controller;

import com.example.datasourceservice.entity.BusinessInfo;

import com.example.datasourceservice.protobuf.BusinessPayload;
import com.example.datasourceservice.service.BusinessInfoJdbcService;
import com.example.datasourceservice.service.BusinessOrderPayloadJdbcService;
import com.example.datasourceservice.util.PayloadContentGenerator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping(
    value = "/api/mvc/business-info",
    produces = {"application/x-protobuf", "application/json"})
@Tag(name = "Business Info Controller", description = "Business information operations")
public class BusinessInfoMvcController {
  private static final Logger logger = LoggerFactory.getLogger(BusinessInfoMvcController.class);

  private final BusinessInfoJdbcService businessInfoService;
  private final BusinessOrderPayloadJdbcService businessOrderPayloadService;

  public BusinessInfoMvcController(
      BusinessInfoJdbcService businessInfoService,
      BusinessOrderPayloadJdbcService businessOrderPayloadService) {
    this.businessInfoService = businessInfoService;
    this.businessOrderPayloadService = businessOrderPayloadService;
  }

  @GetMapping("/name/{name}")
  public ResponseEntity<List<BusinessInfo>> getBusinessInfoByName(@PathVariable String name) {
    List<BusinessInfo> businessInfos = businessInfoService.findAllByBusinessName(name);
    if (businessInfos.isEmpty()) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, "No businesses found with name: " + name);
    }
    return ResponseEntity.ok(businessInfos);
  }

  @GetMapping("/reactive/name/{name}")
  public Flux<BusinessInfo> getBusinessInfoByNameReactive(@PathVariable String name) {
    return businessInfoService.findAllByNameReactive(name);
  }

  //  @GetMapping("/reactive-with-buffer/name/{name}")
  //  public Flux<BusinessInfo> getBusinessInfoByNameReactiveWithBuffer(
  //      @PathVariable String name, @RequestParam(defaultValue = "5") int bufferSize) {
  //    return businessInfoService.findAllByNameReactiveWithBuffer(name, bufferSize);
  //  }

  @Operation(
      summary = "Get business payload",
      description = "Returns business payload in JSON or Protobuf format based on Accept header",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful response",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema =
                      @Schema(
                          type = "object",
                          example =
                              """
                                {
                                    "name": "John Doe",
                                    "order": "123",
                                    "content": "abc"
                                }
                                """)),
              @Content(
                  mediaType = "application/x-protobuf",
                  schema = @Schema(type = "string", format = "binary"))
            })
      })
  @GetMapping(value = "/test-protobuf")
  public ResponseEntity<BusinessPayload> getBusinessPayload() {
    var payload =
        BusinessPayload.newBuilder().setName("John Doe").setOrder("123").setContent("abc").build();
    return ResponseEntity.ok(payload);
  }

  @GetMapping(value = "/test-protobuf-flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  @ResponseBody
  @Operation(
      summary = "Get business payload flux as byte stream",
      description = "Returns business payload flux as Server-Sent Events",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Stream of serialized protobuf messages",
            content =
                @Content(
                    mediaType = MediaType.TEXT_EVENT_STREAM_VALUE,
                    schema =
                        @Schema(
                            type = "string",
                            format = "binary",
                            description = "Stream of serialized BusinessPayload messages")))
      })
  public Flux<byte[]> getBusinessPayloadFlux() {
    return businessOrderPayloadService
        .findAllByNameReactiveAsProto("TimeMachine")
        .delayElements(Duration.ofNanos(500))
        .map(
            payload -> {
              logger.info(
                  "Converting payload {} with size {}",
                  payload.getName(),
                  payload.getSerializedSize());
              return payload.toByteArray();
            })
        .doOnSubscribe(s -> logger.info("Client subscribed to flux"))
        .doOnComplete(() -> logger.info("Completed streaming messages"))
        .doOnCancel(() -> logger.info("Client cancelled subscription"))
        .onErrorResume(
            error -> {
              logger.error("Error in streaming payloads: {}", error.getMessage());
              return Flux.empty();
            });
  }

  @GetMapping(value = "/test-protobuf-flux-old", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  @ResponseBody
  @Operation(
      summary = "Get business payload flux as byte stream",
      description = "Returns business payload flux as Server-Sent Events",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Stream of serialized protobuf messages",
            content =
                @Content(
                    mediaType = MediaType.TEXT_EVENT_STREAM_VALUE,
                    schema =
                        @Schema(
                            type = "string",
                            format = "binary",
                            description = "Stream of serialized BusinessPayload messages")))
      })
  public Flux<byte[]> getBusinessPayloadFluxOld() {
    return Flux.range(1, 10000)
        // Add small delay to prevent overwhelming the client
        .delayElements(Duration.ofNanos(500))
        // Add basic backpressure handling
        //        .onBackpressureBuffer(1000)
        .map(
            i -> {
              BusinessPayload payload =
                  BusinessPayload.newBuilder()
                      .setName("John Doe " + i)
                      .setOrder(String.valueOf(i * 100))
                      .setContent(PayloadContentGenerator.generateRandomContent())
                      .build();

              logger.info("Generated payload {} with size {}", i, payload.getSerializedSize());
              return payload.toByteArray();
            })
        // Add basic error handling
        .onErrorResume(
            error -> {
              logger.error("Error in emitting payloads: {}", error.getMessage());
              return Flux.empty();
            })
        .doOnComplete(() -> logger.info("Completed emitting all messages"));
  }
}
