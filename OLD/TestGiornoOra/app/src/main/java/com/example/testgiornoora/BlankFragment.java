package com.example.testgiornoora;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;


public class BlankFragment extends Fragment {

    Button button;

    public BlankFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        button.findViewById(R.id.BTN);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String giorno = java.text.DateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALIAN).format(new Date());
                final String ora = java.text.DateFormat.getTimeInstance(DateFormat.LONG, Locale.ITALIAN).format(new Date());

                Log.i("Giorno", giorno);
                Log.i("Ora", ora);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }
}