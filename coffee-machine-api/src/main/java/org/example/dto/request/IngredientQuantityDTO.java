package org.example.dto.request;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IngredientQuantityDTO {

    @JsonProperty("name")
    private String name; // название ингредиента

    @JsonProperty("quantity")
    private int quantity; // необходимое количества

    @JsonCreator
    public IngredientQuantityDTO(@JsonProperty("name") String name,
                                 @JsonProperty("quantity") int quantity) {
        this.name = name;
        this.quantity = quantity;
    }
}
