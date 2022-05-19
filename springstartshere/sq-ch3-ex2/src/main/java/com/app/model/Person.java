package com.app.model;

public class Person {
    private String name;
    private Parrot parrot;

    public Person() {
        System.out.println("Creating new Person instance");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Parrot getParrot() {
        return parrot;
    }

    public void setParrot(Parrot parrot) {
        this.parrot = parrot;
    }
}
