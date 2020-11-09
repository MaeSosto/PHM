package com.example.testmenu2;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.testmenu2.Database.DB;
import com.example.testmenu2.Database.Report;
import com.example.testmenu2.Database.ReportDao;
import com.example.testmenu2.Database.ReportViewModel;
import com.example.testmenu2.Utilities.Converters;
import com.example.testmenu2.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Database;
import androidx.room.Room;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        randomData(100);


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_diario, R.id.navigation_statistiche)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    private void randomData(int numReport){
        ReportViewModel reportViewModel = new ReportViewModel(getApplication());
        DB db = Room.databaseBuilder(getApplicationContext(), DB.class, "report_database").allowMainThreadQueries().build();
        db.clearAllTables();


        for(int i = 0; i<numReport; i++){
            Date randomDate = randomDate(Converters.StringToDate("01/01/2019"), Converters.StringToDate("31/12/2020"));

            double battito = randomDouble(60, 100);
            double pressione = randomDouble(60, 80);
            double temperatura = randomDouble(35, 38);
            double glicemia = randomDouble(60, 125);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(randomDate);
            SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
            String giorno =SDF.format(calendar.getTime());
            String ora = String.valueOf(randomDate.getHours())+":"+ String.valueOf(randomDate.getMinutes());

            Log.i("RANDOM REPORT", giorno+ ora + String.valueOf(battito)+ String.valueOf(pressione)+ String.valueOf(temperatura)+ String.valueOf(glicemia)+ giorno);
            Report report = new Report(0, Converters.StringToDate(giorno), ora, battito, temperatura, pressione, glicemia, giorno);
            reportViewModel.setReport(report);
        }

    }

    private double randomDouble(double inizio, double fine){
        boolean bool = ThreadLocalRandom.current().nextBoolean();
        if(bool) {
            double number = ThreadLocalRandom.current().nextDouble(inizio, fine);
            int aux = (int) (number * 100);
            double result = aux / 100d;//12.43
            return result;
        }
        else return  0;

    }

    /*public static Date randomDate (Date startInclusive, Date endExclusive) {
        long startMillis = startInclusive.getTime();
        long endMillis = endExclusive.getTime();
        long randomMillisSinceEpoch = ThreadLocalRandom
                .current()
                .nextLong(startMillis, endMillis);

        return new Date(randomMillisSinceEpoch);
    }

     */

    public static Date randomDate (Date d1, Date d2) {
        Date randomDate = new Date(ThreadLocalRandom.current().nextLong(d1.getTime(), d2.getTime()));
        return randomDate;
    }

}