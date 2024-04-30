package com.example.calco.logic.business;

import com.example.calco.logic.business.entities.Food;
import com.example.calco.logic.business.entities.FoodComponent;
import com.example.calco.logic.business.entities.HistoryOfDishes;
import com.example.calco.logic.business.entities.HistoryOfFood;
import com.example.calco.logic.business.entities.HistoryOfProducts;
import com.example.calco.logic.business.entities.Limit;
import com.example.calco.logic.business.entities.LimitType;
import com.example.calco.logic.business.entities.LimitsLogic;
import com.example.calco.ui.charts.pie.PieChartsPercents;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class FoodLogic {
    public static PieChartsPercents calculateGoalCompletion(LocalDate date) {
        List<Food> food = ProductLogic.getDayHistory(date).stream().map(HistoryOfProducts::getProduct).collect(Collectors.toList());
        food.addAll(DishLogic.getDayHistory(date).stream().map(HistoryOfDishes::getDish).collect(Collectors.toList()));
        Limit limit = LimitsLogic.getLimit(LimitType.DAILY);

        return new PieChartsPercents(calculateCalories(food), calculateCarbs(food), calculateFats(food), calculateProteins(food), limit);
    }

    private static int calculateCalories(List<Food> food) {
        return food.stream().mapToInt(Food::getCalories).sum();
    }

    private static int calculateCarbs(List<Food> food) {
        return food.stream().mapToInt(Food::getCarbs).sum();
    }

    private static int calculateFats(List<Food> food) {
        return food.stream().mapToInt(Food::getFats).sum();
    }

    private static int calculateProteins(List<Food> food) {
        return food.stream().mapToInt(Food::getProteins).sum();
    }

    public static float getComponentPercentage(Food food, FoodComponent component) {
        switch (component) {
            case CALORIES:
                return food.getCalories()/100_000f;
            case CARBS:
                return food.getCarbs()/100_000f;
            case FATS:
                return food.getFats()/100_000f;
            case PROTEINS:
                return food.getProteins()/100_000f;
            default:
                throw new IllegalArgumentException("Unknown component: " + component);
        }
    }

    public static <T extends HistoryOfFood> List<T> uniteSameFood(List<T> history) {
        BiFunction<HistoryOfFood, HistoryOfFood, Boolean> sameFood = (h1, h2) -> h1.getFood().equals(h2.getFood()) && h1.getDate().equals(h2.getDate());
        class HistoryOfFoodWithHash {
            T history;
            HistoryOfFoodWithHash(T history) {
                this.history = history;
            }

            public void mergeWith(T other) {
                history.setMilligrams(history.getMilligrams() + other.getMilligrams());
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                HistoryOfFoodWithHash that = (HistoryOfFoodWithHash) o;
                return Objects.equals(history.getFood(), that.history.getFood()) && Objects.equals(history.getDate(), that.history.getDate());
            }

            @Override
            public int hashCode() {
                return Objects.hash(history.getFood(), history.getDate());
            }
        }

        List<HistoryOfFoodWithHash> united = new ArrayList<>();
        for (T h : history) {
            int uniqueFoodIdx;
            if ((uniqueFoodIdx = united.indexOf(new HistoryOfFoodWithHash(h))) >= 0) {
                united.get(uniqueFoodIdx).mergeWith(h);
            }
            else {
                united.add(new HistoryOfFoodWithHash(h));
            }
        }

        return united.stream().map(h -> h.history).collect(Collectors.toList());
    }
}
