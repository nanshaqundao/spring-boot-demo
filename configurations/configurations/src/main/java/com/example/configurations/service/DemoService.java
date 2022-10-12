package com.example.configurations.service;

import com.example.configurations.config.PersonConfig;
import org.springframework.stereotype.Service;

@Service
public class DemoService {
    private final PersonConfig personConfig;

    public DemoService(PersonConfig personConfig) {
        this.personConfig = personConfig;

        System.out.println(personConfig);;
    }
}
