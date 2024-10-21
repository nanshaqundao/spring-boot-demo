package com.example.datasourceservice.repository;

import com.example.datasourceservice.entity.BusinessInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BusinessInfoRepository extends JpaRepository<BusinessInfo, Long> {

  // Method to find all businesses by name
  List<BusinessInfo> findAllByName(String name);

  List<BusinessInfo> findByName(String name);
}
