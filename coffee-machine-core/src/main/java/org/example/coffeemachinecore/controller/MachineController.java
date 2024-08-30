package org.example.coffeemachinecore.controller;

import org.example.api.CoffeeMachineAPI;
import org.example.coffeemachinecore.service.CoffeeMachineService;
import org.example.dto.request.AddIngredientRequest;
import org.example.dto.request.AddReceiptRequest;
import org.example.dto.response.*;
import org.springframework.web.bind.annotation.*;

@RestController
public class MachineController implements CoffeeMachineAPI {
    private final CoffeeMachineService coffeeMachineService;


    public MachineController(CoffeeMachineService coffeeMachineService) {
        this.coffeeMachineService = coffeeMachineService;
    }

    @Override
    @PostMapping("/addRecipe")
    public ReceiptResponse addRecipe(@RequestBody AddReceiptRequest recipeDto) {
        return coffeeMachineService.addRecipe(recipeDto);
    }

    @Override
    @PostMapping("/addIngredient")
    public IngredientResponse addIngredient(@RequestBody AddIngredientRequest ingredientDto) {
        return coffeeMachineService.addIngredient(ingredientDto);
    }

    @Override
    @GetMapping("/recipes")
    public ReceiptListResponse getAllRecipes() {
        return coffeeMachineService.getAllRecipes();
    }


    @Override
    @GetMapping("/ingredients")
    public IngredientListResponse getAllIngredients() {
        return coffeeMachineService.getAllIngredients();
    }



    @Override
    @PostMapping("/prepareDrink")
    public InfoDTO prepareDrink(@RequestParam String drinkName) {
        return coffeeMachineService.prepareDrink(drinkName);
    }


    @PatchMapping("/updateIngredient")
    public InfoDTO updateIngredientQuantity(@RequestBody AddIngredientRequest ingredient) {
        return coffeeMachineService.updateIngredientQuantity(ingredient);
    }

    @Override
    @GetMapping("/mostPopularBeverage")
    public InfoDTO getMostPopularBeverage() {
        return coffeeMachineService.getMostPopularBeverage();
    }
}
