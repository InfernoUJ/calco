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

public class    FoodLogic {
    public static PieChartsPercents calculateGoalCompletion(LocalDate startDate, LocalDate endDate) {
        List<HistoryOfFood> food = new ArrayList<>();
        food.addAll(ProductLogic.getHistoryForPeriod(startDate, endDate));
        food.addAll(DishLogic.getHistoryForPeriod(startDate, endDate));
        Limit limit = LimitsLogic.getLimit(LimitType.DAILY);

        return new PieChartsPercents(calculateCalories(food), calculateCarbs(food), calculateFats(food), calculateProteins(food), limit);
    }

    private static int calculateCalories(List<HistoryOfFood> food) {
        return food.stream().mapToInt(history -> (int)(history.getFood().getCalories()/100f*history.getMilligrams()/1000f)).sum();
    }

    private static int calculateCarbs(List<HistoryOfFood> food) {
        return food.stream().mapToInt(history -> (int)(history.getFood().getCarbs()/100f*history.getMilligrams()/1000f)).sum();
    }

    private static int calculateFats(List<HistoryOfFood> food) {
        return food.stream().mapToInt(history -> (int)(history.getFood().getFats()/100f*history.getMilligrams()/1000f)).sum();
    }

    private static int calculateProteins(List<HistoryOfFood> food) {
        return food.stream().mapToInt(history -> (int)(history.getFood().getProteins()/100f*history.getMilligrams()/1000f)).sum();
    }

    public static Integer getComponentAbsolute(HistoryOfFood food, FoodComponent component) {
        switch (component) {
            case CALORIES:
                return calculateCalories(List.of(food));
            case CARBS:
                return calculateCarbs(List.of(food));
            case FATS:
                return calculateFats(List.of(food));
            case PROTEINS:
                return calculateProteins(List.of(food));
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
