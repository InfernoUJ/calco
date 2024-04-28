package com.example.calco.viewmodel.activity.state;
import com.example.calco.logic.business.entities.Dish;

public class DishWithCCFPData extends FoodWithCCFPData {
    private Dish dish;

    public DishWithCCFPData(Dish dish, String name, String calories, String carbs, String fats, String proteins) {
        super(name, dish.getImageName(), calories, carbs, fats, proteins);
        this.dish = dish;
    }
    @Override
    public Dish getFood() {
        return dish;
    }

}
