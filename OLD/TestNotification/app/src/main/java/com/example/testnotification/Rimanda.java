package com.example.testnotification;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

public class Rimanda extends AppCompatActivity {

    private int timeHour;
    private int timeMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rimanda);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(MainActivity.NOTIFICATION_ID);

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