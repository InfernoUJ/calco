package com.example.calco.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.example.calco.MainActivity;

import java.time.LocalDateTime;
import java.util.Calendar;

public class NotificationSender extends BroadcastReceiver {
    public static final String CHANNEL_ID = "10001";
    protected Class<MainActivity> notificationIntentClass = MainActivity.class;
    private static final int NOTIFICATION_ID = 1;
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent notificationIntent = createNotificationIntent(context);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
//                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Keep tracking!")
                .setContentText("Add what you have eaten today")
                .setAutoCancel(true)
                .setContentIntent(notificationIntent)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());

        ReminderManager.makeNewReminder(context, LocalDateTime.now().getHour());
    }

    protected PendingIntent createNotificationIntent(Context context) {
        return PendingIntent.getActivity(context, 0, new Intent(context, notificationIntentClass), PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }
}
