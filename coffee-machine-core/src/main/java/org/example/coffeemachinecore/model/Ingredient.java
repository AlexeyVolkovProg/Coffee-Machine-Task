package org.example.coffeemachinecore.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(schema = "coffee_machine", name="ingredient")
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Название ингредиента
     * Должно быть уникальным
     */
    @Column(unique = true)
    private String name;


    /**
     * Кол-во единиц данного ингредиента,
     * которое содержится во внутреннем хранилище кофе машины
     */
    private int quantity;
}
