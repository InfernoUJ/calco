package com.example.calco.viewmodel.activity.state;
import com.example.calco.logic.business.Product;
public class ProductWithCCFPData extends FoodWithCCFPData {
    private Product product;
    private FoodWithCCFPData foodWithCCFPData;

    public ProductWithCCFPData(Product product, String name, Integer imageId, String calories, String carbs, String fats, String proteins) {
        super(name, imageId, calories, carbs, fats, proteins);
        this.product = product;
        this.foodWithCCFPData = foodWithCCFPData;
    }
    @Override
    public Product getFood() {
        return product;
    }

}
