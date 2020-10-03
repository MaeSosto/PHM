package com.example.personalhealthmonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView testo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button bottone1, bottone2, bottone3;

        bottone1 = findViewById(R.id.button1);
        bottone2 = findViewById(R.id.button2);
        bottone3 = findViewById(R.id.button3);

        testo = findViewById(R.id.scritta);

        bottone1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testo.setText("Ciaone");
            }
        });

    }


}