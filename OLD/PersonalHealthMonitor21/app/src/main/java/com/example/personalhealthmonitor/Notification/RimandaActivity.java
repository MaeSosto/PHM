package com.example.personalhealthmonitor.Notification;

import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.personalhealthmonitor.R;
import java.util.Calendar;
import static com.example.personalhealthmonitor.Utilities.Utility.NOTIFICATION_ID;

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

    //MOSTRA L'OROLOGIO
    private void showTimePickerDialog(){
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this, (view, hourOfDay, minute) -> {
            timeHour = hourOfDay;
            timeMinute = minute;
            Button btn = findViewById(R.id.button);
            btn.setOnClickListener(v -> finish());
            btn.setText(getString(R.string.rimanda_label1)+ timeHour + getString(R.string.duePunti)+ timeMinute);
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