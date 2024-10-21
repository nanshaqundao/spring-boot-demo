package com.example.datasourceservice.service;

import java.util.List;

import com.example.datasourceservice.entity.BusinessInfo;
import com.example.datasourceservice.repository.jpa.BusinessInfoJdbcRepository;
import org.springframework.stereotype.Service;

@Service
public class BusinessInfoJdbcService {

  private final BusinessInfoJdbcRepository repository;

  public BusinessInfoJdbcService(BusinessInfoJdbcRepository repository) {
    this.repository = repository;
  }

  public List<BusinessInfo> findAllByBusinessName(String name) {
    return repository.findByName(name);
  }
}
