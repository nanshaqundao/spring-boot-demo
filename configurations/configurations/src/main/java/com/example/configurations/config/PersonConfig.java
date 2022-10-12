package com.example.configurations.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "person")
@Data
public class PersonConfig {
    private Map<String, SinglePerson> configs;
}
