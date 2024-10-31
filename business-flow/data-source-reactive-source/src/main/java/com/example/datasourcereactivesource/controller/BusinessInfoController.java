package com.example.datasourcereactivesource.controller;

import com.example.datasourcereactivesource.model.BusinessInfo;
import com.example.datasourcereactivesource.service.BusinessInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/r2dbc/business-info")
public class BusinessInfoController {

  private final BusinessInfoService businessInfoService;

  public BusinessInfoController(BusinessInfoService businessInfoService) {
    this.businessInfoService = businessInfoService;
  }

  @GetMapping("/name/{name}")
  public Flux<BusinessInfo> getBusinessInfoByName(@PathVariable String name) {
    return businessInfoService
        .findAllByNameReactive(name)
        .switchIfEmpty(
            Mono.error(
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No businesses found with name: " + name)));
  }
}
