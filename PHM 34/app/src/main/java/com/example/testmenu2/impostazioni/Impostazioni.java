package com.example.testmenu2.impostazioni;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;


import com.example.testmenu2.R;
import com.example.testmenu2.Utilities.SetHour;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Impostazioni extends AppCompatActivity{

    TextView TXVbattito_seekbar;
    SeekBar battito_seekbar;
    Button battito_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impostazioni);
        battito_seekbar = findViewById(R.id.battito_seekbar);
        TXVbattito_seekbar = findViewById(R.id.TXVbattito_seekbar);
        battito_time = findViewById(R.id.battito_time);

        battito_seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String s = String.valueOf(seekBar.getProgress());
                TXVbattito_seekbar.setText(s);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        battito_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendario = Calendar.getInstance();

                DatePickerDialog datePickerDialog = new DatePickerDialog(getApplicationContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendario.set(year, month, dayOfMonth);
                        Log.i("CALENDARIO", String.valueOf(calendario.getTime()));
                    }
                },2020, 11, 10);
                datePickerDialog.updateDate(calendario.getTime().getYear(), calendario.getTime().getMonth(), calendario.getTime().getDate());
                datePickerDialog.show();
            }
        });
    }


}