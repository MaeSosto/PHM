package com.example.personalhealthmonitor.Notification;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.personalhealthmonitor.R;

import java.util.Calendar;

import static com.example.personalhealthmonitor.Notification.Alarm.NOTIFICATION_ID;

public class RimandaActivity extends AppCompatActivity {

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
            timeHour = hourOfDay;
            timeMinute = minute;
            Button btn = findViewById(R.id.button);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            btn.setText("Verrai avvisato alle "+ String.valueOf(timeHour)+ ":"+ String.valueOf(timeMinute));
            Calendar alarmClock = Calendar.getInstance();
            alarmClock.set(Calendar.HOUR_OF_DAY, timeHour);
            alarmClock.set(Calendar.MINUTE, timeMinute);
            alarmClock.set(Calendar.SECOND, 0);
            Alarm.RimandaON(alarmClock);
        }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), true
        );
        timePickerDialog.show();
    }
}