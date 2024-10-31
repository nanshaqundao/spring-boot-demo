package com.example.datasourcereactivesource.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("business_info")
public class BusinessInfo {

    @Id
    private Long id;

    private String name;

    private String description;

    public BusinessInfo() {}

    public BusinessInfo(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getters and setters omitted for brevity

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}