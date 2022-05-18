package com.app.model;

public class Parrot {
    private String name;

    public Parrot() {
        System.out.println("Blank Parrot Instance Created");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        System.out.println("Parrot Instance setName with : " + name);
    }
}
