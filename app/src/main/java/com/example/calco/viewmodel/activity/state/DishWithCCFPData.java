package com.example.calco.viewmodel.activity.state;
import com.example.calco.logic.business.Dish;
import com.example.calco.logic.business.Product;

public class DishWithCCFPData extends FoodWithCCFPData {
    private Dish dish;

    public DishWithCCFPData(Dish dish, String name, Integer imageId, String calories, String carbs, String fats, String proteins) {
        super(name, imageId, calories, carbs, fats, proteins);
        this.dish = dish;
    }
    @Override
    public Dish getFood() {
        return dish;
    }

}
