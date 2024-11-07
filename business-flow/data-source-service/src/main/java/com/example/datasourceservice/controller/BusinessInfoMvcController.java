package com.example.datasourceservice.controller;

import com.example.datasourceservice.entity.BusinessInfo;
import com.example.datasourceservice.service.BusinessInfoJdbcService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/api/mvc/business-info")
public class BusinessInfoMvcController {

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
}
