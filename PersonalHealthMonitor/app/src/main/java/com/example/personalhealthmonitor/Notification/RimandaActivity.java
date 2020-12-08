package com.example.personalhealthmonitor.Notification;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.personalhealthmonitor.R;

import java.sql.Time;
import java.util.Calendar;

import static com.example.personalhealthmonitor.Utilities.Utility.KEY_RIMANDA;
import static com.example.personalhealthmonitor.Utilities.Utility.NOTIFICATION_ID;

public class RimandaActivity extends AppCompatActivity {

    private int timeHour;
    private int timeMinute;
    Calendar alarmClock = null;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rimanda);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
        showTimePickerDialog();

        btn = findViewById(R.id.button);
        btn.setOnClickListener(v -> {
            if(alarmClock != null) {
                AlarmManager alarmManagerRimanda = (AlarmManager) getSystemService(ALARM_SERVICE);
                Intent intentRimanda = new Intent(getApplicationContext(), Notification_receiver.class);
                intentRimanda.setAction(KEY_RIMANDA);
                PendingIntent pendingIntentRimanda = PendingIntent.getBroadcast(getApplicationContext(), NOTIFICATION_ID, intentRimanda, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManagerRimanda.setRepeating(AlarmManager.RTC_WAKEUP, alarmClock.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntentRimanda);
                Toast.makeText(getApplicationContext(), "Allarme settato", Toast.LENGTH_LONG).show();
            }
            finish();
        });


    }

    //MOSTRA L'OROLOGIO
    private void showTimePickerDialog(){
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this, (view, hourOfDay, minute) -> {
            timeHour = hourOfDay;
            timeMinute = minute;
            alarmClock = Calendar.getInstance();
            alarmClock.set(Calendar.HOUR_OF_DAY, timeHour);
            alarmClock.set(Calendar.MINUTE, timeMinute);
            alarmClock.set(Calendar.SECOND, 0);
            btn.setText(getString(R.string.rimanda_label1)+ timeHour + getString(R.string.duePunti)+ timeMinute);
        }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), true
        );
        timePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancella", (dialog, which) -> {
            if (which == DialogInterface.BUTTON_NEGATIVE){
                btn.setText(R.string.rimanda_label2);
            }
        });
        //timePickerDialog.setOnDismissListener(dialog -> btn.setText(R.string.rimanda_label2));
        timePickerDialog.show();
    }
}