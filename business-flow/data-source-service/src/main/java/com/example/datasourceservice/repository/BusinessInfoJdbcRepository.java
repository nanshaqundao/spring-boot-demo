package com.example.datasourceservice.repository;

import com.example.datasourceservice.entity.BusinessInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BusinessInfoJdbcRepository extends JpaRepository<BusinessInfo, Long> {
  List<BusinessInfo> findByName(String name);
}
