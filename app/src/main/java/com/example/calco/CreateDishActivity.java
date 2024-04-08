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

import com.example.calco.viewmodel.activity.CreateDishVM;
import com.example.calco.viewmodel.activity.CreateProductVM;

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

    private void setHandlers() {
        setCreateDishButtonHandler();
    }

    private void setCreateDishButtonHandler() {
        View createDishBtn = findViewById(R.id.createProductBtn);
        createDishBtn.setOnClickListener(view -> {
            finish();
        });
    }
}