package com.example.demo.controller;

import com.example.demo.model.RefResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class MyController {

  @GetMapping("/refData")
  public Mono<ResponseEntity<RefResponse>> getAnnaData(@RequestParam String srcCode) {
    System.out.println("Request received with details: " + srcCode);
    RefResponse refResponse = new RefResponse();
    refResponse.setSrcCode(srcCode);
    refResponse.setAnnaCode("annaCode" + srcCode);
    refResponse.setCrossCode("crossCode" + srcCode);
    refResponse.setId(new Random().nextInt(1000));
    return Mono.just(ResponseEntity.ok(refResponse));
  }

  @GetMapping("/demo")
  public Mono<ResponseEntity<RefResponse>> getDemoData() {
    System.out.println("Request received with details: " + "£");
    RefResponse refResponse = new RefResponse();
    refResponse.setSrcCode("£");
    refResponse.setAnnaCode("annaCode" + "£");
    refResponse.setCrossCode("crossCode" + "£");
    refResponse.setId(new Random().nextInt(1000));
    return Mono.just(ResponseEntity.ok(refResponse));
  }

  @PostMapping("/isin")
  public Mono<ResponseEntity<String>> getIsin(@RequestBody String requestBody) {
    String result = "ISIN-" + generateRandomString(10);
    System.out.println("Request received with details: " + requestBody);
    // return Mono.just(ResponseEntity.ok(result));

    return Mono.just(ResponseEntity.ok().body("Error" + result));
  }

  private String generateRandomString(int length) {
    StringBuilder sb = new StringBuilder();
    String uuid = UUID.randomUUID().toString().replace("-", "");

    // Ensure the string starts with a non-digit character
    char firstChar = (char) (Character.toUpperCase(uuid.charAt(0)) % 26 + 'A');
    sb.append(firstChar);

    // Append the remaining characters from the UUID string
    for (int i = 1; i < length; i++) {
      char randomChar = (char) Character.toUpperCase(uuid.charAt(i) % 26 + 'A');
      sb.append(randomChar);
    }

    return sb.toString();
  }
}
