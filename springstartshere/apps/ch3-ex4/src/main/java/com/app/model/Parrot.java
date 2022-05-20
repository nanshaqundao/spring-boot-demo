package com.app.model;

import org.springframework.stereotype.Component;

@Component
public class Parrot {
    private String name = "ekko";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        System.out.println("Parrot Instance setName with : " + name);
    }
}
