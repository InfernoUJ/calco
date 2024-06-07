package com.example.calco.ui.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.calco.R;
import com.example.calco.ui.charts.pie.PieChartsPercents;
import com.example.calco.ui.products.table.FoodImpactRecordData;

import java.net.Inet4Address;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * Implementation of App Widget functionality.
 */
public class PieChartsWidget extends AppWidgetProvider {
    private static final List<Integer> colors = List.of(R.color.PIE_CHART_CALORIES, R.color.PIE_CHART_CARBS, R.color.PIE_CHART_FATS, R.color.PIE_CHART_PROTEINS);
    private static final Integer bgColor = R.color.PIE_CHART_BG;
    private static final Integer chartWidth = 90;
    private static final Integer chartHeight = 90;
    private Consumer<List<Integer>> updatePercents;
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                List<Integer> percents = extras.getIntegerArrayList("percents");
                if (percents == null) {
                    percents = Collections.nCopies(colors.size(), 0);
                }
                if (updatePercents != null) {
                    updatePercents.accept(percents);
                }
            }
        }
    }
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        // There may be multiple widgets active, so update all of them
        updatePercents = percents -> {
            for (int appWidgetId : appWidgetIds) {
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.pie_charts_widget);
                for (int i = 0; i < colors.size(); i++) {
                    Bitmap bitmap = createPieChartBitmap(chartWidth, chartHeight, percents.get(i)/100f, colors.get(i), context.getResources());
                    views.setImageViewBitmap(getImageViewIdForIndex(i), bitmap);
                }
                appWidgetManager.updateAppWidget(appWidgetId, views);
            }
        };
    }

    private int getImageViewIdForIndex(int index) {
        // You need to map index to actual ImageViews' IDs defined in the widget's XML
        switch (index) {
            case 0: return R.id.widget_calories;
            case 1: return R.id.widget_carbs;
            case 2: return R.id.widget_fats;
            case 3: return R.id.widget_proteins;
            default: return -1;
        }
    }

    private Bitmap createPieChartBitmap(int width, int height, float segmentPercentage, int segmentColor, Resources resources) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        int cx = width / 2;
        int cy = height / 2;
        int radius = Math.min(cx, cy);

        RectF oval = new RectF(cx - radius, cy - radius, cx + radius, cy + radius);

        float startAngle = -90;
        float sweepAngle = segmentPercentage * 360;

        paint.setColor(resources.getColor(segmentColor));
        canvas.drawArc(oval, startAngle, sweepAngle, true, paint);

        paint.setColor(resources.getColor(bgColor));
        canvas.drawArc(oval, startAngle + sweepAngle, 360 - sweepAngle, true, paint);

        return bitmap;
    }
}