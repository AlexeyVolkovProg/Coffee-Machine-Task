package org.example.coffeemachinecore.service;

import org.example.coffeemachinecore.model.Ingredient;
import org.example.coffeemachinecore.model.Recipe;
import org.example.coffeemachinecore.repository.IngredientRepository;
import org.example.coffeemachinecore.repository.RecipeRepository;
import org.example.dto.request.AddIngredientRequest;
import org.example.dto.request.AddReceiptRequest;
import org.example.dto.request.IngredientQuantityDto;
import org.example.dto.response.IngredientListResponse;
import org.example.dto.response.IngredientResponse;
import org.example.dto.response.ReceiptListResponse;
import org.example.dto.response.ReceiptResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Service
public class CoffeeMachineService {
    private final IngredientRepository ingredientRepository;

    private final RecipeRepository recipeRepository;


    public CoffeeMachineService(IngredientRepository ingredientRepository, RecipeRepository recipeRepository) {
        this.ingredientRepository = ingredientRepository;
        this.recipeRepository = recipeRepository;
    }

    /**
     * Метод приготовления напитка
     */
    public String prepareDrink(String drinkName) {
        Recipe recipe = recipeRepository.findByName(drinkName);
        if (recipe == null) {
            throw new RuntimeException("Recipe not found");
        }

        Map<String, Integer> recipeIngredients = recipe.getIngredients(); // достаем информацию о нужных ингредиентах и их кол-ве
        for (Map.Entry<String, Integer> entry : recipeIngredients.entrySet()) {
            String ingredientName = entry.getKey();
            int requiredQuantity = entry.getValue();

            Ingredient ingredient = ingredientRepository.findByName(ingredientName);

            if (ingredient == null || ingredient.getQuantity() < requiredQuantity) { // проверка на наличие ингредиентов и их нужного кол-ва
                return "Not enough " + (ingredient != null ? ingredientName : "unknown ingredient");
            }

            ingredient.setQuantity(ingredient.getQuantity() - requiredQuantity); // обновляем кол-во ингредиентов с учетом готовки напитка
            ingredientRepository.save(ingredient);
        }
        return "Success prepare " + drinkName;
    }


    /**
     * Обычно из машины достается предыдущий кейс с ингредиентом и вставляется новый
     */
    public String updateIngredientQuantity(Ingredient ingredient) {
        Ingredient existingIngredient = ingredientRepository.findByName(ingredient.getName());
        if (existingIngredient == null) {
            return "Ingredient not found";
        }
        existingIngredient.setQuantity(ingredient.getQuantity());
        ingredientRepository.save(existingIngredient);
        return "Ingredient quantity updated";
    }


    /**
     * Метод добавление рецепта
     */
    public ReceiptResponse addRecipe(AddReceiptRequest recipeDto) {
        if (recipeRepository.findByName(recipeDto.getName()) != null) {
            throw new IllegalArgumentException("Рецепт с таким именем уже существует: " + recipeDto.getName());
        }

        Recipe recipe = toEntity(recipeDto);
        Recipe savedRecipe = recipeRepository.save(recipe);
        return toDto(savedRecipe);
    }

    /**
     * Добавление ингредиента
     */
    public IngredientResponse addIngredient(AddIngredientRequest ingredientDto) {
        if (ingredientRepository.findByName(ingredientDto.getName()) != null) {
            throw new IllegalArgumentException("Ингредиент с таким именем уже существует: " + ingredientDto.getName());
        }

        Ingredient ingredient = toEntity(ingredientDto);
        Ingredient savedIngredient = ingredientRepository.save(ingredient);
        return toDto(savedIngredient);
    }


    public IngredientListResponse getAllIngredients() {
        List<IngredientResponse> ingredientResponses = ingredientRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return new IngredientListResponse(ingredientResponses, ingredientResponses.size());
    }

    // Новый метод для получения списка всех рецептов с их ингредиентами
    public ReceiptListResponse getAllRecipes() {
        List<ReceiptResponse> receiptResponses = recipeRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return new ReceiptListResponse(receiptResponses, receiptResponses.size());
    }

    // Преобразование RecipeDto в Recipe
    private Recipe toEntity(AddReceiptRequest recipeDto) {
        Recipe recipe = new Recipe();
        recipe.setName(recipeDto.getName());
        recipe.setIngredients(recipeDto.getIngredients().stream()
                .collect(Collectors.toMap(
                        IngredientQuantityDto::getName,
                        IngredientQuantityDto::getQuantity
                )));
        return recipe;
    }

    // Преобразование AddIngredientRequest в Ingredient
    private Ingredient toEntity(AddIngredientRequest ingredientDto) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(ingredientDto.getName());
        ingredient.setQuantity(ingredientDto.getQuantity());
        return ingredient;
    }

    // Преобразование Recipe в ReceiptResponse
    private ReceiptResponse toDto(Recipe recipe) {
        List<IngredientQuantityDto> ingredients = recipe.getIngredients().entrySet().stream()
                .map(entry -> new IngredientQuantityDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        return new ReceiptResponse(recipe.getName(), ingredients);
    }

    // Преобразование Ingredient в IngredientResponse
    private IngredientResponse toDto(Ingredient ingredient) {
        return new IngredientResponse(ingredient.getName(), ingredient.getQuantity());
    }
}
