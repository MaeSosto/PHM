package com.example.personalhealthmonitor.Notification;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.Observer;

import com.example.personalhealthmonitor.Database.Report;
import com.example.personalhealthmonitor.Home.NewReportActivity;
import com.example.personalhealthmonitor.R;
import com.example.personalhealthmonitor.Utilities.Converters;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.personalhealthmonitor.MainActivity.KEY_REPORT_DAILY;
import static com.example.personalhealthmonitor.MainActivity.SDF;
import static com.example.personalhealthmonitor.MainActivity.reportViewModel;
import static com.example.personalhealthmonitor.Notification.Alarm.CHANNEL_ID;
import static com.example.personalhealthmonitor.Notification.Alarm.NOTIFICATION_ID;

public class Notification_receiver extends BroadcastReceiver {
    private Context mContext;
    private NotificationManagerCompat notificationManagerCompat;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        notificationManagerCompat = NotificationManagerCompat.from(context);
        if (intent.getAction().equals(KEY_REPORT_DAILY)) {
            Log.i("Notify Daily", "CHECK");
            //reportViewModel.getCountInDay(Converters.StringToDate(SDF.format(Calendar.getInstance().getTime())));
            new getNumReportsToday().execute();
            sendDailyNotification(0);
        }
    }

    private class getNumReportsToday extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            Log.i("BACKGROUND", "");
            return null;
        }
    }

    private void sendDailyNotification(int num){
        if(num == 0){
            //INTENT NOTIFICA GIORNALIERA
            Intent intent_notification = new Intent(mContext, NewReportActivity.class);
            intent_notification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent_notification, PendingIntent.FLAG_ONE_SHOT);

            //INTENT BOTTONE1 NOTIFICA GIORNALIERA
            Intent IntentNuovoReport = new Intent(mContext, NewReportActivity.class);
            IntentNuovoReport.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntentNuovoReport = PendingIntent.getActivity(mContext, 0,IntentNuovoReport, PendingIntent.FLAG_ONE_SHOT);

            //INTENT BOTTONE2 NOTIFICA GIORNALIERA
            Intent IntentRimanda = new Intent(mContext, Rimanda.class);
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

            notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
            Log.i("Notify Daily", "Report Daily Alarm");
        }
        else{
            Log.i("Notify Daily", "Hai già un report oggi");
        }
    }
}
