package com.example.testdb_4;

import android.content.Context;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import Database.Report;

public class FirstFragment extends Fragment {
    List<Report> reportList;
    ListView listView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        //Creo la view
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        //Prendo la view della lista
        //listView = view.findViewById(R.id.lista_report);

        //ArrayAdapter<Report> arrayList = new ArrayAdapter(getContext(), R.layout.fragment_first, reportList);
        //listView.setAdapter(arrayList);


        //inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //listView.setAdapter(adapter);

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_first, container, false);

        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Snackbar.make(view, "Ho creato la view del fragment 1", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();



        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }
}