package com.example.ch03jpa.repository;

import com.example.ch03jpa.model.Ingredient;
import org.springframework.data.repository.CrudRepository;

public interface IngredientRepository extends CrudRepository<Ingredient, String> {
}
