package com.example.datasourceservice.controller;

import com.example.datasourceservice.entity.BusinessInfo;
import com.example.datasourceservice.service.BusinessInfoJdbcService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
}
