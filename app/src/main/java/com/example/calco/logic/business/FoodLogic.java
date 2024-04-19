package com.example.calco.logic.business;

import com.example.calco.ui.charts.pie.PieChartsPercents;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class FoodLogic {
    public static PieChartsPercents calculateGoalCompletion(LocalDate date) {
        List<Food> food = ProductLogic.getDayHistory(date).stream().map(HistoryOfProducts::getProduct).collect(Collectors.toList());
        food.addAll(DishLogic.getDayHistory(date).stream().map(HistoryOfDishes::getDish).collect(Collectors.toList()));
        // todo add limits to DB
        int caloriesLimit = 500_000;
        int carbsLimit = 500_000;
        int fatsLimit = 500_000;
        int proteinsLimit = 500_000;
        return new PieChartsPercents(calculateCalories(food)*100/caloriesLimit, calculateCarbs(food)*100/carbsLimit, calculateFats(food)*100/fatsLimit, calculateProteins(food)*100/proteinsLimit);
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
}
