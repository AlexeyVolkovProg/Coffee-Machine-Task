package org.example.coffeemachinecore.repository;

import org.example.coffeemachinecore.model.BeverageStatistics;
import org.example.coffeemachinecore.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeverageStatisticsRepository extends JpaRepository<BeverageStatistics, Long> {

    BeverageStatistics findByBeverage(Recipe beverage);
    BeverageStatistics findTopByOrderByCountDesc();
}
