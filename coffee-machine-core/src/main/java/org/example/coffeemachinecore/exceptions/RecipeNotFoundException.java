package org.example.coffeemachinecore.exceptions;

/**
 * Исключение, когда рецепт был не найден
 */
public class RecipeNotFoundException extends RuntimeException{
    public RecipeNotFoundException(String drinkName){
        super("Recipe not found " + drinkName);
    }
}
