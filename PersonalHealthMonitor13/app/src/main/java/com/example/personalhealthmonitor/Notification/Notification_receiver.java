package com.example.personalhealthmonitor.Notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.Observer;

import com.example.personalhealthmonitor.Database.Settings;
import com.example.personalhealthmonitor.Home.NewReportActivity;
import com.example.personalhealthmonitor.R;
import com.example.personalhealthmonitor.Statistiche.StatisticheFragment;
import com.example.personalhealthmonitor.Utilities.Converters;
import com.example.personalhealthmonitor.Utilities.Utility;
import com.github.mikephil.charting.data.BarEntry;

import java.nio.file.attribute.AclEntry;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.personalhealthmonitor.MainActivity.filtro;
import static com.example.personalhealthmonitor.MainActivity.reportViewModel;
import static com.example.personalhealthmonitor.MainActivity.settingsViewModel;
import static com.example.personalhealthmonitor.Notification.Alarm.CHANNEL_ID;
import static com.example.personalhealthmonitor.Notification.Alarm.KEY_REPORT_DAILY;
import static com.example.personalhealthmonitor.Notification.Alarm.KEY_WARNING;
import static com.example.personalhealthmonitor.Notification.Alarm.NOTIFICATION_ID;

public class Notification_receiver extends BroadcastReceiver {
    NotificationManagerCompat notificationManagerCompat;
    String warningString;

    @Override
    public void onReceive(Context context, Intent intent) {
        notificationManagerCompat = NotificationManagerCompat.from(context);

        if (intent.getAction().equals(KEY_REPORT_DAILY)) {
            getDailyNotification(context);
        }
        if(intent.getAction().equals(KEY_WARNING)){
           new getWarningNotification().execute(context);
        }
    }

    //private void getWarningNotification(Context context) {
    //    //INTENT NOTIFICA GIORNALIERA
    //    Intent intent_notification = new Intent(context, StatisticheFragment.class);
    //    intent_notification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    //    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent_notification, PendingIntent.FLAG_ONE_SHOT);
//
    //    settingsViewModel.getmAllSettingsFilter(3).observeForever(new Observer<List<Settings>>() {
    //        @Override
    //        public void onChanged(List<Settings> settings) {
    //            if(settings.size() > 0){
    //                warningString = "";
    //                for(int i = 0; i< settings.size(); i++){
    //                    Settings setting = settings.get(i);
    //                    Double ValAVG = reportViewModel.getAvgVal(setting.getValore(), setting.getInizio(), setting.getFine());
    //                    if(setting.getLimite() > ValAVG){
    //                        warningString = warningString + "IL valore per "+ Utility.KeyToPrompt(setting.getValore())+ " supera il limite medio di "+ ValAVG+ "/"+setting.getLimite() + " nel periodo: "+ Converters.DateToString(setting.getInizio()) + "-"+ Converters.DateToString(setting.getFine()) + "\n";
    //                    }
    //                }
//
    //                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
    //                builder.setSmallIcon(R.drawable.ic_baseline_calendar_today_24)
    //                        .setContentTitle("Attenzione: controlla le tue informazioni sanitarie")
    //                        .setContentText("")
    //                        .setStyle(new NotificationCompat.BigTextStyle()
    //                                .bigText(warningString))
    //                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    //                        .setContentIntent(pendingIntent)
    //                        .setAutoCancel(true);
//
//
    //                notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
    //                Log.i("Notify", "Report Warning Alarm");
    //            }
    //        }
    //    });
    //}

    private void getDailyNotification(Context context){
        //INTENT NOTIFICA GIORNALIERA
        Intent intent_notification = new Intent(context, NewReportActivity.class);
        intent_notification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent_notification, PendingIntent.FLAG_ONE_SHOT);

        //INTENT BOTTONE1 NOTIFICA GIORNALIERA
        Intent IntentNuovoReport = new Intent(context, NewReportActivity.class);
        IntentNuovoReport.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntentNuovoReport = PendingIntent.getActivity(context, 0,IntentNuovoReport, PendingIntent.FLAG_ONE_SHOT);

        //INTENT BOTTONE2 NOTIFICA GIORNALIERA
        Intent IntentRimanda = new Intent(context, Rimanda.class);
        IntentRimanda.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntentRimanda = PendingIntent.getActivity(context, 0,IntentRimanda, PendingIntent.FLAG_ONE_SHOT);

        //NOTIFICA GIORNALIERA BUILDER
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

        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
        Log.i("Notify", "Report Daily Alarm");
    }

    class getWarningNotification extends AsyncTask<Context, Void, NotificationCompat.Builder> {

        @Override
        protected void onPostExecute(NotificationCompat.Builder builder) {
            super.onPostExecute(builder);
            if(builder != null) {
                notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
                Log.i("Notify", "Report Warning Alarm");
            }
        }

        @Override
        protected NotificationCompat.Builder doInBackground(Context... contexts) {

            Context context = contexts[0];
            //INTENT NOTIFICA GIORNALIERA
            Intent intent_notification = new Intent(context, StatisticheFragment.class);
            intent_notification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent_notification, PendingIntent.FLAG_ONE_SHOT);

            List<Settings> list = settingsViewModel.getmAllSettingsFilter(3).getValue();
            if(list!= null && list.size() > 0) {
                warningString = "";
                for (int i = 0; i < list.size(); i++) {
                    Settings setting = list.get(i);
                    Double ValAVG = reportViewModel.getAvgVal(setting.getValore(), setting.getInizio(), setting.getFine());
                    if (setting.getLimite() > ValAVG) {
                        warningString = warningString + "IL valore per " + Utility.KeyToPrompt(setting.getValore()) + " supera il limite medio di " + ValAVG + "/" + setting.getLimite() + " nel periodo: " + Converters.DateToString(setting.getInizio()) + "-" + Converters.DateToString(setting.getFine()) + "\n";
                    }
                }

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
                builder.setSmallIcon(R.drawable.ic_baseline_calendar_today_24)
                        .setContentTitle("Attenzione: controlla le tue informazioni sanitarie")
                        .setContentText("")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(warningString))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

                return builder;
            }
            return null;
        }
    }
}
