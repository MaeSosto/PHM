package com.example.firststarttest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      Boolean firstRun = getSharedPreferences("preferences", MODE_PRIVATE).getBoolean("firstRun", true);

      if(firstRun) {
          firstStart();
          getSharedPreferences("preferences", MODE_PRIVATE).edit().putBoolean("firstRun", false).apply();
      }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("firstStart", false);
    }

    private void firstStart() {
        Toast.makeText(getApplicationContext(), "PRIMO AVVIO", Toast.LENGTH_LONG).show();
    }
}