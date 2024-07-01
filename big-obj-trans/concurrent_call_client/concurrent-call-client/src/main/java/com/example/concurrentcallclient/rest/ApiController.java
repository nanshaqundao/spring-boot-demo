package com.example.concurrentcallclient.rest;

import com.example.concurrentcallclient.model.GranularResponse;
import com.example.concurrentcallclient.service.ApiService;
import com.example.concurrentcallclient.service.GranularApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {
    private final ApiService apiService;
    private final GranularApiService granularApiService;

    public ApiController(ApiService apiService, GranularApiService granularApiService) {
        this.apiService = apiService;
        this.granularApiService = granularApiService;
    }

    @GetMapping("/fetch")
    public Mono<String> fetchData() {
        int pageSize = 50;
        int totalPages = 15;
        return apiService.fetchData(totalPages, pageSize);
    }

    @GetMapping("/fetch-granular")
    public Mono<ResponseEntity<List<GranularResponse>>> fetchDataGranular() {
        int pageSize = 50;
        int totalPages = 15;
        return granularApiService.fetchDataGranular(totalPages, pageSize);
    }
}