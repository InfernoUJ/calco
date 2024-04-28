package com.example.calco.viewmodel.activity.state;
import com.example.calco.logic.business.entities.Product;
public class ProductWithCCFPData extends FoodWithCCFPData {
    private Product product;

    public ProductWithCCFPData(Product product, String name, String calories, String carbs, String fats, String proteins) {
        super(name, product.getImageName(), calories, carbs, fats, proteins);
        this.product = product;
    }
    @Override
    public Product getFood() {
        return product;
    }

}
