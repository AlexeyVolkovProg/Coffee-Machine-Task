package org.example.coffeemachinecore.handler;

import org.example.coffeemachinecore.exceptions.DuplicateNameException;
import org.example.coffeemachinecore.exceptions.IngredientNotFoundException;
import org.example.coffeemachinecore.exceptions.InsufficientIngredientException;
import org.example.coffeemachinecore.exceptions.RecipeNotFoundException;
import org.example.dto.error.ApiErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Класс помогает разрешить ситуации с ошибками при запросах
 */

@ControllerAdvice
public class BadRequestHandler {

    public ResponseEntity<ApiErrorResponseDTO> handleValidationException(Exception ex, String description, HttpStatus status) {
        ApiErrorResponseDTO response = new ApiErrorResponseDTO(
                description,
                status.toString(),
                ex.getClass().getSimpleName(),
                ex.getMessage(),
                Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()));
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(RecipeNotFoundException.class)
    public ResponseEntity<ApiErrorResponseDTO> handleRecipeNotFoundException(RecipeNotFoundException ex) {
        return handleValidationException(ex, "Рецепт не найден", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InsufficientIngredientException.class)
    public ResponseEntity<ApiErrorResponseDTO> handleInsufficientIngredientException(InsufficientIngredientException ex) {
        return handleValidationException(ex, "Недостаточно ингредиентов", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IngredientNotFoundException.class)
    public ResponseEntity<ApiErrorResponseDTO> handleIngredientNotFoundException(IngredientNotFoundException ex) {
        return handleValidationException(ex, "Ингредиент не найден", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateNameException.class)
    public ResponseEntity<ApiErrorResponseDTO> handleDuplicateNameException(DuplicateNameException ex) {
        return handleValidationException(ex, "Элемент с таким именем уже существует", HttpStatus.CONFLICT);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponseDTO> handleOtherExceptions(Exception ex) {
        return handleValidationException(ex, "Скорее всего ошибка в формате запроса. Непредвиденное исключение.", HttpStatus.BAD_REQUEST);
    }



}
