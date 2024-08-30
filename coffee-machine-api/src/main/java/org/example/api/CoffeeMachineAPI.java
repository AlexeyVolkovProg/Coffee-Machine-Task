package org.example.api;


import org.example.dto.request.AddIngredientRequestDTO;
import org.example.dto.request.AddReceiptRequestDTO;
import org.example.dto.response.*;

public interface CoffeeMachineAPI {

    /**
     * Обработка запросов на добавление нового рецепта
     */
    ReceiptResponseDTO addRecipe(AddReceiptRequestDTO recipeDto);

    /**
     * Обработка запросов на добавление нового ингредиента
     */
    IngredientResponseDTO addIngredient(AddIngredientRequestDTO ingredientDto);


    /**
     * Получить список всех доступных рецептов
     */
    ReceiptListResponseDTO getAllRecipes();


    /**
     * Получить список всех доступных ингредиентов
     */
    IngredientListResponseDTO getAllIngredients();


    /**
     * Запустить готовку напитка по названию рецепта
     */
    InfoDTO prepareDrink(String drinkName);

    /**
     * Обновить какой-то ингредиент
     */
    InfoDTO updateIngredientQuantity(AddIngredientRequestDTO ingredient);

    /**
     * Получить статистику по самому популярному напитку
     */
    InfoDTO getMostPopularBeverage();

}
