package com.example.datasourceservice.repository;

import com.example.datasourceservice.entity.BusinessOrderPayload;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BusinessOrderPayloadJdbcRepository
    extends JpaRepository<BusinessOrderPayload, Long> {
  List<BusinessOrderPayload> findByName(String name);
}
