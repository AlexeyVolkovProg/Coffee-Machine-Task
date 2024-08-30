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
import java.util.Optional;
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
        Recipe recipe = recipeRepository.findByName(drinkName)
                .orElseThrow(() -> new RecipeNotFoundException(drinkName));

        Map<String, Integer> recipeIngredients = recipe.getIngredients(); // достаем информацию о нужных ингредиентах и их кол-ве
        for (Map.Entry<String, Integer> entry : recipeIngredients.entrySet()) {
            String ingredientName = entry.getKey();
            int requiredQuantity = entry.getValue();

            Ingredient ingredient = ingredientRepository.findByName(ingredientName)
                    .orElseThrow(() -> new IngredientNotFoundException(ingredientName));

            if (ingredient.getQuantity() < requiredQuantity) {
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
        Ingredient existingIngredient = ingredientRepository.findByName(ingredient.getName())
                .orElseThrow(() -> new IngredientNotFoundException(ingredient.getName()));

        existingIngredient.setQuantity(ingredient.getQuantity());
        ingredientRepository.save(existingIngredient);
        return new InfoDTO("Ingredient quantity updated", true);
    }

    /**
     * Метод добавление рецепта
     */
    public ReceiptResponseDTO addRecipe(AddReceiptRequestDTO recipeDto) {
        if (recipeRepository.findByName(recipeDto.getName()).isPresent()) {
            throw new DuplicateNameException("Рецепт с таким именем уже существует: " + recipeDto.getName());
        }

        List<IngredientQuantityDTO> ingredients = recipeDto.getIngredients();
        for (IngredientQuantityDTO dto : ingredients) {
            ingredientRepository.findByName(dto.getName())
                    .orElseThrow(() -> new IngredientNotFoundException(dto.getName()));
        }
        Recipe recipe = toEntity(recipeDto);
        Recipe savedRecipe = recipeRepository.save(recipe);
        return toDto(savedRecipe);
    }

    /**
     * Добавление ингредиента
     */
    public IngredientResponseDTO addIngredient(AddIngredientRequestDTO ingredientDto) {
        if (ingredientRepository.findByName(ingredientDto.getName()).isPresent()) {
            throw new DuplicateNameException("Ингредиент с таким именем уже существует: " + ingredientDto.getName());
        }

        Ingredient ingredient = toEntity(ingredientDto);
        Ingredient savedIngredient = ingredientRepository.save(ingredient);
        return toDto(savedIngredient);
    }

    public IngredientListResponseDTO getAllIngredients() {
        List<IngredientResponseDTO> ingredientResponseDTOS = ingredientRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return new IngredientListResponseDTO(ingredientResponseDTOS, ingredientResponseDTOS.size());
    }

    // Новый метод для получения списка всех рецептов с их ингредиентами
    public ReceiptListResponseDTO getAllRecipes() {
        List<ReceiptResponseDTO> receiptResponseDTOS = recipeRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return new ReceiptListResponseDTO(receiptResponseDTOS, receiptResponseDTOS.size());
    }

    public InfoDTO getMostPopularBeverage() {
        BeverageStatistics stats = beverageStatisticsRepository.findTopByOrderByCountDesc()
                .orElse(new BeverageStatistics()); // Обработка случая, когда нет статистики

        return new InfoDTO(
                stats.getCount() > 0
                        ? "Most popular beverage is " + stats.getBeverage().getName() + " with " + stats.getCount() + " orders"
                        : "No statistics available yet",
                stats.getCount() > 0
        );
    }

    /**
     * Обновление статистики напитка
     */
    private void updateStatistics(Recipe recipe) {
        BeverageStatistics stats = beverageStatisticsRepository.findByBeverage(recipe)
                .orElseGet(() -> {
                    BeverageStatistics newStats = new BeverageStatistics();
                    newStats.setBeverage(recipe);
                    newStats.setCount(1);
                    return newStats;
                });

        stats.setCount(stats.getCount() + 1);
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
