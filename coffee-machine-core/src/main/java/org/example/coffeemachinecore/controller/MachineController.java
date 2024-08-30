package org.example.coffeemachinecore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.example.api.CoffeeMachineAPI;
import org.example.coffeemachinecore.service.CoffeeMachineService;
import org.example.dto.error.ApiErrorResponseDTO;
import org.example.dto.request.AddIngredientRequestDTO;
import org.example.dto.request.AddReceiptRequestDTO;
import org.example.dto.response.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class MachineController implements CoffeeMachineAPI {
    private final CoffeeMachineService coffeeMachineService;


    public MachineController(CoffeeMachineService coffeeMachineService) {
        this.coffeeMachineService = coffeeMachineService;
    }

    @Override
    @Operation(summary = "Add a new recipe", description = "Creates and saves a new recipe in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe successfully added",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReceiptResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Recipe with the given name already exists",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @PostMapping("/recipe")
    public ReceiptResponseDTO addRecipe(@RequestBody AddReceiptRequestDTO recipeDto) {
        return coffeeMachineService.addRecipe(recipeDto);
    }

    @Override
    @Operation(summary = "Add a new ingredient", description = "Creates and saves a new ingredient in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ingredient successfully added",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IngredientResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Ingredient with the given name already exists",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @PostMapping("/ingredient")
    public IngredientResponseDTO addIngredient(@RequestBody AddIngredientRequestDTO ingredientDto) {
        return coffeeMachineService.addIngredient(ingredientDto);
    }

    @Override
    @Operation(summary = "Get all recipes", description = "Retrieves a list of all recipes")
    @ApiResponse(responseCode = "200", description = "List of all recipes retrieved successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ReceiptListResponseDTO.class)))
    @GetMapping("/recipes")
    public ReceiptListResponseDTO getAllRecipes() {
        return coffeeMachineService.getAllRecipes();
    }


    @Override
    @Operation(summary = "Get all ingredients", description = "Retrieves a list of all ingredients")
    @ApiResponse(responseCode = "200", description = "List of all ingredients retrieved successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = IngredientListResponseDTO.class)))
    @GetMapping("/ingredients")
    public IngredientListResponseDTO getAllIngredients() {
        return coffeeMachineService.getAllIngredients();
    }


    @Override
    @Operation(summary = "Prepare a drink", description = "Prepares a drink according to the specified recipe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Drink successfully prepared",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InfoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Recipe not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Ingredient not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Insufficient ingredients",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @GetMapping("/prepareDrink")
    public InfoDTO prepareDrink(@RequestParam String drinkName) {
        return coffeeMachineService.prepareDrink(drinkName);
    }

    @Override
    @Operation(summary = "Update ingredient quantity", description = "Updates the quantity of an existing ingredient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ingredient quantity updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InfoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Ingredient not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @PatchMapping("/ingredient")
    public InfoDTO updateIngredientQuantity(@RequestBody AddIngredientRequestDTO ingredient) {
        return coffeeMachineService.updateIngredientQuantity(ingredient);
    }

    @Operation(summary = "Get the most popular beverage", description = "Retrieves the most popular beverage based on order statistics")
    @ApiResponse(responseCode = "200", description = "Most popular beverage retrieved successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = InfoDTO.class)))
    @Override
    @GetMapping("/beverage/most-popular")
    public InfoDTO getMostPopularBeverage() {
        return coffeeMachineService.getMostPopularBeverage();
    }
}
