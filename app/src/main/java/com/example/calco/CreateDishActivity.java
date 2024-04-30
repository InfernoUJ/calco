package com.example.calco;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        RecyclerView recyclerView = findViewById(R.id.dishProductsScrollView);
        recyclerView.setAdapter(model.getAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private String getDishName() {
        return ((EditText) findViewById(R.id.dishName)).getText().toString();
    }


    // todo maybe refactor - remove parsing logic from here
    private List<Map.Entry<Integer, Integer>> determineChosenProducts() {
        List<Map.Entry<Integer, Integer>> chosenProducts = new ArrayList<>();
        ConstraintLayout lastProductsTable = (ConstraintLayout) findViewById(R.id.dish_component_table_record);
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