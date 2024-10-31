package com.example.datasourcereactivesource.repo;

import com.example.datasourcereactivesource.model.BusinessInfo;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface BusinessInfoRepository extends ReactiveCrudRepository<BusinessInfo, Long> {
    Flux<BusinessInfo> findByName(String name);
}