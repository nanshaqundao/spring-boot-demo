package com.nanshaqundao.jdbctemplatedemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
    // use context.initializer.classes from properties to load the extra property
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
