package com.example.lab5_20200825_iot.Notificaciones;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.lab5_20200825_iot.R;

public class NotificationHelper {
    // Se  hizo uso de chatGPT para la configuración del NotificationHelper

    public static final String CHANNEL_ID_HIGH = "TareasHigh";
    public static final String CHANNEL_ID_DEFAULT = "TareasDefault";
    public static final String CHANNEL_ID_LOW = "TareasLow";
    public static final String CHANNEL_ID_PERSISTENT = "TareasPersistent";

    public static void createNotificationChannels(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel highChannel = new NotificationChannel(
                    CHANNEL_ID_HIGH,
                    "High Priority Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            highChannel.setDescription("Channel for high priority notifications");

            NotificationChannel defaultChannel = new NotificationChannel(
                    CHANNEL_ID_DEFAULT,
                    "Default Priority Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            defaultChannel.setDescription("Channel for default priority notifications");

            NotificationChannel lowChannel = new NotificationChannel(
                    CHANNEL_ID_LOW,
                    "Low Priority Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            lowChannel.setDescription("Channel for low priority notifications");

            NotificationChannel persistentChannel = new NotificationChannel(
                    CHANNEL_ID_PERSISTENT,
                    "Persistent Notification Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            persistentChannel.setDescription("Channel for persistent notifications");

            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(highChannel);
            manager.createNotificationChannel(defaultChannel);
            manager.createNotificationChannel(lowChannel);
            manager.createNotificationChannel(persistentChannel);
        }
    }

    public static void sendNotification(Context context, String title, String message, int priority) {
        String channelId;
        if (priority == NotificationManagerCompat.IMPORTANCE_HIGH) {
            channelId = CHANNEL_ID_HIGH;
        } else if (priority == NotificationManagerCompat.IMPORTANCE_DEFAULT) {
            channelId = CHANNEL_ID_DEFAULT;
        } else {
            channelId = CHANNEL_ID_LOW;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(priority)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }

    public static void sendPersistentNotification(Context context, String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_PERSISTENT)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(1, builder.build());
    }
}
