package com.example.datasourceservice.controller;

import com.example.datasourceservice.entity.BusinessInfo;
import com.example.datasourceservice.service.BusinessInfoR2dbcService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/webflux/business-info")
public class BusinessInfoWebFluxController {

  private final BusinessInfoR2dbcService businessInfoService;

  public BusinessInfoWebFluxController(BusinessInfoR2dbcService businessInfoService) {
    this.businessInfoService = businessInfoService;
  }

  @GetMapping("/name/{name}")
  public Flux<BusinessInfo> getBusinessInfoByName(@PathVariable String name) {
    return businessInfoService
        .findAllByBusinessName(name)
        .switchIfEmpty(
            Flux.error(
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No businesses found with name: " + name)));
  }

  @Operation(summary = "Upload a CSV file with business information")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Successfully uploaded and processed the file",
                  content = {@Content(mediaType = "application/json", schema = @Schema(implementation = BusinessInfo.class))}),
          @ApiResponse(responseCode = "400", description = "Invalid file supplied", content = @Content)
  })
  @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public Flux<BusinessInfo> uploadCsv(@RequestPart("file") FilePart file) {
    return businessInfoService.saveBusinessInfoFromCsv(file);
  }
}
