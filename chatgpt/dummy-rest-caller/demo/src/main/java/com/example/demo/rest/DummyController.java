package com.example.demo.rest;

import com.example.demo.service.DummyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/dummy")
public class DummyController {
  private final DummyService dummyService;

  public DummyController(DummyService dummyService) {
    this.dummyService = dummyService;
  }

  @GetMapping("/isin")
  public Mono<ResponseEntity<String>> getIsin(@RequestParam String name) {
    return dummyService
        .getIsin(name)
        .map(response -> ResponseEntity.status(response.getStatusCode()).body(response.getBody()))
        .onErrorResume(e -> Mono.just(ResponseEntity.status(500).body("Real Internal Error")));
  }
}
