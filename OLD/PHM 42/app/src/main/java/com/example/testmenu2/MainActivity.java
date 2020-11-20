package com.example.testmenu2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.testmenu2.Database.DB;
import com.example.testmenu2.Database.Notification;
import com.example.testmenu2.Database.NotificationViewModel;
import com.example.testmenu2.Database.Report;
import com.example.testmenu2.Database.ReportViewModel;
import com.example.testmenu2.Database.Settings;
import com.example.testmenu2.Database.SettingsViewModel;
import com.example.testmenu2.Utilities.Converters;
import com.example.testmenu2.impostazioni.Impostazioni;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends AppCompatActivity {

    public static ReportViewModel reportViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        Boolean firstRun = getSharedPreferences("preferences", MODE_PRIVATE).getBoolean("firstRun", true);

        if(firstRun) {
            randomData(100);
            getSharedPreferences("preferences", MODE_PRIVATE).edit().putBoolean("firstRun", false).apply();
        }
        reportViewModel = ViewModelProviders.of(this).get(ReportViewModel.class);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_diario, R.id.navigation_statistiche)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void openOptionsMenu() {
        super.openOptionsMenu();
        Log.i("MENU", "CLICK");
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings) {
            Intent intent = new Intent(getApplicationContext(), Impostazioni.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void randomData(int numReport){
        ReportViewModel reportViewModel = ViewModelProviders.of(this).get(ReportViewModel.class);

        for(int i = 0; i<numReport; i++){
            Date randomDate = randomDate(Converters.StringToDate("01/01/2019"), Converters.StringToDate("31/12/2020"));

            double battito = randomDouble(60, 100);
            double pressioneSistolica = randomDouble(60, 80);
            double pressioneDiastolica = randomDouble(60, 80);
            double temperatura = randomDouble(35, 38);
            double glicemia = randomDouble(60, 125);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(randomDate);
            SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
            String giorno =SDF.format(calendar.getTime());
            String ora = String.valueOf(randomDate.getHours())+":"+ String.valueOf(randomDate.getMinutes());

            Report report = new Report(0, Converters.StringToDate(giorno), ora, battito, temperatura, pressioneSistolica, pressioneDiastolica, glicemia, giorno);
            reportViewModel.setReport(report);
        }

        SettingsViewModel settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        Settings tmpSettings = new Settings(0, "Battito", 2, null, null, 0);
        settingsViewModel.setSettings(tmpSettings);
        tmpSettings = new Settings(0, "PressioneDiastolica", 2, null, null, 0);
        settingsViewModel.setSettings(tmpSettings);
        tmpSettings = new Settings(0, "PressioneSistolica", 2, null, null, 0);
        settingsViewModel.setSettings(tmpSettings);
        tmpSettings = new Settings(0, "Temperatura", 2, null, null, 0);
        settingsViewModel.setSettings(tmpSettings);
        tmpSettings = new Settings(0, "Glicemia", 2, null, null, 0);
        settingsViewModel.setSettings(tmpSettings);

        NotificationViewModel notificationViewModel = ViewModelProviders.of(this).get(NotificationViewModel.class);
        Notification tmpNotification = new Notification(0, true, Calendar.getInstance().getTime());
        notificationViewModel.setNotification(tmpNotification);
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

    public static Date randomDate (Date d1, Date d2) {
        Date randomDate = new Date(ThreadLocalRandom.current().nextLong(d1.getTime(), d2.getTime()));
        return randomDate;
    }
}