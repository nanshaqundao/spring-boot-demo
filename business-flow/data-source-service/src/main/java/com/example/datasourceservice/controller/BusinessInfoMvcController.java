package com.example.datasourceservice.controller;

import com.example.datasourceservice.entity.BusinessInfo;

import com.example.datasourceservice.protobuf.BusinessPayload;
import com.example.datasourceservice.service.BusinessInfoJdbcService;
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

import java.util.List;

@RestController
@RequestMapping(
    value = "/api/mvc/business-info",
    produces = {"application/x-protobuf", "application/json"})
@Tag(name = "Business Info Controller", description = "Business information operations")
public class BusinessInfoMvcController {
  private static final Logger logger = LoggerFactory.getLogger(BusinessInfoMvcController.class);

  private final BusinessInfoJdbcService businessInfoService;

  public BusinessInfoMvcController(BusinessInfoJdbcService businessInfoService) {
    this.businessInfoService = businessInfoService;
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

  @GetMapping("/reactive-with-buffer/name/{name}")
  public Flux<BusinessInfo> getBusinessInfoByNameReactiveWithBuffer(
      @PathVariable String name, @RequestParam(defaultValue = "5") int bufferSize) {
    return businessInfoService.findAllByNameReactiveWithBuffer(name, bufferSize);
  }

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

  // Streaming endpoint
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
    return Flux.range(1, 5)
        .map(
            i -> {
              BusinessPayload payload =
                  BusinessPayload.newBuilder()
                      .setName("John Doe " + i)
                      .setOrder(String.valueOf(i * 100))
                      .setContent("content " + i) // In real case, this would be your 10K content
                      .build();
              logger.debug("Generated payload {} with size {}", i, payload.getSerializedSize());
              return payload.toByteArray();
            });
  }
}
