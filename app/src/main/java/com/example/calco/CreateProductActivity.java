package com.example.calco;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.calco.logic.business.ProductLogic;

public class CreateProductActivity extends AppCompatActivity {

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

        setHandlers();
    }

    private void setHandlers() {
        setCreateProductButtonHandler();
    }

    private void setCreateProductButtonHandler() {
        View createProductBtn = findViewById(R.id.createProductBtn);
        createProductBtn.setOnClickListener(view -> {
            String name = ((EditText)findViewById(R.id.productName)).getText().toString();
            Integer calories = Integer.parseInt(((EditText)findViewById(R.id.productCalories)).getText().toString());
            Integer carbs = Integer.parseInt(((EditText)findViewById(R.id.productCarbs)).getText().toString());
            Integer fats = Integer.parseInt(((EditText)findViewById(R.id.productFats)).getText().toString());
            Integer proteins = Integer.parseInt(((EditText)findViewById(R.id.productProteins)).getText().toString());

            ProductLogic.createNewProduct(name, calories, carbs, fats, proteins);
            System.out.println("finishing activity");
            finish();
        });
    }
}