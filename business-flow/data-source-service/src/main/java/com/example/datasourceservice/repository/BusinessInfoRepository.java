package com.example.datasourceservice.repository;

import com.example.datasourceservice.entity.BusinessInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessInfoRepository extends JpaRepository<BusinessInfo, Long> {
}