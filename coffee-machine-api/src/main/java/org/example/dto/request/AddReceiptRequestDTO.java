package org.example.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AddReceiptRequestDTO {
    @JsonProperty("name")
    private String name;  // Название напитка

    @JsonProperty("ingredients")
    private List<IngredientQuantityDTO> ingredients;  // Список необходимых ингредиентов и их количество

    @JsonCreator
    public AddReceiptRequestDTO(@JsonProperty("name") String name,
                                @JsonProperty("ingredients") List<IngredientQuantityDTO> ingredients) {
        this.name = name;
        this.ingredients = ingredients;
    }
}
