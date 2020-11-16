package com.example.impostazioni;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.example.impostazioni.Database.Settings;
import com.example.impostazioni.Database.SettingsViewModel;
import com.example.impostazioni.Utilities.Converters;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements  DatePickerDialog.OnDateSetListener{

    private SimpleDateFormat SDF;
    private TextView TXVbattito_seekbar;
    private SeekBar battito_seekbar;
    private Button BTNsalva, BTNbattito_data1, BTNbattito_data2;
    private LinearLayout LLbattito;
    private SettingsViewModel settingsViewModel;
    private LiveData<List<Settings>> mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impostazioni);

        SDF = new SimpleDateFormat("dd/MM/yyyy");

        BTNsalva = findViewById(R.id.BTNsave);
        battito_seekbar = findViewById(R.id.battito_seekbar);
        TXVbattito_seekbar = findViewById(R.id.TXVbattito_seekbar);
        BTNbattito_data1 = findViewById(R.id.BTNbattito_data1);
        BTNbattito_data2 = findViewById(R.id.BTNbattito_data2);
        LLbattito = findViewById(R.id.LLbattito);

        LLbattito.setVisibility(View.GONE);

        settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        mSettings = settingsViewModel.getAllSettings();
        mSettings.observe(this, new Observer<List<Settings>>() {
            @Override
            public void onChanged(List<Settings> settings) {
                if(settings.size() == 0){
                    newSettings();
                    mSettings = settingsViewModel.getAllSettings();
                }
                setSettings(settings);
            }
        });

        BTNsalva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Settings battito_settings = settingsViewModel.getSetting("Battito");
                battito_settings.setImportanza(battito_seekbar.getProgress());
                settingsViewModel.updateSettings(battito_settings);
            }
        });

        battito_seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String s = String.valueOf(seekBar.getProgress());
                TXVbattito_seekbar.setText(s);
                showRange("Battito", seekBar.getProgress());
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
               showDatePickerDialog();
            }
        });
    }

    private void showRange(String string, int val) {
        switch(string){
            case "Battito":
                if(val>2) LLbattito.setVisibility(View.VISIBLE);
                else LLbattito.setVisibility(View.GONE);
                break;

        }
    }

    private void showDatePickerDialog(){
       DatePickerDialog datePickerDialog = new DatePickerDialog(
               this,
               this,
               Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
       );
       datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = dayOfMonth+"-"+(month+1)+"-"+year;
        Log.i("DATA", date);
    }

    private void newSettings(){
        Settings newSettings = new Settings(0, "Battito", 2, null, null, 0);
        settingsViewModel.setSettings(newSettings);
        newSettings = new Settings(0, "Pressione", 2, null, null, 0);
        settingsViewModel.setSettings(newSettings);
        newSettings = new Settings(0, "Temperatura", 2, null, null, 0);
        settingsViewModel.setSettings(newSettings);
        newSettings = new Settings(0, "Glicemia", 2, null, null, 0);
        settingsViewModel.setSettings(newSettings);
    }

    private void setSettings(List<Settings> settings){
        for(int i= 0; i< settings.size(); i++){
            Settings s = settings.get(i);
            switch (s.getValore()){
                case "Battito":
                    battito_seekbar.setProgress(s.getImportanza());

            }
        }
    }
}