package com.example.ch11paymentsopenfeign.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.example.ch11paymentsopenfeign.proxy")
public class ProjectConfig {
}
