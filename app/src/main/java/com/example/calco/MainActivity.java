package com.example.calco;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.calco.logic.business.entities.LimitType;
import com.example.calco.logic.persistent.databases.AppDataBase;
import com.example.calco.ui.charts.pie.CCFPPieChartGroupFragment;
import com.example.calco.ui.dialogs.SetLimitsDialog;
import com.example.calco.ui.dialogs.WayToChooseImageDialog;
import com.example.calco.ui.pickers.data.DatePickerFragment;
import com.example.calco.ui.products.table.FoodTableFragment;
import com.example.calco.ui.widget.PieChartsWidget;
import com.example.calco.viewmodel.activity.FoodTableVM;
import com.example.calco.viewmodel.activity.LimitsVM;
import com.example.calco.viewmodel.activity.PieChartsVM;
import com.example.calco.logic.business.entities.FoodComponent;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.calco.databinding.ActivityMainBinding;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DatePickerFragment.DatePickerListener,
        SetLimitsDialog.SetLimitsDialogListener, WayToChooseImageDialog.WayToChooseImageDialogListener{
    public static final String dateFormat = "yyyy-MM-dd";

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private FoodTableVM foodTableModel;
    private PieChartsVM pieChartsModel;
    private LimitsVM limitsModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppDataBase.createInstance(getApplicationContext());

        foodTableModel = new ViewModelProvider(this).get(FoodTableVM.class);
        pieChartsModel = new ViewModelProvider(this).get(PieChartsVM.class);
        limitsModel = new ViewModelProvider(this).get(LimitsVM.class);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        // this is mail icon action
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setAnchorView(R.id.fab).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();

        setHandlers();

        setDateToField(LocalDate.now());

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateFoodTable();
        updatePieCharts();
    }

    // TODO maybe create base class, so this method will call all methods annotated with my annotation
    private void setHandlers() {
        setDateChoosingHandlers();
        setAddingProductHandlers();
        setFoodTableHandler();
        setPieChartsHandler();
        setCaloriesLimitHandler();
        setRadioButtonsFilters();
    }

    private void setDateChoosingHandlers() {
        View chooseDateButton = binding.appBarMain.getRoot().findViewById(R.id.chooseDateBtn);
        chooseDateButton.setOnClickListener(view -> {
            DatePickerFragment newFragment = new DatePickerFragment();
            newFragment.show(getSupportFragmentManager(), "datePicker");
        });
    }

    private void setAddingProductHandlers() {
        View addProductBtn = binding.appBarMain.getRoot().findViewById(R.id.addProductBtn);
        addProductBtn.setOnClickListener(view -> {
            Intent addProductactivityIntent = new Intent(this, AddFoodActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("date", getDate());
            addProductactivityIntent.putExtras(bundle);
            startActivity(addProductactivityIntent);
        });
    }

    private void setFoodTableHandler() {
        FoodTableFragment productTable = (FoodTableFragment) getSupportFragmentManager().findFragmentById(R.id.food_table_fragment);
        foodTableModel.getFoodRecords().observe(this, productImpactRecordDataList -> {
            productTable.replaceProducts(productImpactRecordDataList);
        });
    }

    private void setPieChartsHandler() {
        CCFPPieChartGroupFragment pieChartGroup = (CCFPPieChartGroupFragment) getSupportFragmentManager().findFragmentById(R.id.ccfp_pie_chart_group_fragment);
        pieChartsModel.getPercents().observe(this, pieChartsPercents -> {
            pieChartGroup.updateLoadings(pieChartsPercents);
        });
    }

    private void setCaloriesLimitHandler() {
        PieChart caloriesChart = (PieChart) findViewById(R.id.pieChartCalories);
        caloriesChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                SetLimitsDialog dialog = new SetLimitsDialog(LimitType.DAILY);
                dialog.show(getSupportFragmentManager(), "setLimitsDialog");
            }

            @Override
            public void onNothingSelected() {
                System.out.println("onNothingSelected");
            }
        });
    }

    private void setRadioButtonsFilters() {
        List<Integer> radioButtonsIds = List.of(R.id.calories_radio_button, R.id.carbs_radio_button, R.id.fats_radio_button, R.id.proteins_radio_button);
        for (int id : radioButtonsIds) {
            ((RadioButton)findViewById(id)).setOnCheckedChangeListener((view, isChecked) -> {
                if (isChecked) {
                    FoodComponent component = FoodComponent.values()[radioButtonsIds.indexOf(id)];
                    foodTableModel.sortFoodBy(component, getLocalDate(), getResources(), getPackageName());
                }
            });
        }
    }

    @Override
    public void onDialogPositiveClick(DatePickerFragment dialog, int year, int month, int day) {
        LocalDate chosenDate = LocalDate.of(year, month, day);
        setDateToField(chosenDate);
        updateFoodTable();
        updatePieCharts();
    }

    private void updateFoodTable() {
        foodTableModel.updateFoodTable(getLocalDate(), getResources(), getPackageName());
    }

    private void updatePieCharts() {
        pieChartsModel.updatePercents(getLocalDate());
        updateHomeWidget();
    }

    @Override
    public void onDialogNegativeClick(DatePickerFragment dialog) {

    }

    private void setDateToField(LocalDate date) {
        ((TextView)binding.appBarMain.getRoot().findViewById(R.id.dateTextView)).setText(date.format(DateTimeFormatter.ofPattern(dateFormat)));
    }

    private String getDate() {
        return ((TextView)binding.appBarMain.getRoot().findViewById(R.id.dateTextView)).getText().toString();
    }

    private LocalDate getLocalDate() {
        return LocalDate.parse(getDate(), DateTimeFormatter.ofPattern(MainActivity.dateFormat));
    }

    @Override
    public void onSetLimitsDialogPositiveClick(DialogFragment dialog, LimitType type, String calories, String carbs, String fats, String proteins) {
        System.out.println("Calories: " + calories + " Carbs: " + carbs + " Fats: " + fats + " Proteins: " + proteins);
        limitsModel.setLimit(type, calories, carbs, fats, proteins);
        updatePieCharts();
    }

    @Override
    public void onSetLimitsDialogNegativeClick(DialogFragment dialog) {

    }

    @Override
    public void onDialogPositiveClick(WayToChooseImageDialog dialog) {
        Intent intent = new Intent(this, TakingPictureActivity.class);
        intent.putExtra("food", dialog.getFood());
        startActivity(intent);
    }

    @Override
    public void onDialogNegativeClick(WayToChooseImageDialog dialog) {

    }

    @Override
    public void onDialogNeutralClick(WayToChooseImageDialog dialog) {
        Intent intent = new Intent(this, SelectPictureActivity.class);
        intent.putExtra("food", dialog.getFood());
        startActivity(intent);
    }

    private void updateHomeWidget() {
        Intent intent = new Intent(this, PieChartsWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), PieChartsWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        intent.putIntegerArrayListExtra("percents", pieChartsModel.getPercents().getValue().toList());
        sendBroadcast(intent);
    }
}