package com.example.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "webclient")
public class WebClientConfig {

    private String baseUrl;
    private int connectionTimeoutInSeconds;
    private int readTimeoutInSeconds;
    private int writeTimeoutInSeconds;
    private int responseTimeoutInSeconds;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public int getConnectionTimeoutInSeconds() {
        return connectionTimeoutInSeconds;
    }

    public void setConnectionTimeoutInSeconds(int connectionTimeoutInSeconds) {
        this.connectionTimeoutInSeconds = connectionTimeoutInSeconds;
    }

    public int getReadTimeoutInSeconds() {
        return readTimeoutInSeconds;
    }

    public void setReadTimeoutInSeconds(int readTimeoutInSeconds) {
        this.readTimeoutInSeconds = readTimeoutInSeconds;
    }

    public int getWriteTimeoutInSeconds() {
        return writeTimeoutInSeconds;
    }

    public void setWriteTimeoutInSeconds(int writeTimeoutInSeconds) {
        this.writeTimeoutInSeconds = writeTimeoutInSeconds;
    }

    public int getResponseTimeoutInSeconds() {
        return responseTimeoutInSeconds;
    }

    public void setResponseTimeoutInSeconds(int responseTimeoutInSeconds) {
        this.responseTimeoutInSeconds = responseTimeoutInSeconds;
    }
}
