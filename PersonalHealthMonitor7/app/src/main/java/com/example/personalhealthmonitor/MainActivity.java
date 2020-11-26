package com.example.personalhealthmonitor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.personalhealthmonitor.Database.ReportViewModel;
import com.example.personalhealthmonitor.Database.SettingsViewModel;
import com.example.personalhealthmonitor.Settings.SettingsActivity;
import com.example.personalhealthmonitor.Statistiche.StatisticheFragment;
import com.example.personalhealthmonitor.Utilities.Utility;
import com.google.android.material.bottomnavigation.BottomNavigationView;



public class MainActivity extends AppCompatActivity {

    public static ReportViewModel reportViewModel;
    public static SettingsViewModel settingsViewModel;
    private static StatisticheFragment statisticheFragment;
    public static final String KEY_BATTITO = "report_battito";
    public static final String KEY_TEMPERATURA = "report_temperatura";
    public static final String KEY_PRESSIONESIS = "report_pressione_sistolica";
    public static final String KEY_PRESSIONEDIA = "report_pressione_diastolica";
    public static final String KEY_GLICEMIAMAX = "report_glicemia_max";
    public static final String KEY_GLICEMIAMIN = "report_glicemia_min";
    public static final String KEY_NOTIFICATION = "report_notification";
    public static final int BATTITORANGE1 = 40;
    public static final int BATTITORANGE2 = 180;
    public static final int TEMPERATURARANGE1 = 35;
    public static final int TEMPERATURARANGE2 = 43;
    public static final int PRESSIONERANGE1 = 40;
    public static final int PRESSIONERANGE2 = 180;
    public static final int GLICEMIARANGE1 = 50;
    public static final int GLICEMIARANGE2 = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        reportViewModel = ViewModelProviders.of(this).get(ReportViewModel.class);
        settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        boolean firstRun = getSharedPreferences("preferences", MODE_PRIVATE).getBoolean("firstRun", true);
        if(firstRun) {
            Utility.randomData(100);
            getSharedPreferences("preferences", MODE_PRIVATE).edit().putBoolean("firstRun", false).apply();
        }

        //Menu basso
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
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}