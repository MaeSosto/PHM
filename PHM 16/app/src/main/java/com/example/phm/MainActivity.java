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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //VARIABILI
    public static ReportViewModel reportViewModel;
    public static ReportListAdapter reportListAdapter;
    public static FragmentManager fragmentManager;
    public static String SGiorno;
    public static Calendar calendar;
    public static SimpleDateFormat SDF;
    public static TodayReport todayReport;
    public static FragmentTransaction fragmentTransaction;
    public static LiveData<List<Report>> mReports;



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TOOLBAR
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Imposto il giorno
        calendar = Calendar.getInstance();
        SDF = new SimpleDateFormat("dd/MM/yy");
        SGiorno = SDF.format(calendar.getTime());


        //TODAY REPORT
        fragmentManager = getSupportFragmentManager();
        if(findViewById(R.id.fragment_container)!= null){
            if(savedInstanceState != null){
                return;
            }

            fragmentTransaction = fragmentManager.beginTransaction();
            todayReport = new TodayReport();
            fragmentTransaction.add(R.id.fragment_container, todayReport, null);
            fragmentTransaction.commit();
        }



        //CONTAINER MAIN
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        reportListAdapter = new ReportListAdapter(this);
        recyclerView.setAdapter(reportListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reportViewModel = ViewModelProviders.of(this).get(ReportViewModel.class);
        mReports = reportViewModel.getAllReportsInDate(SGiorno);
        mReports.observeForever(new Observer<List<Report>>() {
            @Override
            public void onChanged(List<Report> reports) {
                reportListAdapter.setReports(reports);
            }
        });



        //GESTISCE LO SWIPE TOP BOT LEFT E RIGHT
        recyclerView.setOnTouchListener(new OnSwipeTouchListener(this) {

            @SuppressLint("ClickableViewAccessibility")
            public void onSwipeRight() {
                calendar.add(Calendar.DATE, -1);
                updateDay(SDF.format(calendar.getTime()));
            }

            @SuppressLint("ClickableViewAccessibility")
            public void onSwipeLeft() {
                calendar.add(Calendar.DATE, 1);
                updateDay(SDF.format(calendar.getTime()));
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
    protected void onResume() {
        super.onResume();
        calendar = Calendar.getInstance();
        updateDay(SDF.format(calendar.getTime()));
    }

    public void updateDay (String day){
        SGiorno = day;
        todayReport.refreshTodayReport();
        mReports = reportViewModel.getAllReportsInDate(day);
        mReports.observeForever(new Observer<List<Report>>() {
            @Override
            public void onChanged(List<Report> reports) {
                reportListAdapter.setReports(reports);
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