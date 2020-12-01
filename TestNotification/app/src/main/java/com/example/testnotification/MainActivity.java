package com.example.testnotification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    public static final String CHANNEL_ID = "Daily_notification";
    public static final int NOTIFICATION_ID = 001;
    boolean alarm = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();

        Calendar calendar = Calendar.getInstance();
        //calendar.set(Calendar.HOUR_OF_DAY, 8);
        //calendar.set(Calendar.MINUTE, 25);
        //calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(getApplicationContext(), Notification_receiver.class);
        intent.setAction("MY_NOTIFICATION_MESSAGE");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 500, pendingIntent);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(alarm){
                   alarmManager.cancel(pendingIntent);
                   alarm = false;
                   Log.i("ALARM", "false");
               }
               else{
                   Calendar calendar = Calendar.getInstance();
                   alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 500, pendingIntent);
                   alarm = true;
                   Log.i("ALARM", "true");
               }
            }
        });
    }

    public void createNotificationChannel(){
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