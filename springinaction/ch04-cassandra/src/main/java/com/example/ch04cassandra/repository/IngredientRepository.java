package com.example.ch04cassandra.repository;

import com.example.ch04cassandra.model.Ingredient;
import org.springframework.data.repository.CrudRepository;

public interface IngredientRepository extends CrudRepository<Ingredient, String> {
}
