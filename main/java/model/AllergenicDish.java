package model;

import java.util.Objects;

public class AllergenicDish {
    private Allergenic allergenic;
    private Dish dish;

    public AllergenicDish(Allergenic allergenic, Dish dish) {
        this.allergenic = allergenic;
        this.dish = dish;
    }

    public Allergenic getAllergenic() {
        return allergenic;
    }

    public Dish getDish() {
        return dish;
    }

    @Override
    public String toString() {
        return "AllergenicDish{" +
                "allergenic=" + allergenic +
                ", dish=" + dish +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AllergenicDish)) return false;
        AllergenicDish that = (AllergenicDish) o;
        return Objects.equals(allergenic, that.allergenic) &&
                Objects.equals(dish, that.dish);
    }

    @Override
    public int hashCode() {
        return Objects.hash(allergenic, dish);
    }
}
