package org.example.coffeemachinecore.exceptions;

/**
 * Исключение, когда ингредиент был не найден
 */
public class IngredientNotFoundException extends RuntimeException {
    public IngredientNotFoundException(String ingredient){
        super("Ingredient not found " + ingredient);
    }
}
