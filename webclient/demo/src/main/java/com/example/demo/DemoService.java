package com.example.demo;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class DemoService {
    private final WebClient webClient;

    public DemoService(WebClient webClient) {
        this.webClient = webClient;
    }
}
