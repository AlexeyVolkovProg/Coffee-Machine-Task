package org.example.dto.response;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.dto.request.IngredientQuantityDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ReceiptResponse {

    @JsonProperty("name")
    private String name;  // Название напитка

    @JsonProperty("ingredients")
    private List<IngredientQuantityDto> ingredients;  // Список необходимых ингредиентов и их количество

    @JsonCreator
    public ReceiptResponse(@JsonProperty("name") String name,
                           @JsonProperty("ingredients") List<IngredientQuantityDto> ingredients) {
        this.name = name;
        this.ingredients = ingredients;
    }
}
