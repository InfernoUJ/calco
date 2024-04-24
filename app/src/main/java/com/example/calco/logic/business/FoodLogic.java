package com.example.calco.logic.business;

import com.example.calco.logic.business.entities.Food;
import com.example.calco.logic.business.entities.HistoryOfDishes;
import com.example.calco.logic.business.entities.HistoryOfProducts;
import com.example.calco.logic.business.entities.Limit;
import com.example.calco.logic.business.entities.LimitType;
import com.example.calco.logic.business.entities.LimitsLogic;
import com.example.calco.ui.charts.pie.PieChartsPercents;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class FoodLogic {
    public static PieChartsPercents calculateGoalCompletion(LocalDate date) {
        List<Food> food = ProductLogic.getDayHistory(date).stream().map(HistoryOfProducts::getProduct).collect(Collectors.toList());
        food.addAll(DishLogic.getDayHistory(date).stream().map(HistoryOfDishes::getDish).collect(Collectors.toList()));
        // todo add limits to DB
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
}
