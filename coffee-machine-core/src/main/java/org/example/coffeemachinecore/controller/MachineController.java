package org.example.coffeemachinecore.controller;

import org.example.coffeemachinecore.service.CoffeeMachineService;
import org.example.dto.request.AddIngredientRequest;
import org.example.dto.request.AddReceiptRequest;
import org.example.dto.response.IngredientListResponse;
import org.example.dto.response.IngredientResponse;
import org.example.dto.response.ReceiptListResponse;
import org.example.dto.response.ReceiptResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class MachineController {
    private final CoffeeMachineService coffeeMachineService;


    public MachineController(CoffeeMachineService coffeeMachineService) {
        this.coffeeMachineService = coffeeMachineService;
    }


    @PostMapping("/addRecipe")
    public ReceiptResponse addRecipe(@RequestBody AddReceiptRequest recipeDto) {
        return coffeeMachineService.addRecipe(recipeDto);
    }

    @PostMapping("/addIngredient")
    public IngredientResponse addIngredient(@RequestBody AddIngredientRequest ingredientDto) {
        return coffeeMachineService.addIngredient(ingredientDto);
    }

    // Новый метод для получения списка всех рецептов
    @GetMapping("/recipes")
    public ReceiptListResponse getAllRecipes() {
        return coffeeMachineService.getAllRecipes();
    }


    @GetMapping("/ingredients")
    public IngredientListResponse getAllIngredients() {
        return coffeeMachineService.getAllIngredients();
    }

}
