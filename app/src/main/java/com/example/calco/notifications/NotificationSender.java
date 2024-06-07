package com.example.calco.notifications;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.calco.MainActivity;

import java.time.LocalDateTime;

public class NotificationSender extends BroadcastReceiver {
    public static final String CHANNEL_ID = "10001";
    protected Class<MainActivity> notificationIntentClass = MainActivity.class;
    private static final int NOTIFICATION_ID = 44;

    @Override
    public void onReceive(Context context, Intent intent) {
        PendingIntent notificationIntent = createNotificationIntent(context);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Keep tracking!")
                .setContentText("Add what you have eaten today")
                .setAutoCancel(true)
                .setContentIntent(notificationIntent)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        ReminderManager.makeNewReminder(context, LocalDateTime.now().getHour());

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            System.out.println( "No permission");
            return;
        }

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notificationBuilder.build());
        System.out.println( "Notification sent");
    }

    protected PendingIntent createNotificationIntent(Context context) {
        return PendingIntent.getActivity(context, 0, new Intent(context, notificationIntentClass), PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }
}
