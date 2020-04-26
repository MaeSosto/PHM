package com.example.heathcaremonitor_v0;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Creo i 3 bottoni del menu
    private Button button_home;
    private Button button_diario;
    private Button button_statistiche;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Inizializzo i bottoni
        button_home = findViewById(R.id.button_home);
        button_diario = findViewById(R.id.button_diario);
        button_statistiche = findViewById(R.id.button_statistiche);

        //Gestisto il click su uno dei tre bottoni nella funzione onClick
        button_home.setOnClickListener(this);
        button_diario.setOnClickListener(this);
        button_statistiche.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        //Se ho cliccato sul bottone home
        if(v == button_home){

        }

        //Se ho cliccato sul bottone diario
        else if(v == button_diario){

        }

        //Se ho cliccato sul bottone statistiche
        else if(v == button_statistiche){

        }

    }

}
