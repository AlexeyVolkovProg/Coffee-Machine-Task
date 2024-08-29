package org.example.coffeemachinecore.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;


@Entity
@Setter
@Getter
@Table(schema = "coffee_machine", name="recipe")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    /**
     * Хранит названия нужных ингредиентов
     * с кол-ом, которое необходимо для готовки одного заказа данного рецепта
     */
    @ElementCollection
    @MapKeyColumn(name = "ingredient_name")
    @Column(name = "quantity")
    private Map<String, Integer> ingredients = new HashMap<>();
}
