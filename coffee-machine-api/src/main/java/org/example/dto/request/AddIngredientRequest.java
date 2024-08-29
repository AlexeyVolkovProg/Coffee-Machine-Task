package org.example.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddIngredientRequest {
    @JsonProperty("name")
    private String name;

    @JsonProperty("quantity")
    private int quantity;
}
