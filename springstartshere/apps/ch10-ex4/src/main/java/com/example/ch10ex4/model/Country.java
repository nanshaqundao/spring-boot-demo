package com.example.ch10ex4.model;

import lombok.Data;

@Data
public class Country {
    private String name;
    private Integer population;

    public static Country of(String name, Integer population) {
        Country country = new Country();
        country.setName(name);
        country.setPopulation(population);
        return country;
    }
}
