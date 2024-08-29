package org.example.coffeemachinecore.controller;

import org.example.coffeemachinecore.model.Ingredient;
import org.example.coffeemachinecore.service.CoffeeMachineService;
import org.example.dto.request.AddIngredientRequest;
import org.example.dto.request.AddReceiptRequest;
import org.example.dto.response.*;
import org.springframework.web.bind.annotation.*;

@RestController
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

    @GetMapping("/recipes")
    public ReceiptListResponse getAllRecipes() {
        return coffeeMachineService.getAllRecipes();
    }


    @GetMapping("/ingredients")
    public IngredientListResponse getAllIngredients() {
        return coffeeMachineService.getAllIngredients();
    }



    @PostMapping("/prepareDrink")
    public InfoDTO prepareDrink(@RequestParam String drinkName) {
        return coffeeMachineService.prepareDrink(drinkName);
    }


    @PatchMapping("/updateIngredient")
    public InfoDTO updateIngredientQuantity(@RequestBody AddIngredientRequest ingredient) {
        return coffeeMachineService.updateIngredientQuantity(ingredient);
    }

    @GetMapping("/mostPopularBeverage")
    public InfoDTO getMostPopularBeverage() {
        return coffeeMachineService.getMostPopularBeverage();
    }
}
