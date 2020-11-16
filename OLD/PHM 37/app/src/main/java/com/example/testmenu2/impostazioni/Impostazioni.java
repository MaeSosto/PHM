package com.example.testmenu2.impostazioni;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.testmenu2.Database.Notification;
import com.example.testmenu2.Database.NotificationViewModel;
import com.example.testmenu2.Database.Settings;
import com.example.testmenu2.Database.SettingsViewModel;
import com.example.testmenu2.MainActivity;
import com.example.testmenu2.R;
import com.example.testmenu2.Utilities.Converters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.Date;
import java.util.List;

public class Impostazioni extends AppCompatActivity implements  DatePickerDialog.OnDateSetListener{

    private Settings battito, pressione, temperatura, glicemia;
    private Notification notification;
    private SettingsViewModel settingsViewModel;
    private NotificationViewModel notificationViewModel;
    private int timeHour, timeMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impostazioni);

        Button BTNsalva = findViewById(R.id.BTNsave);

        //DATI BATTITO
        SeekBar battito_seekbar = findViewById(R.id.battito_seekbar);
        TextView TXVbattito_seekbar = findViewById(R.id.TXVbattito_seekbar);
        Button BTNbattito_data1 = findViewById(R.id.BTNbattito_data1);
        Button BTNbattito_data2 = findViewById(R.id.BTNbattito_data2);
        EditText ETbattito = findViewById(R.id.ETbattito_limite);
        LinearLayout LLbattito = findViewById(R.id.LLbattito);
        LLbattito.setVisibility(View.GONE);

        //DATI PRESSIONE
        SeekBar pressione_seekbar = findViewById(R.id.pressione_seekbar);
        TextView TXVpressione_seekbar = findViewById(R.id.TXVpressione_seekbar);
        Button BTNpressione_data1 = findViewById(R.id.BTNpressione_data1);
        Button BTNpressione_data2 = findViewById(R.id.BTNpressione_data2);
        EditText ETpressione = findViewById(R.id.ETpressione_limite);
        LinearLayout LLpressione = findViewById(R.id.LLpressione);
        LLpressione.setVisibility(View.GONE);

        //DATI TEMPERATURA
        SeekBar temperatura_seekbar = findViewById(R.id.temperatura_seekbar);
        TextView TXVtemperatura_seekbar = findViewById(R.id.TXVtemperatura_seekbar);
        Button BTNtemperatura_data1 = findViewById(R.id.BTNtemperatura_data1);
        Button BTNtemperatura_data2 = findViewById(R.id.BTNtemperatura_data2);
        EditText ETtemperatura = findViewById(R.id.ETtemperatura_limite);
        LinearLayout LLtemperatura = findViewById(R.id.LLtemperatura);
        LLtemperatura.setVisibility(View.GONE);

        //DATI GLICEMIA
        SeekBar glicemia_seekbar = findViewById(R.id.glicemia_seekbar);
        TextView TXVglicemia_seekbar = findViewById(R.id.TXVglicemia_seekbar);
        Button BTNglicemia_data1 = findViewById(R.id.BTNglicemia_data1);
        Button BTNglicemia_data2 = findViewById(R.id.BTNglicemia_data2);
        EditText ETglicemia = findViewById(R.id.ETglicemia_limite);
        LinearLayout LLglicemia = findViewById(R.id.LLglicemia);
        LLglicemia.setVisibility(View.GONE);

        //DATI NOTIFICHE
        Switch notification_switch = findViewById(R.id.notification_switch);
        Button BTNnotification = findViewById(R.id.BTNnotifica);
        LinearLayout LLnotification = findViewById(R.id.LLnotifiche);
        LLnotification.setVisibility(View.GONE);


        //PRENDO I SETTING DAL DATABASE
        settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        LiveData<List<Settings>> mSettings = settingsViewModel.getAllSettings();
        mSettings.observe(this, new Observer<List<Settings>>() {
            @Override
            public void onChanged(List<Settings> settings) {

                for (int i = 0; i<settings.size(); i++){
                    Settings tmpSetting = settings.get(i);
                    String value = tmpSetting.getValore();
                    switch (value){
                        case "Battito":
                            battito = tmpSetting;
                            setSettings(battito, battito_seekbar, TXVbattito_seekbar, ETbattito, BTNbattito_data1, BTNbattito_data2);
                            break;
                        case "Pressione":
                            pressione = tmpSetting;
                            setSettings(pressione, pressione_seekbar, TXVpressione_seekbar, ETpressione, BTNpressione_data1, BTNpressione_data2);
                            break;
                        case "Temperatura":
                            temperatura = tmpSetting;
                            setSettings(temperatura, temperatura_seekbar, TXVtemperatura_seekbar, ETtemperatura, BTNtemperatura_data1, BTNtemperatura_data2);
                            break;
                        case "Glicemia":
                            glicemia = tmpSetting;
                            setSettings(glicemia, glicemia_seekbar, TXVglicemia_seekbar, ETglicemia, BTNglicemia_data1, BTNglicemia_data2);
                            break;
                    }
                }

            }
        });

        notificationViewModel = ViewModelProviders.of(this).get(NotificationViewModel.class);
        LiveData<Notification> LDnotification = notificationViewModel.getNotification();
        LDnotification.observe(this, new Observer<Notification>() {
            @Override
            public void onChanged(Notification tmpnotification) {
                notification = tmpnotification;
                setNotification(notification, notification_switch, LLnotification, BTNnotification);
            }
        });


        //BOTTONE SALVATAGGIO IMPOSTAZIONI
        BTNsalva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ok = true;
                saveNotification(notification, BTNnotification, notification_switch);
                if(!saveSettings(battito, battito_seekbar, ETbattito, BTNbattito_data1, BTNbattito_data2)){
                    Toast.makeText(getApplicationContext(), "Errore nella gestione delle date del battito cardiaco", Toast.LENGTH_LONG).show();
                    ok = false;
                }
                if(!saveSettings(pressione, pressione_seekbar, ETpressione, BTNpressione_data1, BTNpressione_data2)){
                    Toast.makeText(getApplicationContext(), "Errore nella gestione delle date della pressione arteriosa", Toast.LENGTH_LONG).show();
                    ok = false;
                }
                if(!saveSettings(temperatura, temperatura_seekbar, ETtemperatura, BTNtemperatura_data1, BTNtemperatura_data2)){
                    Toast.makeText(getApplicationContext(), "Errore nella gestione delle date della temperatura corporea", Toast.LENGTH_LONG).show();
                    ok = false;
                }
                if(!saveSettings(glicemia, glicemia_seekbar, ETglicemia, BTNglicemia_data1, BTNglicemia_data2)){
                    Toast.makeText(getApplicationContext(), "Errore nella gestione delle date dell'indice glicemico", Toast.LENGTH_LONG).show();
                    ok = false;
                }
                if(ok){
                    settingsViewModel.updateSettings(battito);
                    settingsViewModel.updateSettings(pressione);
                    settingsViewModel.updateSettings(temperatura);
                    settingsViewModel.updateSettings(glicemia);
                    notificationViewModel.updateNotification(notification);
                    Toast.makeText(getApplicationContext(), "Impostazioni salvate", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });

        //NOTIFICHE
        notification_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Calendar calendar = Calendar.getInstance();
                    timeHour = calendar.getTime().getHours();
                    timeMinute = calendar.getTime().getMinutes();
                    //calendar.set(calendar.getTime().getYear(), calendar.getTime().getMonth(), calendar.getTime().getDate(), timeHour, timeMinute);
                    BTNnotification.setText(timeHour + ":"+ timeMinute);
                    LLnotification.setVisibility(View.VISIBLE);
                }
                else LLnotification.setVisibility(View.GONE);
            }
        });

        BTNnotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(BTNnotification);
            }
        });

        //BATTITO
        battito_seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String s = String.valueOf(seekBar.getProgress());
                TXVbattito_seekbar.setText(s);
                battito.setImportanza(seekBar.getProgress());
                showRange(battito, seekBar.getProgress(), LLbattito, BTNbattito_data1, BTNbattito_data2);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        BTNbattito_data1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(BTNbattito_data1);
            }
        });

        BTNbattito_data2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(BTNbattito_data2);
            }
        });

        //PRESSIONE
        pressione_seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String s = String.valueOf(seekBar.getProgress());
                TXVpressione_seekbar.setText(s);
                pressione.setImportanza(seekBar.getProgress());
                showRange(pressione, seekBar.getProgress(), LLpressione, BTNpressione_data1, BTNpressione_data2);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        BTNpressione_data1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(BTNpressione_data1);
            }
        });

        BTNpressione_data2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(BTNpressione_data2);
            }
        });

        //TEMPERATURA
        temperatura_seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String s = String.valueOf(seekBar.getProgress());
                TXVtemperatura_seekbar.setText(s);
                temperatura.setImportanza(seekBar.getProgress());
                showRange(temperatura, seekBar.getProgress(), LLtemperatura, BTNtemperatura_data1, BTNtemperatura_data2);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        BTNtemperatura_data1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(BTNtemperatura_data1);
            }
        });

        BTNtemperatura_data2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(BTNtemperatura_data2);
            }
        });

        //GLICEMIA
        glicemia_seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String s = String.valueOf(seekBar.getProgress());
                TXVglicemia_seekbar.setText(s);
                glicemia.setImportanza(seekBar.getProgress());
                showRange(glicemia, seekBar.getProgress(), LLglicemia, BTNglicemia_data1, BTNglicemia_data2);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        BTNglicemia_data1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(BTNglicemia_data1);
            }
        });

        BTNglicemia_data2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(BTNglicemia_data2);
            }
        });

    }

    private void showDatePickerDialog(Button button){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String Sdate = dayOfMonth+"-"+(month+1)+"-"+year;
                button.setText(Sdate);
            }
        },
                Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showTimePickerDialog(Button button){
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String Stime = hourOfDay+":"+minute;
                button.setText(Stime);
                timeHour = hourOfDay;
                timeMinute = minute;
            }
        },Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), true
        );
        timePickerDialog.show();
    }

    private boolean saveSettings(Settings settings, SeekBar seekBar, EditText editText, Button button1, Button button2){
        settings.setImportanza(seekBar.getProgress());
        if(settings.getImportanza() > 2) {
            settings.setLimite(Double.parseDouble(editText.getText().toString()));
            try {
                settings.setInizio(new SimpleDateFormat("dd-MM-yyyy").parse(button1.getText().toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                settings.setFine(new SimpleDateFormat("dd-MM-yyyy").parse(button2.getText().toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return settings.getInizio() != null && settings.getFine() != null && (Converters.DateToLong(settings.getInizio()) <= Converters.DateToLong(settings.getFine()));
        }
        else{
            settings.setInizio(null);
            settings.setFine(null);
            settings.setLimite(0);
        }
        return true;
    }

    private void saveNotification(Notification notification, Button button, Switch s){
        notification.setEnable(s.isChecked());
        if(notification.isEnable()) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(calendar.getTime().getYear(), calendar.getTime().getMonth(), calendar.getTime().getDate(), timeHour, timeMinute);

            Log.i("ORARIO", String.valueOf(calendar.getTime()));
            notification.setHour(calendar.getTime());
        }
    }

    private  void setNotification(Notification notification, Switch notification_switch, LinearLayout LLnotification, Button BTNnotification){
        notification_switch.setChecked(notification.isEnable());
        if(notification.isEnable()){
            LLnotification.setVisibility(View.VISIBLE);
            Date date = notification.getHour();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            timeHour = calendar.getTime().getHours();
            timeMinute = calendar.getTime().getMinutes();
            BTNnotification.setText(String.valueOf(timeHour+ ":" + timeMinute));
            Log.i("ORARIO NOTIFICA", String.valueOf(calendar.getTime()));
        }
    }

    private void setSettings(Settings settings, SeekBar seekBar, TextView textView, EditText editText, Button button1, Button button2){
        seekBar.setProgress(settings.getImportanza());
        textView.setText(String.valueOf(settings.getImportanza()));
        if(settings.getImportanza()>2){
            editText.setText(String.valueOf(settings.getLimite()));
            button1.setText(Converters.DateToString(settings.getInizio()));
            button2.setText(Converters.DateToString(settings.getFine()));
        }
    }


    private void showRange(Settings settings, int val, LinearLayout linearLayout, Button button1, Button button2) {
        if(val>2) {
            linearLayout.setVisibility(View.VISIBLE);
            if(settings.getLimite() == 0){
                button1.setText(Converters.DateToString(Calendar.getInstance().getTime()));
                button2.setText(Converters.DateToString(Calendar.getInstance().getTime()));
            }
        }
        else linearLayout.setVisibility(View.GONE);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }
}