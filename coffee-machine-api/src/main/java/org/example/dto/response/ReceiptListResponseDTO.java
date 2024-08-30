package org.example.dto.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptListResponseDTO {
    @JsonProperty("recipes")
    private List<ReceiptResponseDTO> recipes;  // Список рецептов

    @JsonProperty("total")
    private int total;  // Общее количество рецептов
}
