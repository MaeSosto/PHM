package com.example.testperiodo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yy");
        String giorno = SDF.format(calendar.getTime());
        //Log.i("OGGI", giorno);
        Log.i("PRIMO SETTIMANA", PrimoGiornoSettimana());
        //Log.i("ULTIMO SETTIMANA", UltimoGiornoSettimana());
        /*
        Log.i("PRIMO MESE", PrimoGiorno(Calendar.DAY_OF_MONTH));
        Log.i("ULTIMO MESE", UltimoGiorno(Calendar.DAY_OF_MONTH));
        Log.i("PRIMO ANNO", PrimoGiorno(Calendar.DAY_OF_YEAR));
        Log.i("ULTIMO ANNO", UltimoGiorno(Calendar.DAY_OF_YEAR));

         */
    }

    public String PrimoGiornoSettimana (){
        Calendar calendar = Calendar.getInstance();

        calendar.set(2020, 9, 8);
        SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yy");
        calendar.set(calendar.DAY_OF_WEEK, calendar.MONDAY);
        calendar.add(Calendar.MONTH, calendar.WEEK_OF_MONTH);
        //calendar.set(calendar.DAY_OF_WEEK,calendar.getActualMinimum(calendar.DAY_OF_WEEK));
        String giorno = SDF.format(calendar.getTime());
        return giorno;
    }

    public String UltimoGiornoSettimana(){
        Calendar calendar = Calendar.getInstance(new Locale("en","UK"));
        calendar.set(2020, 10, 1);
        SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yy");
        calendar.set(calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calendar.add(calendar.DAY_OF_WEEK, 6);
        String giorno = SDF.format(calendar.getTime());
        return giorno;
    }



}

