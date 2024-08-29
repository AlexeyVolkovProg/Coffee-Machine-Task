package org.example.coffeemachinecore.repository;

import org.example.coffeemachinecore.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Recipe findByName(String name);
}
