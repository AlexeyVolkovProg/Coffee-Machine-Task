package org.example.coffeemachinecore.exceptions;


/**
 * Исключение на случай, когда недостаточно ингредиентов
 */
public class InsufficientIngredientException extends RuntimeException{
    public InsufficientIngredientException(String ingredient){
        super("Not enough " + ingredient);
    }

}
