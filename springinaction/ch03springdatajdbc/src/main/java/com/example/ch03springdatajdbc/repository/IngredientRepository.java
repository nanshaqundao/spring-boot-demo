package com.example.ch03springdatajdbc.repository;


import com.example.ch03springdatajdbc.model.Ingredient;
import org.springframework.data.repository.CrudRepository;


public interface IngredientRepository  extends CrudRepository<Ingredient, String> {

}
