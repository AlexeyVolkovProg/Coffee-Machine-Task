package org.example.coffeemachinecore.service;

import org.example.coffeemachinecore.model.Ingredient;
import org.example.coffeemachinecore.model.Recipe;
import org.example.dto.request.AddIngredientRequestDTO;
import org.example.dto.request.AddReceiptRequestDTO;
import org.example.dto.request.IngredientQuantityDTO;
import org.example.dto.response.IngredientResponseDTO;
import org.example.dto.response.ReceiptResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DtoTransformerService {

    public Recipe toEntity(AddReceiptRequestDTO recipeDto) {
        Recipe recipe = new Recipe();
        recipe.setName(recipeDto.getName());
        recipe.setIngredients(recipeDto.getIngredients().stream()
                .collect(Collectors.toMap(
                        IngredientQuantityDTO::getName,
                        IngredientQuantityDTO::getQuantity
                )));
        return recipe;
    }


    public Ingredient toEntity(AddIngredientRequestDTO ingredientDto) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(ingredientDto.getName());
        ingredient.setQuantity(ingredientDto.getQuantity());
        return ingredient;
    }


    public ReceiptResponseDTO toDto(Recipe recipe) {
        List<IngredientQuantityDTO> ingredients = recipe.getIngredients().entrySet().stream()
                .map(entry -> new IngredientQuantityDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        return new ReceiptResponseDTO(recipe.getName(), ingredients);
    }


    public IngredientResponseDTO toDto(Ingredient ingredient) {
        return new IngredientResponseDTO(ingredient.getName(), ingredient.getQuantity());
    }
}
