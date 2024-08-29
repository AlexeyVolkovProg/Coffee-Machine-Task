package org.example.coffeemachinecore.service;

import org.example.coffeemachinecore.exceptions.DuplicateNameException;
import org.example.coffeemachinecore.exceptions.IngredientNotFoundException;
import org.example.coffeemachinecore.exceptions.InsufficientIngredientException;
import org.example.coffeemachinecore.exceptions.RecipeNotFoundException;
import org.example.coffeemachinecore.model.BeverageStatistics;
import org.example.coffeemachinecore.model.Ingredient;
import org.example.coffeemachinecore.model.Recipe;
import org.example.coffeemachinecore.repository.BeverageStatisticsRepository;
import org.example.coffeemachinecore.repository.IngredientRepository;
import org.example.coffeemachinecore.repository.RecipeRepository;
import org.example.dto.request.AddIngredientRequest;
import org.example.dto.request.AddReceiptRequest;
import org.example.dto.request.IngredientQuantityDto;
import org.example.dto.response.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CoffeeMachineService {
    private final IngredientRepository ingredientRepository;

    private final RecipeRepository recipeRepository;

    private final BeverageStatisticsRepository beverageStatisticsRepository;


    public CoffeeMachineService(IngredientRepository ingredientRepository, RecipeRepository recipeRepository, BeverageStatisticsRepository beverageStatisticsRepository) {
        this.ingredientRepository = ingredientRepository;
        this.recipeRepository = recipeRepository;
        this.beverageStatisticsRepository = beverageStatisticsRepository;
    }

    /**
     * Метод приготовления напитка
     */
    public InfoDTO prepareDrink(String drinkName) {
        Recipe recipe = recipeRepository.findByName(drinkName);
        if (recipe == null) {
            throw new RecipeNotFoundException(drinkName);
        }

        Map<String, Integer> recipeIngredients = recipe.getIngredients(); // достаем информацию о нужных ингредиентах и их кол-ве
        for (Map.Entry<String, Integer> entry : recipeIngredients.entrySet()) {
            String ingredientName = entry.getKey();
            int requiredQuantity = entry.getValue();

            Ingredient ingredient = ingredientRepository.findByName(ingredientName);

            if (ingredient == null ) { // проверка на наличие ингредиентов и их нужного кол-ва
               throw new IngredientNotFoundException(ingredientName);
            }

            if(ingredient.getQuantity() < requiredQuantity){
                throw new InsufficientIngredientException(ingredientName);
            }

            updateStatistics(recipe); // обновляем статистику заказов по данному рецепту
            ingredient.setQuantity(ingredient.getQuantity() - requiredQuantity); // обновляем кол-во ингредиентов с учетом готовки напитка
            ingredientRepository.save(ingredient);
        }
        return new InfoDTO("Successfully prepared " + drinkName, true);
    }


    /**
     * Обычно из машины достается предыдущий кейс с ингредиентом и вставляется новый
     */
    public InfoDTO updateIngredientQuantity(AddIngredientRequest ingredient) {
        Ingredient existingIngredient = ingredientRepository.findByName(ingredient.getName());
        if (existingIngredient == null) {
            throw new IngredientNotFoundException(ingredient.getName());
        }
        existingIngredient.setQuantity(ingredient.getQuantity());
        ingredientRepository.save(existingIngredient);
        return new InfoDTO("Ingredient quantity updated", true);
    }


    /**
     * Метод добавление рецепта
     */
    public ReceiptResponse addRecipe(AddReceiptRequest recipeDto) {
        if (recipeRepository.findByName(recipeDto.getName()) != null) {
            throw new DuplicateNameException("Рецепт с таким именем уже существует: " + recipeDto.getName());
        }

        List<IngredientQuantityDto> ingredients = recipeDto.getIngredients();
        for (IngredientQuantityDto dto : ingredients) {
            Ingredient ingredient = ingredientRepository.findByName(dto.getName());
            if (ingredient == null) {
                throw new IngredientNotFoundException(dto.getName());
            }
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
            throw new DuplicateNameException("Ингредиент с таким именем уже существует: " + ingredientDto.getName());
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

    public InfoDTO getMostPopularBeverage() {
        BeverageStatistics stats = beverageStatisticsRepository.findTopByOrderByCountDesc();
        if (stats == null) {
            return new InfoDTO("No statistics available yet", false);
        }

        return new InfoDTO("Most popular beverage is " + stats.getBeverage().getName() + " with " + stats.getCount() + " orders", true);
    }


    /**
     * Обновление статистики напитка
     */
    private void updateStatistics(Recipe recipe) {
        BeverageStatistics stats = beverageStatisticsRepository.findByBeverage(recipe);
        if (stats == null) {
            stats = new BeverageStatistics();
            stats.setBeverage(recipe);
            stats.setCount(1);
        } else {
            stats.setCount(stats.getCount() + 1);
        }
        beverageStatisticsRepository.save(stats);
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
