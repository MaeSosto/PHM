package com.example.personalhealthmonitor.Notification;

import android.app.AlarmManager;
import android.app.AsyncNotedAppOp;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.loader.content.AsyncTaskLoader;

import com.example.personalhealthmonitor.Database.Notification;
import com.example.personalhealthmonitor.Database.Report;
import com.example.personalhealthmonitor.Utilities.Converters;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.personalhealthmonitor.MainActivity.KEY_REPORT_DAILY;
import static com.example.personalhealthmonitor.MainActivity.SDF;
import static com.example.personalhealthmonitor.MainActivity.notificationViewModel;
import static com.example.personalhealthmonitor.MainActivity.reportViewModel;

public class Alarm extends Service {
    public static final String CHANNEL_ID = "Daily_notification";
    public static final int NOTIFICATION_ID = 1;

    private AlarmManager alarmManagerReportDaily;
    private PendingIntent pendingIntentReportDaily;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("NOTIFICATION ALARM", "CREATED");
        //Creo il canale delle notifiche
        createNotificationChannel();

        //Setto le notifiche giornaliere
        alarmManagerReportDaily = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intentAlarm = new Intent(getApplicationContext(), Notification_receiver.class);
        intentAlarm.setAction(KEY_REPORT_DAILY);
        pendingIntentReportDaily = PendingIntent.getBroadcast(getApplicationContext(), NOTIFICATION_ID, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationViewModel.getNotification().observeForever(new Observer<Notification>() {
            @Override
            public void onChanged(Notification notification) {
                if(notification.isStatus()){
                    Log.i("NOTIFICATION ALARM", "ON");
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, notification.getOra());
                    calendar.set(Calendar.MINUTE, notification.getMinuti());
                    calendar.set(Calendar.SECOND, 0);
                    alarmManagerReportDaily.setRepeating(android.app.AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), /*android.app.AlarmManager.INTERVAL_DAY*/ 500, pendingIntentReportDaily);
                }
                else{
                    Log.i("NOTIFICATION ALARM", "OFF");
                    alarmManagerReportDaily.cancel(pendingIntentReportDaily);
                }
            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //CREO IL CANALE DELLE NOTIFICHE
    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Personal Notification";
            String description = "Descrizione";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationChannel.setDescription(description);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
