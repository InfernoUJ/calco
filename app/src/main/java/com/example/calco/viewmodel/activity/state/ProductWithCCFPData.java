package com.example.calco.viewmodel.activity.state;
import com.example.calco.logic.business.Product;
public class ProductWithCCFPData extends FoodWithCCFPData {
    private Product product;
    private FoodWithCCFPData foodWithCCFPData;

    public ProductWithCCFPData(Product product, FoodWithCCFPData foodWithCCFPData) {
        super(foodWithCCFPData.getName(), foodWithCCFPData.getImageId(), foodWithCCFPData.getCalories(), foodWithCCFPData.getCarbs(), foodWithCCFPData.getFats(), foodWithCCFPData.getProteins());
        this.product = product;
        this.foodWithCCFPData = foodWithCCFPData;
    }

    public Product getProduct() {
        return product;
    }

}
