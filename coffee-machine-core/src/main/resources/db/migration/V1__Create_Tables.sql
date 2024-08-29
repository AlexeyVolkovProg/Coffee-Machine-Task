CREATE SCHEMA IF NOT EXISTS coffee_machine;

-- Создание таблицы Ingredient в схеме coffee_machine
CREATE TABLE IF NOT EXISTS coffee_machine.ingredient
(
    id       SERIAL PRIMARY KEY,
    name     VARCHAR(255) UNIQUE NOT NULL,
    quantity INT                 NOT NULL
);

-- Создание таблицы Recipe в схеме coffee_machine
CREATE TABLE IF NOT EXISTS coffee_machine.recipe
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL
);

-- Создание связанной таблицы для хранения ингредиентов рецепта в схеме coffee_machine
CREATE TABLE IF NOT EXISTS coffee_machine.recipe_ingredients
(
    recipe_id       BIGINT       NOT NULL,
    ingredient_name VARCHAR(255) NOT NULL,
    quantity        INT          NOT NULL,
    FOREIGN KEY (recipe_id) REFERENCES coffee_machine.recipe (id),
    FOREIGN KEY (ingredient_name) REFERENCES coffee_machine.ingredient (name)
);
