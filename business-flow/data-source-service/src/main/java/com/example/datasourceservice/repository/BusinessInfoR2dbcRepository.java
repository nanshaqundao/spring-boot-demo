package com.example.datasourceservice.repository;

import com.example.datasourceservice.entity.BusinessInfo;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface BusinessInfoR2dbcRepository extends R2dbcRepository<BusinessInfo, Long> {
  Flux<BusinessInfo> findByName(String name);
}
