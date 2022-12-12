package com.example.demo;

public class Item {
    private String name;
    private String location;

    public Item() {

    }

    public Item(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
