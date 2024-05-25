package com.example.lab5_20200825_iot.Notificaciones;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.NotificationCompat;
import com.example.lab5_20200825_iot.R;
// Se  hizo uso de chatGPT para la configuración del ReminderReciever

public class ReminderReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");
        String message = intent.getStringExtra("message");
        int priority = intent.getIntExtra("priority", NotificationManagerCompat.IMPORTANCE_DEFAULT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, getNotificationChannelId(priority))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(priority)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }

    private String getNotificationChannelId(int priority) {
        switch (priority) {
            case NotificationManagerCompat.IMPORTANCE_HIGH:
                return NotificationHelper.CHANNEL_ID_HIGH;
            case NotificationManagerCompat.IMPORTANCE_DEFAULT:
                return NotificationHelper.CHANNEL_ID_DEFAULT;
            case NotificationManagerCompat.IMPORTANCE_LOW:
                return NotificationHelper.CHANNEL_ID_LOW;
            default:
                return NotificationHelper.CHANNEL_ID_DEFAULT;
        }
    }
}
