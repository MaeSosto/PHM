package com.example.healthcaremonitor_v0;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.healthcaremonitor_v0.UI.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigation = findViewById(R.id.bottom_navigation_menu);
//        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
//        openFragment(HomeFragment.newInstance("", ""));
    }

    //Cambia il fragment a seconda dell'input passato
    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

//    BottomNavigationView.OnNavigationItemReselectedListener navigationItemSelectedListener =
//            new BottomNavigationView.OnNavigationItemReselectedListener() {
//                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                    switch (item.getItemId()) {
//                        case R.id.navigation_home:
//    //                        openFragment(HomeFragment.newInstance("", ""));
//                            return true;
//                        case R.id.navigation_diario:
//    //                        openFragment(SmsFragment.newInstance("", ""));
//                            return true;
//                        case R.id.navigation_statistiche:
//    //                        openFragment(NotificationFragment.newInstance("", ""));
//                            return true;
//                    }
//                    return false;
//                }
//            };

}
