package com.example.personalhealthmonitor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.example.personalhealthmonitor.Database.NotificationViewModel;
import com.example.personalhealthmonitor.Database.ReportViewModel;
import com.example.personalhealthmonitor.Database.SettingsViewModel;
import com.example.personalhealthmonitor.Notification.Alarm;
import com.example.personalhealthmonitor.Settings.SettingsActivity;
import com.example.personalhealthmonitor.Utilities.Utility;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import static com.example.personalhealthmonitor.Utilities.Utility.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Menu basso
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_diario, R.id.navigation_statistiche)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        //SETTA IL NAV VIEW IN BASE ALL'ORIENTAMENTO DEL DISPOSITIVO
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            BottomNavigationView navView = findViewById(R.id.bottom_nav_view);
            NavigationUI.setupWithNavController(navView, navController);
        }
        else{
            NavigationView navView = findViewById(R.id.nav_view);
            NavigationUI.setupWithNavController(navView, navController);
        }

        filtro = new MutableLiveData<>();
        filtro.setValue(1);
        reportViewModel = ViewModelProviders.of(this).get(ReportViewModel.class);
        settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        notificationViewModel = ViewModelProviders.of(this).get(NotificationViewModel.class);


        //PRIMO AVVIO
        boolean firstRun = getSharedPreferences("preferences", MODE_PRIVATE).getBoolean("firstRun", true);
        if(firstRun) {
            Utility.randomData(100);
            //startService(new Intent( this,Alarm.class));
            getSharedPreferences("preferences", MODE_PRIVATE).edit().putBoolean("firstRun", false).apply();
        }
        startService(new Intent( this, Alarm.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent i = new Intent(this, Alarm.class);
        stopService(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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