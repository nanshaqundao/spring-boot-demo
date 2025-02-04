package com.example.fluxdataconsumerservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MyObj {
    private Long id;
    private String name;
    private String content;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}