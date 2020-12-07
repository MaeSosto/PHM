package com.example.personalhealthmonitor.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.personalhealthmonitor.Database.Notification;
import com.example.personalhealthmonitor.Database.Settings;
import com.example.personalhealthmonitor.Notification.Alarm;
import com.example.personalhealthmonitor.Notification.Notification_receiver;
import com.example.personalhealthmonitor.R;
import com.example.personalhealthmonitor.Utilities.Converters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.personalhealthmonitor.MainActivity.KEY_BATTITO;
import static com.example.personalhealthmonitor.MainActivity.KEY_GLICEMIAMAX;
import static com.example.personalhealthmonitor.MainActivity.KEY_GLICEMIAMIN;
import static com.example.personalhealthmonitor.MainActivity.KEY_PRESSIONEDIA;
import static com.example.personalhealthmonitor.MainActivity.KEY_PRESSIONESIS;
import static com.example.personalhealthmonitor.MainActivity.KEY_TEMPERATURA;
import static com.example.personalhealthmonitor.MainActivity.SDF;
import static com.example.personalhealthmonitor.MainActivity.notificationViewModel;
import static com.example.personalhealthmonitor.MainActivity.reportViewModel;
import static com.example.personalhealthmonitor.MainActivity.settingsViewModel;

public class SettingsActivity extends AppCompatActivity implements  DatePickerDialog.OnDateSetListener{

    private SeekBar battito_seekbar, pressioneSistolica_seekbar, pressioneDiastolica_seekbar, temperatura_seekbar, glicemiamax_seekbar, glicemiamin_seekbar;
    private TextView TXVbattito_seekbar, TXVpressioneSistolica_seekbar, TXVpressioneDiastolica_seekbar, TXVtemperatura_seekbar, TXVglicemiamax_seekbar, TXVglicemiamin_seekbar;
    private Button BTNsalva, BTNbattito_data1, BTNbattito_data2, BTNpressioneSistolica_data1, BTNpressioneSistolica_data2, BTNpressioneDiastolica_data1, BTNpressioneDiastolica_data2, BTNtemperatura_data1, BTNtemperatura_data2, BTNglicemiamax_data1, BTNglicemiamax_data2, BTNglicemiamin_data1, BTNglicemiamin_data2, BTNnotification;
    private EditText ETbattito, ETpressioneSistolica, ETpressioneDiastolica, ETtemperatura, ETglicemiamax, ETglicemiamin;
    private LinearLayout LLnotification, LLbattito, LLpressioneSistolica, LLpressioneDiastolica, LLtemperatura, LLglicemiamax, LLglicemiamin;
    private Switch notification_switch;
    private Settings battito, pressioneSistolica, pressioneDiastolica, temperatura, glicemiamax, glicemiamin;
    private Notification notification;
    private int timeHour, timeMinute; //mi serve per salvare la data e l'ora delle notifiche

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        //Collego la viewmodel alla view
        setViewModel();;

        //PRENDO I SETTING DAL DATABASE
        settingsViewModel.getAllSettings().observe(this, this::getSettingsFromDB);

        //BOTTONE SALVATAGGIO IMPOSTAZIONI
        BTNsalva.setOnClickListener(v -> {saveSettings();});

        //NOTIFICHE
        notification_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                Calendar calendar = Calendar.getInstance();
                timeHour = calendar.getTime().getHours();
                timeMinute = calendar.getTime().getMinutes();
                //calendar.set(calendar.getTime().getYear(), calendar.getTime().getMonth(), calendar.getTime().getDate(), timeHour, timeMinute);
                BTNnotification.setText(timeHour + ":"+ timeMinute);
                LLnotification.setVisibility(View.VISIBLE);
            }
            else LLnotification.setVisibility(View.GONE);
        });
        BTNnotification.setOnClickListener(v -> showTimePickerDialog(BTNnotification));

        //BATTITO
        battito_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                showRange(battito, TXVbattito_seekbar, seekBar.getProgress(), LLbattito, BTNbattito_data1, BTNbattito_data2);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        BTNbattito_data1.setOnClickListener(v -> showDatePickerDialog(BTNbattito_data1));
        BTNbattito_data2.setOnClickListener(v -> showDatePickerDialog(BTNbattito_data2));

        //PRESSIONE SISTOLICA
        pressioneSistolica_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                showRange(pressioneSistolica, TXVpressioneSistolica_seekbar, seekBar.getProgress(), LLpressioneSistolica, BTNpressioneSistolica_data1, BTNpressioneSistolica_data2);}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        BTNpressioneSistolica_data1.setOnClickListener(v -> showDatePickerDialog(BTNpressioneSistolica_data1));
        BTNpressioneSistolica_data2.setOnClickListener(v -> showDatePickerDialog(BTNpressioneSistolica_data2));

        //PRESSIONE Diastolica
        pressioneDiastolica_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                showRange(pressioneDiastolica, TXVpressioneDiastolica_seekbar, seekBar.getProgress(), LLpressioneDiastolica, BTNpressioneDiastolica_data1, BTNpressioneDiastolica_data2);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        BTNpressioneDiastolica_data1.setOnClickListener(v -> showDatePickerDialog(BTNpressioneDiastolica_data1));
        BTNpressioneDiastolica_data2.setOnClickListener(v -> showDatePickerDialog(BTNpressioneDiastolica_data2));

        //TEMPERATURA
        temperatura_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                showRange(temperatura, TXVtemperatura_seekbar, seekBar.getProgress(), LLtemperatura, BTNtemperatura_data1, BTNtemperatura_data2); }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        BTNtemperatura_data1.setOnClickListener(v -> showDatePickerDialog(BTNtemperatura_data1));
        BTNtemperatura_data2.setOnClickListener(v -> showDatePickerDialog(BTNtemperatura_data2));

        //GLICEMIA MAX
        glicemiamax_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                showRange(glicemiamax, TXVglicemiamax_seekbar, seekBar.getProgress(), LLglicemiamax, BTNglicemiamax_data1, BTNglicemiamax_data2);}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        BTNglicemiamax_data1.setOnClickListener(v -> showDatePickerDialog(BTNglicemiamax_data1));
        BTNglicemiamax_data2.setOnClickListener(v -> showDatePickerDialog(BTNglicemiamax_data2));


        //GLICEMIA
        glicemiamin_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                showRange(glicemiamin, TXVglicemiamin_seekbar, seekBar.getProgress(), LLglicemiamin, BTNglicemiamin_data1, BTNglicemiamin_data2); }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        BTNglicemiamin_data1.setOnClickListener(v -> showDatePickerDialog(BTNglicemiamin_data1));
        BTNglicemiamin_data2.setOnClickListener(v -> showDatePickerDialog(BTNglicemiamin_data2));
    }

    private void showDatePickerDialog(Button button){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, (view, year, month, dayOfMonth) -> {
            String Sdate = dayOfMonth+"/"+(month+1)+"/"+year;
            button.setText(Sdate);
        },
                Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showTimePickerDialog(Button button){
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this, (view, hourOfDay, minute) -> {
            String Stime = hourOfDay+":"+minute;
            button.setText(Stime);
            timeHour = hourOfDay;
            timeMinute = minute;
        },Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), true
        );
        timePickerDialog.show();
    }
    private void showRange(Settings settings, TextView textView, int val, LinearLayout linearLayout, Button button1, Button button2) {
        textView.setText(String.valueOf(val));
        settings.setImportanza(val);
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
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {}

    //CONTROLLA CHE NON CI SIANO ERRORI NELL'INSERIMENTO DEI DATI
    private boolean saveSettings(Settings settings, SeekBar seekBar, EditText editText, Button button1, Button button2){
        Date inizio, fine;
        settings.setImportanza(seekBar.getProgress());
        if(settings.getImportanza() > 2) {
            //Log.i("CHECKSETTING", "importanza: "+ settings.getImportanza());
            if(editText.getText().toString().equals("")){
                //Log.i("ERROR", "VALORE LIMITE VUOTO");
                return false;
            }
            try {
                inizio = SDF.parse(button1.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
                //Log.i("ERROR", "BOTTONE 1 NON E' UNA DATA");
                return false;
            }
            try {
                fine = SDF.parse(button2.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
                //Log.i("ERROR", "BOTTONE 1 NON E' UNA DATA");
                return false;
            }
            if(Converters.DateToLong(inizio) > Converters.DateToLong(fine)){
                //Log.i("ERROR", "LA DATA DI INIZIO > FINE");
                return false;
            }
            else{
                settings.setLimite(Double.parseDouble(editText.getText().toString()));
                settings.setInizio(inizio);
                settings.setFine(fine);
            }
        }
        else{
            //Log.i("CHECKSETTING", "importanza: "+ settings.getImportanza());
            settings.setInizio(null);
            settings.setFine(null);
            settings.setLimite(0);
        }
        return true;
    }

    private void saveSettings(){
        boolean ok = true;
        if(notification_switch.isChecked()){
            notification.setStatus(true);
            notification.setOra(timeHour);
            notification.setMinuti(timeMinute);
        }
        else notification.setStatus(false);
        if(!saveSettings(battito, battito_seekbar, ETbattito, BTNbattito_data1, BTNbattito_data2)){
            Toast.makeText(getApplicationContext(), "Errore nella gestione delle date del battito cardiaco", Toast.LENGTH_LONG).show();
            ok = false;
        }
        if(!saveSettings(pressioneSistolica, pressioneSistolica_seekbar, ETpressioneSistolica, BTNpressioneSistolica_data1, BTNpressioneSistolica_data2)){
            Toast.makeText(getApplicationContext(), "Errore nella gestione delle date della pressione arteriosa", Toast.LENGTH_LONG).show();
            ok = false;
        }
        if(!saveSettings(pressioneDiastolica, pressioneDiastolica_seekbar, ETpressioneDiastolica, BTNpressioneDiastolica_data1, BTNpressioneDiastolica_data2)){
            Toast.makeText(getApplicationContext(), "Errore nella gestione delle date della pressione arteriosa", Toast.LENGTH_LONG).show();
            ok = false;
        }
        if(!saveSettings(temperatura, temperatura_seekbar, ETtemperatura, BTNtemperatura_data1, BTNtemperatura_data2)){
            Toast.makeText(getApplicationContext(), "Errore nella gestione delle date della temperatura corporea", Toast.LENGTH_LONG).show();
            ok = false;
        }
        if(!saveSettings(glicemiamax, glicemiamax_seekbar, ETglicemiamax, BTNglicemiamax_data1, BTNglicemiamax_data2)){
            Toast.makeText(getApplicationContext(), "Errore nella gestione delle date dell'indice glicemico", Toast.LENGTH_LONG).show();
            ok = false;
        }
        if(!saveSettings(glicemiamin, glicemiamin_seekbar, ETglicemiamin, BTNglicemiamin_data1, BTNglicemiamin_data2)){
            Toast.makeText(getApplicationContext(), "Errore nella gestione delle date dell'indice glicemico", Toast.LENGTH_LONG).show();
            ok = false;
        }
        if(ok){
            settingsViewModel.updateSettings(battito);
            settingsViewModel.updateSettings(pressioneSistolica);
            settingsViewModel.updateSettings(pressioneDiastolica);
            settingsViewModel.updateSettings(temperatura);
            settingsViewModel.updateSettings(glicemiamax);
            settingsViewModel.updateSettings(glicemiamin);
            notificationViewModel.updateNotification(notification);

            //new Alarm.setDailyNotification().execute();

            Toast.makeText(getApplicationContext(), "Impostazioni salvate", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    //PRENDE LE INFORMAZIONI DAL DATABASE E ASSEGNA I VALORI AGLI OGGETTI DELLA SCHERMATA
    private void getSettingsFromDB(List<Settings> settings){

        notificationViewModel.getNotification().observe(this, new Observer<Notification>() {
            @Override
            public void onChanged(Notification tmpnotification) {
                notification = tmpnotification;
                if(notification.isStatus()){
                    notification_switch.setChecked(true);
                    LLnotification.setVisibility(View.VISIBLE);
                    BTNnotification.setText(notification.getOra() + ":" + notification.getMinuti());
                }
                else notification_switch.setChecked(false);
            }
        });

        for (int i = 0; i<settings.size(); i++){
            Settings tmpSetting = settings.get(i);
            String value = tmpSetting.getValore();
            switch (value){
                case KEY_BATTITO:
                    battito = tmpSetting;
                    setSettings(battito, battito_seekbar, TXVbattito_seekbar, ETbattito, BTNbattito_data1, BTNbattito_data2);
                    break;
                case KEY_PRESSIONESIS:
                    pressioneSistolica = tmpSetting;
                    setSettings(pressioneSistolica, pressioneSistolica_seekbar, TXVpressioneSistolica_seekbar, ETpressioneSistolica, BTNpressioneSistolica_data1, BTNpressioneSistolica_data2);
                    break;
                case KEY_PRESSIONEDIA:
                    pressioneDiastolica = tmpSetting;
                    setSettings(pressioneDiastolica, pressioneDiastolica_seekbar, TXVpressioneDiastolica_seekbar, ETpressioneDiastolica, BTNpressioneDiastolica_data1, BTNpressioneDiastolica_data2);
                    break;
                case KEY_TEMPERATURA:
                    temperatura = tmpSetting;
                    setSettings(temperatura, temperatura_seekbar, TXVtemperatura_seekbar, ETtemperatura, BTNtemperatura_data1, BTNtemperatura_data2);
                    break;
                case KEY_GLICEMIAMAX:
                    glicemiamax = tmpSetting;
                    setSettings(glicemiamax, glicemiamax_seekbar, TXVglicemiamax_seekbar, ETglicemiamax, BTNglicemiamax_data1, BTNglicemiamax_data2);
                    break;
                case KEY_GLICEMIAMIN:
                    glicemiamin = tmpSetting;
                    setSettings(glicemiamin, glicemiamin_seekbar, TXVglicemiamin_seekbar, ETglicemiamin, BTNglicemiamin_data1, BTNglicemiamin_data2);
            }
        }
    }

    //COSTRUISCE I BOTTONI, LE ET E LE TXV DI OGNI SINGOLO VALORE
    private void setSettings(Settings settings, SeekBar seekBar, TextView textView, EditText editText, Button button1, Button button2){
        seekBar.setProgress(settings.getImportanza());
        textView.setText(String.valueOf(settings.getImportanza()));
        if(settings.getImportanza()>2){
            editText.setText(String.valueOf(settings.getLimite()));
            button1.setText(Converters.DateToString(settings.getInizio()));
            button2.setText(Converters.DateToString(settings.getFine()));
        }
    }


    //COLLEGA LA VIEWMODEL ALLA VIEW
    private void setViewModel(){
        BTNsalva = findViewById(R.id.BTNsave);

        //DATI NOTIFICHE
        notification_switch = findViewById(R.id.notification_switch);
        BTNnotification = findViewById(R.id.BTNnotifica);
        LLnotification = findViewById(R.id.LLnotifiche);
        LLnotification.setVisibility(View.GONE);

        //DATI BATTITO
        battito_seekbar = findViewById(R.id.battito_seekbar);
        TXVbattito_seekbar = findViewById(R.id.TXVbattito_seekbar);
        BTNbattito_data1 = findViewById(R.id.BTNbattito_data1);
        BTNbattito_data2 = findViewById(R.id.BTNbattito_data2);
        ETbattito = findViewById(R.id.ETbattito_limite);
        LLbattito = findViewById(R.id.LLbattito);
        LLbattito.setVisibility(View.GONE);

        //DATI PRESSIONE SISTOLICA
        pressioneSistolica_seekbar = findViewById(R.id.pressioneSistolica_seekbar);
        TXVpressioneSistolica_seekbar = findViewById(R.id.TXVpressioneSistolica_seekbar);
        BTNpressioneSistolica_data1 = findViewById(R.id.BTNpressioneSistolica_data1);
        BTNpressioneSistolica_data2 = findViewById(R.id.BTNpressioneSistolica_data2);
        ETpressioneSistolica = findViewById(R.id.ETpressioneSistolica_limite);
        LLpressioneSistolica = findViewById(R.id.LLpressioneSistolica);
        LLpressioneSistolica.setVisibility(View.GONE);

        //DATI PRESSIONE DIASTOLICA
        pressioneDiastolica_seekbar = findViewById(R.id.pressioneDiastolica_seekbar);
        TXVpressioneDiastolica_seekbar = findViewById(R.id.TXVpressioneDiastolica_seekbar);
        BTNpressioneDiastolica_data1 = findViewById(R.id.BTNpressioneDiastolica_data1);
        BTNpressioneDiastolica_data2 = findViewById(R.id.BTNpressioneDiastolica_data2);
        ETpressioneDiastolica = findViewById(R.id.ETpressioneDiastolica_limite);
        LLpressioneDiastolica = findViewById(R.id.LLpressioneDiastolica);
        LLpressioneDiastolica.setVisibility(View.GONE);

        //DATI TEMPERATURA
        temperatura_seekbar = findViewById(R.id.temperatura_seekbar);
        TXVtemperatura_seekbar = findViewById(R.id.TXVtemperatura_seekbar);
        BTNtemperatura_data1 = findViewById(R.id.BTNtemperatura_data1);
        BTNtemperatura_data2 = findViewById(R.id.BTNtemperatura_data2);
        ETtemperatura = findViewById(R.id.ETtemperatura_limite);
        LLtemperatura = findViewById(R.id.LLtemperatura);
        LLtemperatura.setVisibility(View.GONE);

        //DATI GLICEMIA MAX
        glicemiamax_seekbar = findViewById(R.id.glicemiaMax_seekbar);
        TXVglicemiamax_seekbar = findViewById(R.id.TXVglicemiaMax_seekbar);
        BTNglicemiamax_data1 = findViewById(R.id.BTNglicemiaMax_data1);
        BTNglicemiamax_data2 = findViewById(R.id.BTNglicemiaMax_data2);
        ETglicemiamax = findViewById(R.id.ETglicemia_limiteMax);
        LLglicemiamax = findViewById(R.id.LLglicemiaMax);
        LLglicemiamax.setVisibility(View.GONE);

        //DATI GLICEMIA
        glicemiamin_seekbar = findViewById(R.id.glicemiaMin_seekbar);
        TXVglicemiamin_seekbar = findViewById(R.id.TXVglicemiaMin_seekbar);
        BTNglicemiamin_data1 = findViewById(R.id.BTNglicemiaMin_data1);
        BTNglicemiamin_data2 = findViewById(R.id.BTNglicemiaMin_data2);
        ETglicemiamin = findViewById(R.id.ETglicemiaMin_limite);
        LLglicemiamin = findViewById(R.id.LLglicemiaMin);
        LLglicemiamin.setVisibility(View.GONE);
    }
}