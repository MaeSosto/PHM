package com.example.phm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import Database.Report;
import Database.ReportViewModel;
import UI.NewReportActivity;
import UI.ReportListAdapter;
import UI.TodayReport;
import Utilities.OnSwipeTouchListener;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //VARIABILI
    public static ReportViewModel reportViewModel;
    public static ReportListAdapter reportListAdapter;
    public static FragmentManager fragmentManager;
    public static DateFormat DFGiorno;
    public static String SGiorno;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TOOLBAR
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Imposto il giorno
        //giorno = java.text.DateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALIAN).format(new Date());
        DFGiorno =DateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALY);
        SGiorno = DFGiorno.format(new Date());



        //TODAY REPORT
        fragmentManager = getSupportFragmentManager();
        if(findViewById(R.id.fragment_container)!= null){
            if(savedInstanceState != null){
                return;
            }

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            TodayReport todayReport = new TodayReport();
            fragmentTransaction.add(R.id.fragment_container, todayReport, null);
            fragmentTransaction.commit();
        }



        //CONTAINER MAIN
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        reportListAdapter = new ReportListAdapter(this);
        recyclerView.setAdapter(reportListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reportViewModel = ViewModelProviders.of(this).get(ReportViewModel.class);

        reportViewModel.getAllReports().observe(this, new Observer<List<Report>>() {
            @Override
            public void onChanged(@Nullable List<Report> reports) {
                reportListAdapter.setReports(reports);
            }
        });

        
        //GESTISCE LO SWIPE TOP BOT LEFT E RIGHT
        recyclerView.setOnTouchListener(new OnSwipeTouchListener(this) {
            /*@SuppressLint("ClickableViewAccessibility")
            public void onSwipeTop() {
                Toast.makeText(getApplicationContext(), "top", Toast.LENGTH_SHORT).show();
            }

            @SuppressLint("ClickableViewAccessibility")
            public void onSwipeBottom() {
                Toast.makeText(getApplicationContext(), "bottom", Toast.LENGTH_SHORT).show();
            }
             */
            @SuppressLint("ClickableViewAccessibility")
            public void onSwipeRight() {
                Toast.makeText(getApplicationContext(), "right", Toast.LENGTH_SHORT).show();
                DFGiorno.getCalendar().add(Calendar.DATE, -1);
                SGiorno = DFGiorno.format(new Date());
                Log.i("DATE", SGiorno);
            }
            @SuppressLint("ClickableViewAccessibility")
            public void onSwipeLeft() {
                Toast.makeText(getApplicationContext(), "left", Toast.LENGTH_SHORT).show();
                DFGiorno.getCalendar().add(Calendar.DATE, 1);
                SGiorno = DFGiorno.format(new Date());
                Log.i("DATE", SGiorno);
            }
        });


        //FAB
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NewReportActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}