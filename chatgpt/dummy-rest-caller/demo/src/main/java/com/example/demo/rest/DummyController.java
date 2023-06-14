package com.example.demo.rest;

import com.example.demo.service.DummyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/dummy")
public class DummyController {
    private final DummyService dummyService;

    public DummyController(DummyService dummyService) {
        this.dummyService = dummyService;
    }

    @GetMapping("/a")
    public Mono<ResponseEntity<String>> getIsin() {
        return dummyService.getIsin().map(ResponseEntity::ok);
    }
}
