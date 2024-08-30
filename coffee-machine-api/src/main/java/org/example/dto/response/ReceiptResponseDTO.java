package org.example.dto.response;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.dto.request.IngredientQuantityDTO;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ReceiptResponseDTO {

    @JsonProperty("name")
    private String name;  // Название напитка

    @JsonProperty("ingredients")
    private List<IngredientQuantityDTO> ingredients;  // Список необходимых ингредиентов и их количество

    @JsonCreator
    public ReceiptResponseDTO(@JsonProperty("name") String name,
                              @JsonProperty("ingredients") List<IngredientQuantityDTO> ingredients) {
        this.name = name;
        this.ingredients = ingredients;
    }
}
