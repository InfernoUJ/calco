package com.example.calco.notifications;

import static androidx.core.app.ActivityCompat.requestPermissions;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import java.util.Calendar;
import java.util.List;

public class ReminderManager {
    private static final List<Integer> hours = List.of(13, 20);
    private static final Integer minutes = 0;
    private static final int reminderId = 1;
    public static void makeNewReminder(Context context, int previousHour) {
        int nextHour = findNextHour(previousHour);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent intent = createPendingIntent(context);
        Calendar calendar = createCalendar(nextHour);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), intent), intent);
                System.out.println("Alarm set for " + calendar.get(Calendar.DAY_OF_MONTH) + "; "+ nextHour + ":00");
            }
        }
    }

    public static void makeNewReminder(Context context) {
        makeNewReminder(context, Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
    }

    private static int findNextHour(int previousHour) {
        int newIndex = 0;
        while (newIndex < hours.size() && hours.get(newIndex) <= previousHour) {
            newIndex++;
        }
        if (newIndex == hours.size()) {
            newIndex = 0;
        }
        return hours.get(newIndex);
    }

    private static PendingIntent createPendingIntent(Context context) {
        return PendingIntent.getBroadcast(
                context.getApplicationContext(),
                reminderId,
                new Intent(context.getApplicationContext(), NotificationSender.class),
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );
    }

    private static Calendar createCalendar(int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minutes);
        if (Calendar.getInstance().after(calendar)) {
            calendar.add(Calendar.DATE, 1);
        }
        return calendar;
    }
}
