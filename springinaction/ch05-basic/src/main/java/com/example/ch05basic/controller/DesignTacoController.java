package com.example.ch05basic.controller;

import com.example.ch05basic.model.Ingredient;
import com.example.ch05basic.model.Ingredient.Type;
import com.example.ch05basic.model.Taco;
import com.example.ch05basic.model.TacoOrder;
import com.example.ch05basic.model.TacoUser;
import com.example.ch05basic.repository.IngredientRepository;
import com.example.ch05basic.repository.TacoUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/design")
@SessionAttributes("tacoOrder")
public class DesignTacoController {

    private final IngredientRepository ingredientRepo;
    private final TacoUserRepository tacoUserRepository;

    @Autowired
    public DesignTacoController(
            IngredientRepository ingredientRepo, TacoUserRepository tacoUserRepository) {
        this.ingredientRepo = ingredientRepo;
        this.tacoUserRepository = tacoUserRepository;
    }

    @ModelAttribute
    public void addIngredientsToModel(Model model) {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredientRepo.findAll().forEach(i -> ingredients.add(i));

        Type[] types = Ingredient.Type.values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(),
                    filterByType(ingredients, type));
        }
    }

    @ModelAttribute(name = "tacoOrder")
    public TacoOrder tacoOrder() {
        return new TacoOrder();
    }

    @ModelAttribute(name = "taco")
    public Taco taco() {
        return new Taco();
    }

    @ModelAttribute(name = "tacoUser")
    public TacoUser tacoUser(Principal principal){
        String username = principal.getName();
        TacoUser tacoUser = tacoUserRepository.findByUsername(username);
        return tacoUser;
    }

    @GetMapping
    public String showDesignForm() {
        return "design";
    }

    @PostMapping
    public String processTaco(
            @Valid Taco taco, Errors errors,
            @ModelAttribute TacoOrder tacoOrder) {

        if (errors.hasErrors()) {
            return "design";
        }

        tacoOrder.addTaco(taco);

        return "redirect:/orders/current";
    }

    private Iterable<Ingredient> filterByType(
            List<Ingredient> ingredients, Type type) {
        return ingredients
                .stream()
                .filter(x -> x.getType().equals(type))
                .collect(Collectors.toList());
    }

}
