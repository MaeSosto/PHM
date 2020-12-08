package com.example.personalhealthmonitor.Notification;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.example.personalhealthmonitor.Home.HomeFragment;
import com.example.personalhealthmonitor.Home.NewReportActivity;
import com.example.personalhealthmonitor.R;
import com.example.personalhealthmonitor.Statistiche.StatisticheFragment;
import static com.example.personalhealthmonitor.Utilities.Utility.*;

public class Notification_receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        //NOTIFICA GIORNALIERA
        if (intent.getAction().equals(KEY_REPORT_DAILY)) {
            notificationManagerCompat.notify(NOTIFICATION_ID, sendDailyNotification(context).build());
            Log.i("Notify", "Report Daily Alarm");
        }
        //NOTIFICA DI ALLARME
       if (intent.getAction().equals(KEY_WARNING)) {
           String WarningString = intent.getStringExtra("WarningString");
           notificationManagerCompat.notify(NOTIFICATION_ID, sendWarningNotification(context, WarningString).build());
           Log.i("Notify", "Report Warning Alarm");
       }
       //NOTIFICA RIMANDATA
       if(intent.getAction().equals(KEY_RIMANDA)){
           notificationManagerCompat.notify(NOTIFICATION_ID, sendDailyNotification(context).build());
           Log.i("Notify", "Report Rimanda");
       }
    }

    //CREA LA NOTIFICA DI ALLARME
    private NotificationCompat.Builder sendWarningNotification(Context mContext, String WarningString) {
        //INTENT NOTIFICA WARNING
        Intent intent_notification = new Intent(mContext, StatisticheFragment.class);
        intent_notification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent_notification, PendingIntent.FLAG_ONE_SHOT);

        //NOTIFICA GIORNALIERA BUILDER
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_baseline_warning_24)
                .setContentTitle("Attenzione, alcuni valori hanno superato il limite!")
                .setContentText("")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(WarningString))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
//
        return builder;
    }

    //CREA LA NOTIFICA GIORNALIERA O QUELLA CHE DEVE ESSEERE RIMANDATA
    private NotificationCompat.Builder sendDailyNotification(Context mContext){
        //INTENT NOTIFICA GIORNALIERA
        Intent intent_notification = new Intent(mContext, HomeFragment.class);
        intent_notification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent_notification, PendingIntent.FLAG_ONE_SHOT);

        //INTENT BOTTONE1 NOTIFICA GIORNALIERA
        Intent IntentNuovoReport = new Intent(mContext, NewReportActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
        stackBuilder.addNextIntentWithParentStack(IntentNuovoReport);
        PendingIntent pendingIntentNuovoReport = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        //INTENT BOTTONE2 NOTIFICA GIORNALIERA
        Intent IntentRimanda = new Intent(mContext, RimandaActivity.class);
        IntentRimanda.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntentRimanda = PendingIntent.getActivity(mContext, 0,IntentRimanda, PendingIntent.FLAG_ONE_SHOT);

        //NOTIFICA GIORNALIERA BUILDER
            NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID);
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

            return builder;
    }
}
