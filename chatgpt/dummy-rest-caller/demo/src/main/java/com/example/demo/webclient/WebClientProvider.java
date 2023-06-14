package com.example.demo.webclient;

import com.example.demo.config.WebClientConfig;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
public class WebClientProvider {

    private final WebClientConfig webClientConfig;

    public WebClientProvider(WebClientConfig webClientConfig) {
        this.webClientConfig = webClientConfig;
    }

    @Bean(name = "simpleWebClient")
    public WebClient getSimpleWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8080")
                .build();
    }



    @Bean(name = "webClientWithTimeout")
    public WebClient getWebClientWithTimeout() {
        return getWebClient(webClientConfig.getBaseUrl(),
                webClientConfig.getConnectionTimeoutInSeconds(),
                webClientConfig.getReadTimeoutInSeconds(),
                webClientConfig.getWriteTimeoutInSeconds(),
                webClientConfig.getResponseTimeoutInSeconds());
    }

    private WebClient getWebClient(String baseUrl, int connectionTimeoutInSeconds, int readTimeoutInSeconds, int writeTimeoutInSeconds, int responseTimeoutInSeconds) {

        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(responseTimeoutInSeconds))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeoutInSeconds * 1000)
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(readTimeoutInSeconds, TimeUnit.SECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(writeTimeoutInSeconds, TimeUnit.SECONDS)));

        return WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader(ACCEPT, APPLICATION_JSON_VALUE)
                .build();

    }
}
