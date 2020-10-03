package com.example.testdb_6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import Database.Diario;
import Database.NewReportActivity;

public class MainActivity extends AppCompatActivity {

    public static Diario myAppDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        myAppDatabase = Room.databaseBuilder(getApplicationContext(), Diario.class ,"userdb").allowMainThreadQueries().build();

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), NewReportActivity.class);
                startActivity(intent);
            }
        });
    }
}