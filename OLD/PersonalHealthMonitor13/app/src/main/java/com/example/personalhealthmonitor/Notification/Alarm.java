package com.example.personalhealthmonitor.Notification;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import com.example.personalhealthmonitor.Database.Notification;
import java.util.Calendar;

import static com.example.personalhealthmonitor.MainActivity.notificationViewModel;

public class Alarm extends Service {
    public static final String CHANNEL_ID = "Daily_notification";
    public static final int NOTIFICATION_ID = 1;
    public static final String KEY_REPORT_DAILY = "REPORT GIORNALIERO";
    private AlarmManager alarmManagerReportDaily;
    private PendingIntent pendingIntentReportDaily;
    public static final String KEY_WARNING = "WARNING";
    private AlarmManager alarmManagerWarning;
    private PendingIntent pendingIntentWarning;

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
        DailyNotification();

        //Setto le notifiche per gli allarmi
        alarmManagerWarning = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intentWarning = new Intent(getApplicationContext(), Notification_receiver.class);
        intentWarning.setAction(KEY_WARNING);
        pendingIntentWarning = PendingIntent.getBroadcast(getApplicationContext(), NOTIFICATION_ID, intentWarning, PendingIntent.FLAG_UPDATE_CURRENT);
        WarningNotification();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void DailyNotification(){
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

    private void WarningNotification(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        alarmManagerReportDaily.setRepeating(android.app.AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), /*android.app.AlarmManager.INTERVAL_DAY*/ 500, pendingIntentWarning);
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
