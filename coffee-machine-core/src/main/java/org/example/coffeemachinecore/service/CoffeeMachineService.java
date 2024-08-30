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
import org.example.dto.request.AddIngredientRequestDTO;
import org.example.dto.request.AddReceiptRequestDTO;
import org.example.dto.request.IngredientQuantityDTO;
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
    public InfoDTO updateIngredientQuantity(AddIngredientRequestDTO ingredient) {
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
    public ReceiptResponseDTO addRecipe(AddReceiptRequestDTO recipeDto) {
        if (recipeRepository.findByName(recipeDto.getName()) != null) {
            throw new DuplicateNameException("Рецепт с таким именем уже существует: " + recipeDto.getName());
        }

        List<IngredientQuantityDTO> ingredients = recipeDto.getIngredients();
        for (IngredientQuantityDTO dto : ingredients) {
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
    public IngredientResponseDTO addIngredient(AddIngredientRequestDTO ingredientDto) {
        if (ingredientRepository.findByName(ingredientDto.getName()) != null) {
            throw new DuplicateNameException("Ингредиент с таким именем уже существует: " + ingredientDto.getName());
        }

        Ingredient ingredient = toEntity(ingredientDto);
        Ingredient savedIngredient = ingredientRepository.save(ingredient);
        return toDto(savedIngredient);
    }


    public IngredientListResponseDTO getAllIngredients() {
        List<IngredientResponseDTO> ingredientResponsDTOS = ingredientRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return new IngredientListResponseDTO(ingredientResponsDTOS, ingredientResponsDTOS.size());
    }

    // Новый метод для получения списка всех рецептов с их ингредиентами
    public ReceiptListResponseDTO getAllRecipes() {
        List<ReceiptResponseDTO> receiptResponsDTOS = recipeRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return new ReceiptListResponseDTO(receiptResponsDTOS, receiptResponsDTOS.size());
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
    private Recipe toEntity(AddReceiptRequestDTO recipeDto) {
        Recipe recipe = new Recipe();
        recipe.setName(recipeDto.getName());
        recipe.setIngredients(recipeDto.getIngredients().stream()
                .collect(Collectors.toMap(
                        IngredientQuantityDTO::getName,
                        IngredientQuantityDTO::getQuantity
                )));
        return recipe;
    }

    // Преобразование AddIngredientRequest в Ingredient
    private Ingredient toEntity(AddIngredientRequestDTO ingredientDto) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(ingredientDto.getName());
        ingredient.setQuantity(ingredientDto.getQuantity());
        return ingredient;
    }

    // Преобразование Recipe в ReceiptResponse
    private ReceiptResponseDTO toDto(Recipe recipe) {
        List<IngredientQuantityDTO> ingredients = recipe.getIngredients().entrySet().stream()
                .map(entry -> new IngredientQuantityDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        return new ReceiptResponseDTO(recipe.getName(), ingredients);
    }

    // Преобразование Ingredient в IngredientResponse
    private IngredientResponseDTO toDto(Ingredient ingredient) {
        return new IngredientResponseDTO(ingredient.getName(), ingredient.getQuantity());
    }
}
