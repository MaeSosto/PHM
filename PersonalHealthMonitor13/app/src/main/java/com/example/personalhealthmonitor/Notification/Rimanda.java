package com.example.personalhealthmonitor.Notification;

import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.personalhealthmonitor.R;

import java.util.Calendar;

import static com.example.personalhealthmonitor.Notification.Alarm.NOTIFICATION_ID;

public class Rimanda extends AppCompatActivity {

    private int timeHour;
    private int timeMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rimanda);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);

        showTimePickerDialog();
    }

    private void showTimePickerDialog(){
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this, (view, hourOfDay, minute) -> {
            String Stime = hourOfDay+":"+minute;
            timeHour = hourOfDay;
            timeMinute = minute;
            TextView TXVnuovoOrario = findViewById(R.id.textView2);
            TXVnuovoOrario.setText("Verrai avvisato alle "+ String.valueOf(timeHour)+ ":"+ String.valueOf(timeMinute));
        }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), true
        );
        timePickerDialog.show();
    }
}