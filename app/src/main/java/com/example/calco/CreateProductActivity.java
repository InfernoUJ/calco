package com.example.calco;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.calco.logic.business.ProductLogic;
import com.example.calco.viewmodel.activity.CreateProductVM;
import com.example.calco.viewmodel.activity.state.CreateProductUiState;

public class CreateProductActivity extends AppCompatActivity {

    private CreateProductVM model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.create_product_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        model = new ViewModelProvider(this).get(CreateProductVM.class);

        setHandlers();
    }

    private void setHandlers() {
        setCreateProductButtonHandler();
    }

    private void setCreateProductButtonHandler() {
        View createProductBtn = findViewById(R.id.createProductBtn);
        createProductBtn.setOnClickListener(view -> {
            String name = ((EditText)findViewById(R.id.productName)).getText().toString();
            String calories = ((EditText)findViewById(R.id.productCalories)).getText().toString();
            String carbs = ((EditText)findViewById(R.id.productCarbs)).getText().toString();
            String fats = ((EditText)findViewById(R.id.productFats)).getText().toString();
            String proteins = ((EditText)findViewById(R.id.productProteins)).getText().toString();

            model.createNewProduct(name, calories, carbs, fats, proteins);
            finish();
        });
    }
}