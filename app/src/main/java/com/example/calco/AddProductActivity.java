package com.example.calco;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.calco.ui.pickers.data.DatePickerFragment;

public class AddProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.add_product_main), (v, insets) -> {
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
            Intent createProductActivityIntent = new Intent(this, CreateProductActivity.class);
            startActivity(createProductActivityIntent);
        });
    }
}