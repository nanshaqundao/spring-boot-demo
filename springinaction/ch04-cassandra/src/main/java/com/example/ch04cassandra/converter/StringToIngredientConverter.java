package com.example.ch04cassandra.converter;

import com.example.ch04cassandra.model.Ingredient;
import com.example.ch04cassandra.model.IngredientUDT;
import com.example.ch04cassandra.repository.IngredientRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class StringToIngredientConverter implements Converter<String, IngredientUDT> {

    private IngredientRepository ingredientRepository;

    public StringToIngredientConverter(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public IngredientUDT convert(String id) {
        Optional<Ingredient> ingredient = ingredientRepository.findById(id);
        if (ingredient.isEmpty()) {
            return null;
        }

        return ingredient
                .map(i -> new IngredientUDT(i.getName(), i.getType()))
                .get();
    }
}
