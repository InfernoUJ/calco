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
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.calco.logic.persistent.databases.AppDataBase;
import com.example.calco.ui.dialogs.MassInputDialog;
import com.example.calco.viewmodel.activity.AddFoodVM;
import com.example.calco.viewmodel.activity.state.FoodWithCCFPData;

import java.util.List;

public class AddFoodActivity extends AppCompatActivity implements MassInputDialog.MassInputDialogListener {

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
        setCreateProductButtonHandler();
        setLastUsedFoodHandler();
        setCreateDishButtonHandler();
    }

    private void setCreateProductButtonHandler() {
        View createProductBtn = findViewById(R.id.createProductBtn);
        createProductBtn.setOnClickListener(view -> {
            Intent createProductActivityIntent = new Intent(this, CreateProductActivity.class);
            startActivity(createProductActivityIntent);
        });
    }

    private void setCreateDishButtonHandler() {
        View createDishBtn = findViewById(R.id.createDishBtn);
        createDishBtn.setOnClickListener(view -> {
            Intent createDishActivityIntent = new Intent(this, CreateDishActivity.class);
            startActivity(createDishActivityIntent);
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
            setDialogHandlerForFoodRow(foodRow);
            lastFoodTable.addView(foodRow);
        });
    }

    private void setDialogHandlerForFoodRow(View view) {
        view.setOnClickListener(v -> {
            DialogFragment dialogFragment = new MassInputDialog();
            dialogFragment.show(getSupportFragmentManager(), "massInputDialog");
        });
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String mass) {
        // User touched the dialog's positive button
        System.out.println("Mass: " + mass + " time form bundle: " + getIntent().getExtras().getString("date"));
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
        // do nothing
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