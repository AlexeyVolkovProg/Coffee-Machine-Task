package org.example.coffeemachinecore.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(schema = "coffee_machine", name="beverage_statistics")
public class BeverageStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Recipe beverage;

    private int count; // Количество заказов
}
