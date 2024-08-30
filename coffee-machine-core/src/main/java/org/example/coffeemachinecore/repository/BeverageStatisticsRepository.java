package org.example.coffeemachinecore.repository;

import org.example.coffeemachinecore.model.BeverageStatistics;
import org.example.coffeemachinecore.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BeverageStatisticsRepository extends JpaRepository<BeverageStatistics, Long> {
    Optional<BeverageStatistics> findByBeverage(Recipe beverage);
    Optional<BeverageStatistics> findTopByOrderByCountDesc();
}
