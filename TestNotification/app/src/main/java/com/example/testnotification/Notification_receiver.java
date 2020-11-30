package com.example.testnotification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static com.example.testnotification.MainActivity.CHANNEL_ID;
import static com.example.testnotification.MainActivity.NOTIFICATION_ID;

public class Notification_receiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {


        Intent intent_notification = new Intent(context, NewReportActivity.class);
        intent_notification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent_notification, PendingIntent.FLAG_ONE_SHOT);

        Intent IntentNuovoReport = new Intent(context, NewReportActivity.class);
        IntentNuovoReport.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntentNuovoReport = PendingIntent.getActivity(context, 0,IntentNuovoReport, PendingIntent.FLAG_ONE_SHOT);

        Intent IntentRimanda = new Intent(context, Rimanda.class);
        IntentRimanda.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntentRimanda = PendingIntent.getActivity(context, 0,IntentRimanda, PendingIntent.FLAG_ONE_SHOT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_baseline_calendar_today_24)
                .setContentTitle("Non hai ancora aggiunto report oggi")
                .setContentText("")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Monitorare i tuoi parametri è importante, clicca qua per inserire un nuovo report"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_baseline_calendar_today_24, "Aggiungi report", pendingIntentNuovoReport)
                .addAction(R.drawable.ic_baseline_calendar_today_24, "Rimanda", pendingIntentRimanda);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
        if (intent.getAction().equals("MY_NOTIFICATION_MESSAGE")) {
            notificationManagerCompat.notify(NOTIFICATION_ID,builder.build());
            Log.i("Notify", "Alarm"); //Optional, used for debuging.
        }
    }

}
