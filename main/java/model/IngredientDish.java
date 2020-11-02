package model;

import java.util.Objects;

public class IngredientDish {
    private Dish dish;
    private Ingredient ingredient;

    public IngredientDish(Dish dish, Ingredient ingredient) {
        this.dish = dish;
        this.ingredient = ingredient;
    }

    public Dish getDish() {
        return dish;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    @Override
    public String toString() {
        return "IngredientDish{" +
                "dish=" + dish +
                ", ingredient=" + ingredient +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IngredientDish)) return false;
        IngredientDish that = (IngredientDish) o;
        return Objects.equals(dish, that.dish) &&
                Objects.equals(ingredient, that.ingredient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dish, ingredient);
    }
}
