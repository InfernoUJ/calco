package com.example.calco.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

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

        if (alarmManager.canScheduleExactAlarms()) {
            alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), intent), intent);
        }
    }

    public static void makeNewReminder(Context context) {
        makeNewReminder(context, Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
    }

    private static int findNextHour(int previousHour) {
        int nexIndex = (hours.lastIndexOf(previousHour) + 1) >= hours.size() ? 0 : hours.lastIndexOf(previousHour) + 1;
        return hours.get(nexIndex);
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
