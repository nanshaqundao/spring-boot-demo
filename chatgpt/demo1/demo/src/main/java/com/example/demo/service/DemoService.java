package com.example.demo.service;

import com.example.demo.model.Input;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class DemoService {

    private final WebClient webClient;

    public DemoService(WebClient webClient) {
        this.webClient = webClient;
    }

    public String generateResponse(String message) {
//        String payload = "{\"prompt\":\"" + message + "\",\"max_tokens\":60,\"n\":1,\"stop\":\"\n\"}";
        String bodyString = generateBody(message);
        return webClient.post()
//                .body(BodyInserters.fromValue(generateBody(message)))
                .body(Mono.just(bodyString), String.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String generateBody(String message) {
        return "{\n" +
                "  \"model\": \"gpt-3.5-turbo\",\n" +
                "  \"messages\": [{\"role\": \"user\", \"content\": \"" +
                message +
                "\"}]\n" +
                "}\n";
    }

    public Input parseResponse(String result) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(result, Input.class);
    }
}
