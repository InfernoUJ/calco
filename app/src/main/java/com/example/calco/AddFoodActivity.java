package com.example.calco;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.example.calco.viewmodel.activity.AddFoodVM;
import com.example.calco.viewmodel.activity.state.FoodWithCCFPData;

import java.util.List;
import java.util.stream.Stream;

public class AddFoodActivity extends AppCompatActivity {

    private AddFoodVM model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_food);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.add_food_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        model = new ViewModelProvider(this).get(AddFoodVM.class);

        setHandlers();

    }

    // why onResume wasn't being called? - idk
    @Override
    protected void onStart() {
        super.onStart();
        System.out.println(AppDataBase.getInstance() == null);
        model.updateLastUsedFood(getResources(), getPackageName());
    }

    private void setHandlers() {
        setCreateFoodButtonHandler();
        setLastUsedFoodHandler();
    }

    private void setCreateFoodButtonHandler() {
        View createProductBtn = findViewById(R.id.createProductBtn);
        createProductBtn.setOnClickListener(view -> {
            Intent createProductActivityIntent = new Intent(this, CreateProductActivity.class);
            startActivity(createProductActivityIntent);
        });
    }

    private void setLastUsedFoodHandler() {
        model.getFood().observe(this, this::addFoodToTable);
    }

    private void addFoodToTable(List<FoodWithCCFPData> food) {
        // todo can make null-termination it in querry method ?
        //  or create my own annotation for query methods
        if (food == null) {
            return;
        }
        LinearLayout lastFoodTable = (LinearLayout) findViewById(R.id.lastFoodTableLinearLayout);
        lastFoodTable.removeAllViews();
        food.forEach(foodRecord -> {
            View foodRow = createFoodRecord(foodRecord);
            lastFoodTable.addView(foodRow);
        });
    }

    private View createFoodRecord(FoodWithCCFPData food) {
        View productRow = getLayoutInflater().inflate(R.layout.last_food_table_record,null);

        ImageView productImage = productRow.findViewById(R.id.last_food_image);
        TextView productName = productRow.findViewById(R.id.last_food_name);
        TextView productCalories = productRow.findViewById(R.id.last_food_calories);
        TextView productCarbs = productRow.findViewById(R.id.last_food_carbs);
        TextView productFats = productRow.findViewById(R.id.last_food_fats);
        TextView productProteins = productRow.findViewById(R.id.last_food_proteins);

        productImage.setImageResource(food.getImageId());
        productName.setText(food.getName());
        productCalories.setText(food.getCalories());
        productCarbs.setText(food.getCarbs());
        productFats.setText(food.getFats());
        productProteins.setText(food.getProteins());

        return productRow;
    }
}