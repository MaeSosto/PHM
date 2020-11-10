package com.example.testmenu2.impostazioni;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.example.testmenu2.R;

public class Impostazioni extends AppCompatActivity {
    public static FragmentManager fragmentManager;
    public static FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impostazioni);

        Bundle bundle = getIntent().getExtras();
        String extra = bundle.getString("extra");

        //TODAY REPORT
        fragmentManager = getSupportFragmentManager();
        if(findViewById(R.id.fragment_container)!= null){
            if(savedInstanceState != null){
                return;
            }

            fragmentTransaction = fragmentManager.beginTransaction();
            if(extra == "importanza"){
                ImportanzaFragment importanzaFragment = new ImportanzaFragment();
                fragmentTransaction.add(R.id.fragment_container, importanzaFragment, null);
            }
            else {
                NotificheFragment notificheFragment = new NotificheFragment();
                fragmentTransaction.add(R.id.fragment_container, notificheFragment, null);
            }
            fragmentTransaction.commit();
        }

    }
}