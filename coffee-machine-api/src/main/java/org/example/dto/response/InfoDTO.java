package org.example.dto.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *  DTO используется для ответа на запросы о готовки напитка, обновления данных конкретного ингредиента,
 *  а также для отправки ответа со статистиков заказов самого популярного рецепта
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InfoDTO {

    @JsonProperty("message")
    private String message;

    @JsonProperty("success")
    private boolean success;

}
