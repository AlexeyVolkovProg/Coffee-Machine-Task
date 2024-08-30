package org.example.api;


import org.example.dto.request.AddIngredientRequest;
import org.example.dto.request.AddReceiptRequest;
import org.example.dto.response.*;

public interface CoffeeMachineAPI {

    /**
     * Обработка запросов на добавление нового рецепта
     */
    ReceiptResponse addRecipe(AddReceiptRequest recipeDto);

    /**
     * Обработка запросов на добавление нового ингредиента
     */
    IngredientResponse addIngredient(AddIngredientRequest ingredientDto);


    /**
     * Получить список всех доступных рецептов
     */
    ReceiptListResponse getAllRecipes();


    /**
     * Получить список всех доступных ингредиентов
     */
    IngredientListResponse getAllIngredients();


    /**
     * Запустить готовку напитка по названию рецепта
     */
    InfoDTO prepareDrink(String drinkName);

    /**
     * Обновить какой-то ингредиент
     */
    InfoDTO updateIngredientQuantity(AddIngredientRequest ingredient);

    /**
     * Получить статистику по самому популярному напитку
     */
    InfoDTO getMostPopularBeverage();

}
