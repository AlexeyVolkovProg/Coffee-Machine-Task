-- Вставка начальных данных в таблицу ingredient в схеме coffee_machine
INSERT INTO coffee_machine.ingredient (name, quantity) VALUES ('Coffee Beans', 1000);
INSERT INTO coffee_machine.ingredient (name, quantity) VALUES ('Water', 100);
INSERT INTO coffee_machine.ingredient (name, quantity) VALUES ('Milk', 100);

-- Вставка начальных данных в таблицу recipe в схеме coffee_machine
INSERT INTO coffee_machine.recipe (name) VALUES ('Espresso');
INSERT INTO coffee_machine.recipe (name) VALUES ('Americano');
INSERT INTO coffee_machine.recipe (name) VALUES ('Cappuccino');

-- Вставка данных в таблицу recipe_ingredients в схеме coffee_machine
INSERT INTO coffee_machine.recipe_ingredients (recipe_id, ingredient_name, quantity)
VALUES
    (1, 'Coffee Beans', 50);

INSERT INTO coffee_machine.recipe_ingredients (recipe_id, ingredient_name, quantity)
VALUES
    (2, 'Coffee Beans', 50),
    (2, 'Water', 100);

INSERT INTO coffee_machine.recipe_ingredients (recipe_id, ingredient_name, quantity)
VALUES
    (3, 'Coffee Beans', 50),
    (3, 'Milk', 100),
    (3, 'Water', 50);
