package com.example.datasourcereactivesource.service;

import com.example.datasourcereactivesource.model.BusinessInfo;
import com.example.datasourcereactivesource.repo.BusinessInfoRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class BusinessInfoService {

    private final BusinessInfoRepository repository;

    public BusinessInfoService(BusinessInfoRepository repository) {
        this.repository = repository;
    }

    public Flux<BusinessInfo> findAllByNameReactive(String name) {
        return repository.findByName(name);
    }
}