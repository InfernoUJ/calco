package com.example.calco;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.calco.logic.persistent.databases.AppDataBase;
import com.example.calco.viewmodel.activity.CreateDishVM;
import com.example.calco.viewmodel.activity.CreateProductVM;
import com.example.calco.viewmodel.activity.state.FoodWithCCFPData;
import com.example.calco.viewmodel.activity.state.FoodWithCCFPData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CreateDishActivity extends AppCompatActivity {

    private CreateDishVM model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_dish);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        model = new ViewModelProvider(this).get(CreateDishVM.class);

        setHandlers();
    }

    // why onResume wasn't being called? - idk
    @Override
    protected void onStart() {
        super.onStart();
        model.updateProductList(getResources(), getPackageName());
    }

    private void setHandlers() {
        setCreateDishButtonHandler();
        setLastUsedProductsHandler();
    }

    private void setCreateDishButtonHandler() {
        View createDishBtn = findViewById(R.id.createDishBtn);
        createDishBtn.setOnClickListener(view -> {
            List<Map.Entry<Integer, Integer>> chosenProducts = determineChosenProducts();
            model.createDish(getDishName(), chosenProducts);
            finish();
        });
    }

    private void setLastUsedProductsHandler() {
        model.getProducts().observe(this, this::addProductsToTable);
    }

    private String getDishName() {
        return ((EditText) findViewById(R.id.dishName)).getText().toString();
    }

    private void addProductsToTable(List<FoodWithCCFPData> product) {
        // todo can make null-termination it in querry method ?
        //  or create my own annotation for query methods
        if (product == null) {
            return;
        }
        LinearLayout lastProductsTable = (LinearLayout) findViewById(R.id.dishProductsLayout);
        lastProductsTable.removeAllViews();
        product.forEach(p -> {
            View productRow = createFoodRecord(p);
            lastProductsTable.addView(productRow);
        });
    }

    private View createFoodRecord(FoodWithCCFPData product) {
        View productRow = getLayoutInflater().inflate(R.layout.component_of_dish,null);

        ImageView productImage = productRow.findViewById(R.id.component_image);
        TextView productName = productRow.findViewById(R.id.component_name);
        TextView productCalories = productRow.findViewById(R.id.component_calories);
        TextView productCarbs = productRow.findViewById(R.id.component_carbs);
        TextView productFats = productRow.findViewById(R.id.component_fats);
        TextView productProteins = productRow.findViewById(R.id.component_proteins);

        productImage.setImageResource(product.getImageId());
        productName.setText(product.getName());
        productCalories.setText(product.getCalories());
        productCarbs.setText(product.getCarbs());
        productFats.setText(product.getFats());
        productProteins.setText(product.getProteins());

        return productRow;
    }

    // todo maybe refactor - remove parsing logic from here
    private List<Map.Entry<Integer, Integer>> determineChosenProducts() {
        List<Map.Entry<Integer, Integer>> chosenProducts = new ArrayList<>();
        LinearLayout lastProductsTable = (LinearLayout) findViewById(R.id.dishProductsLayout);
        for(int i = 0; i < lastProductsTable.getChildCount(); i++) {
            View productRow = lastProductsTable.getChildAt(i);
            EditText productAmount = productRow.findViewById(R.id.amount_in_g);
            if (productAmount.getText().toString().equals("")) {
                continue;
            }
            int amount = Integer.parseInt(productAmount.getText().toString());
            if (amount == 0) {
                continue;
            }
            chosenProducts.add(Map.entry(i, amount));
        }
        return chosenProducts;
    }
}