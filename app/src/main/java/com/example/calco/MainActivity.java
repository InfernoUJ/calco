package com.example.calco;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.calco.databinding.ActivityMainBinding;
import com.example.calco.logic.business.entities.FoodComponent;
import com.example.calco.logic.business.entities.LimitType;
import com.example.calco.logic.files.JsonFilesCreator;
import com.example.calco.logic.files.JsonZipCreator;
import com.example.calco.logic.files.JsonZipReader;
import com.example.calco.logic.persistent.databases.AppDataBase;
import com.example.calco.logic.persistent.entities.PProduct;
import com.example.calco.notifications.NotificationSender;
import com.example.calco.notifications.ReminderManager;
import com.example.calco.ui.charts.pie.CCFPPieChartGroupFragment;
import com.example.calco.ui.dialogs.SetLimitsDialog;
import com.example.calco.ui.dialogs.WayToChooseImageDialog;
import com.example.calco.ui.pickers.data.DatePickerFragment;
import com.example.calco.ui.products.table.FoodTableFragment;
import com.example.calco.ui.widget.PieChartsWidget;
import com.example.calco.viewmodel.activity.FoodTableVM;
import com.example.calco.viewmodel.activity.LimitsVM;
import com.example.calco.viewmodel.activity.PieChartsVM;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DatePickerFragment.DatePickerListener,
        SetLimitsDialog.SetLimitsDialogListener, WayToChooseImageDialog.WayToChooseImageDialogListener {
    public static final String dateFormat = "yyyy-MM-dd";
    public static final int REQUEST_ALARM = 7;

    private AppBarConfiguration mAppBarConfiguration;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ActivityMainBinding binding;

    private FoodTableVM foodTableModel;
    private PieChartsVM pieChartsModel;
    private LimitsVM limitsModel;

    private FoodTableFragment productTable;

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

        productTable = (FoodTableFragment) getSupportFragmentManager().findFragmentById(R.id.food_table_fragment);

        askForPermissions();
        setHandlers();

        setEndDateToField(LocalDate.now());
        setStartDateToField(LocalDate.now());

//        String path = JsonZipCreator.createZip(getApplicationContext(), JsonFilesCreator.createJsonFiles());
//        System.out.println( "Path: "+path);
//        List<PProduct> products = JsonZipReader.deserialize(PProduct.class,  path);
//        System.out.println( "Products: "+products);
//        for(PProduct product : products) {
//            System.out.println( "Product: "+product.uid);
//        }
//        System.out.println( "===");
//        for(PProduct product : AppDataBase.getInstance().productDao().getAll()) {
//            System.out.println( "Product: "+product.uid);
//        }
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
    public boolean onOptionsItemSelected(MenuItem item) {
        System.out.println( "Item selected: "+ item);
        Intent intent = new Intent(this, DataActivity.class);
        startActivity(intent);
        return true;
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
        setPieChartsHandler();
        setCaloriesLimitHandler();
        setRadioButtonsFilters();
        setNotificationChannels();
    }

    private void askForPermissions() {
        askCameraPermission();
        askStoragePermission();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            askNotificationPermission();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            askAlarmPermission();
        }
    }

    private void askCameraPermission() {
        if (checkSelfPermission(android.Manifest.permission.CAMERA) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.CAMERA}, 1);
        }
    }

    private void askStoragePermission() {
        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void askNotificationPermission() {
        if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    public void askAlarmPermission() {
        if (checkSelfPermission(Manifest.permission.SCHEDULE_EXACT_ALARM) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.SCHEDULE_EXACT_ALARM}, REQUEST_ALARM);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ALARM) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                System.out.println( "Alarm permission granted");
            }
            else {
                System.out.println( "Alarm permission not granted");
            }
        }
    }
    private void setNotificationChannels() {
        NotificationChannel channel = new NotificationChannel(NotificationSender.CHANNEL_ID, "Calco", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManagerCompat.from(this).createNotificationChannel(channel);
        ReminderManager.makeNewReminder(this);
    }

    private void setDateChoosingHandlers() {
        View chooseDateButton = binding.appBarMain.getRoot().findViewById(R.id.endDateTextView);
        chooseDateButton.setOnClickListener(view -> {
            DatePickerFragment newFragment = new DatePickerFragment(DatePickerFragment.DateType.END_DATE);
            newFragment.show(getSupportFragmentManager(), "datePicker");
        });

        View previousDateButton = binding.appBarMain.getRoot().findViewById(R.id.startDateTextView);
        previousDateButton.setOnClickListener(view -> {
            DatePickerFragment newFragment = new DatePickerFragment(DatePickerFragment.DateType.START_DATE);
            newFragment.show(getSupportFragmentManager(), "datePicker");
        });
    }

    private void setAddingProductHandlers() {
        View addProductBtn = binding.appBarMain.getRoot().findViewById(R.id.addProductBtn);
        addProductBtn.setOnClickListener(view -> {
            Intent addProductactivityIntent = new Intent(this, AddFoodActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("date", getEndDate());
            addProductactivityIntent.putExtras(bundle);
            startActivity(addProductactivityIntent);
        });
    }

    private void setPieChartsHandler() {
        CCFPPieChartGroupFragment pieChartGroup = (CCFPPieChartGroupFragment) getSupportFragmentManager().findFragmentById(R.id.ccfp_pie_chart_group_fragment);
        pieChartsModel.getPercents().observe(this, pieChartsPercents -> {
            pieChartGroup.updateLoadings(pieChartsPercents);
        });
    }

    private void setCaloriesLimitHandler() {
        View caloriesChart = findViewById(R.id.pieChartGroup);
        caloriesChart.setOnClickListener(view -> {
            SetLimitsDialog dialog = new SetLimitsDialog(LimitType.DAILY);
            dialog.show(getSupportFragmentManager(), "setLimitsDialog");
        });
    }

    private void setRadioButtonsFilters() {
        List<Integer> radioButtonsIds = List.of(R.id.calories_radio_button, R.id.carbs_radio_button, R.id.fats_radio_button, R.id.proteins_radio_button);
        for (int id : radioButtonsIds) {
            ((RadioButton)findViewById(id)).setOnCheckedChangeListener((view, isChecked) -> {
                if (isChecked) {
                    FoodComponent component = FoodComponent.values()[radioButtonsIds.indexOf(id)];
                    foodTableModel.sortFoodBy(component, getStartLocalDate(), getEndLocalDate(), getResources(), getPackageName());
                }
            });
        }
    }

    @Override
    public void onDialogPositiveClick(DatePickerFragment dialog, int year, int month, int day, DatePickerFragment.DateType type) {
        LocalDate chosenDate = LocalDate.of(year, month, day);
        switch (type) {
            case START_DATE:
                // set the same date for creating less misunderstandings
                setStartDateToField(chosenDate);
                setEndDateToField(chosenDate);
                break;
            case END_DATE:
                setEndDateToField(chosenDate);
                break;
        }
        updateFoodTable();
        updatePieCharts();
    }

    private void updateFoodTable() {
        productTable.replaceProducts(getStartLocalDate(), getEndLocalDate(), getResources(), getPackageName());
    }

    private void updatePieCharts() {
        pieChartsModel.updatePercents(getStartLocalDate(), getEndLocalDate());
        updateHomeWidget();
    }

    @Override
    public void onDialogNegativeClick(DatePickerFragment dialog) {

    }

    private void setStartDateToField(LocalDate date) {
        ((TextView)binding.appBarMain.getRoot().findViewById(R.id.startDateTextView)).setText(date.format(DateTimeFormatter.ofPattern(dateFormat)));
    }

    private void setEndDateToField(LocalDate date) {
        ((TextView)binding.appBarMain.getRoot().findViewById(R.id.endDateTextView)).setText(date.format(DateTimeFormatter.ofPattern(dateFormat)));
    }

    private String getEndDate() {
        return ((TextView)binding.appBarMain.getRoot().findViewById(R.id.endDateTextView)).getText().toString();
    }

    private String getStartDate() {
        return ((TextView)binding.appBarMain.getRoot().findViewById(R.id.startDateTextView)).getText().toString();
    }

    private LocalDate getEndLocalDate() {
        return LocalDate.parse(getEndDate(), DateTimeFormatter.ofPattern(MainActivity.dateFormat));
    }

    private LocalDate getStartLocalDate() {
        return LocalDate.parse(getStartDate(), DateTimeFormatter.ofPattern(MainActivity.dateFormat));
    }

    @Override
    public void onSetLimitsDialogPositiveClick(DialogFragment dialog, LimitType type, String calories, String carbs, String fats, String proteins) {
        System.out.println( "Calories: " + calories + " Carbs: " + carbs + " Fats: " + fats + " Proteins: " + proteins);
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