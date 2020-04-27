package com.example.healthcaremonitor_v0;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.healthcaremonitor_v0.UI.diario.DiarioFragment;
import com.example.healthcaremonitor_v0.UI.home.HomeFragment;
import com.example.healthcaremonitor_v0.UI.statistiche.StatisticheFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        bottomNavigation = findViewById(R.id.bottom_navigation_menu);
//        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        BottomNavigationView navView = findViewById(R.id.bottom_navigation_menu);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_diario, R.id.navigation_statistiche)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

    }


    //Prendo in input un fragment e lo sostituisco al container attuale
    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            HomeFragment details = new HomeFragment();
                            //getSupportFragmentManager().beginTransaction().add(android.R.id.content, details).commit();
                            openFragment(details);
                            return true;
                        case R.id.navigation_diario:
                            DiarioFragment diario = new DiarioFragment();
                            openFragment(diario);
                            return true;
                        case R.id.navigation_statistiche:
                            //openFragment(NotificationFragment.newInstance("", ""));
                            StatisticheFragment fragment = new StatisticheFragment();
                            openFragment(fragment);
                            return true;
                    }
                    return false;
                }
            };

}
