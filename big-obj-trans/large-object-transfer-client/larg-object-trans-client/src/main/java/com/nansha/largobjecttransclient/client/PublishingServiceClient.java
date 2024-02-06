package com.nansha.largobjecttransclient.client;

import com.nansha.largobjecttransclient.model.MessageState;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class PublishingServiceClient {

    private final WebClient webClient;

    public PublishingServiceClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080/message").build(); // Adjust the base URL accordingly
    }

    public Mono<List<MessageState>> getMessageStates(int page, int size) {
        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/get")
                        .queryParam("pageNum", page)
                        .queryParam("pageSize", size)
                        .build())
                .retrieve()
                .bodyToFlux(MessageState.class)
                .collectList();
    }
}