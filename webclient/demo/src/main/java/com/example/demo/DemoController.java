package com.example.demo;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    @PostMapping("/demo/postItem")
    public ResponseEntity<String> postItem(@RequestBody Item item) {
        HttpHeaders headers = new HttpHeaders();
        System.out.println(item);

        return new ResponseEntity<>("good", HttpStatus.OK);
    }
}
