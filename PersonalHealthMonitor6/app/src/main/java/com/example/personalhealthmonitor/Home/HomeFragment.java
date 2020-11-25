package com.example.personalhealthmonitor.Home;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.personalhealthmonitor.Database.ReportViewModel;
import com.example.personalhealthmonitor.R;
import com.example.personalhealthmonitor.Utilities.Converters;
import com.example.personalhealthmonitor.Utilities.OnSwipeTouchListener;
import com.github.mikephil.charting.data.Entry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import static com.example.personalhealthmonitor.MainActivity.KEY_BATTITO;
import static com.example.personalhealthmonitor.MainActivity.KEY_GLICEMIAMAX;
import static com.example.personalhealthmonitor.MainActivity.KEY_GLICEMIAMIN;
import static com.example.personalhealthmonitor.MainActivity.KEY_PRESSIONEDIA;
import static com.example.personalhealthmonitor.MainActivity.KEY_PRESSIONESIS;
import static com.example.personalhealthmonitor.MainActivity.KEY_TEMPERATURA;
import static com.example.personalhealthmonitor.MainActivity.reportViewModel;

public class HomeFragment extends Fragment {

    private MutableLiveData<Date> dataSel;
    private static Calendar calendar;
    private static SimpleDateFormat SDF;
    private TextView TXVBattiti, TXVPressioneSistolica, TXVPressioneDiastolica, TXVTemperatura, TXVGlicemiaMax, TXVGlicemiaMin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setto la data di oggi
        calendar = Calendar.getInstance();
        SDF = new SimpleDateFormat("dd/MM/yyyy");
        dataSel = new MutableLiveData<>();
        dataSel.setValue(Converters.StringToDate(SDF.format(calendar.getTime())));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        CardView CardNoReport = root.findViewById(R.id.CardNoReport);

        //Recycler view
        RecyclerView recyclerView = root.findViewById(R.id.recyclerview);
        ReportListAdapter reportListAdapter = new ReportListAdapter(getContext());
        recyclerView.setAdapter(reportListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        reportViewModel = ViewModelProviders.of(this).get(ReportViewModel.class);

        dataSel.observe(getViewLifecycleOwner(), new Observer<Date>() {
            @Override
            public void onChanged(Date date) {
                TextView todayreportVal = root.findViewById(R.id.TXVTodayReport);
                todayreportVal.setText(getString(R.string.home_label1)+ SDF.format(date));

                TXVBattiti = root.findViewById(R.id.TXVbattito);
                TXVPressioneSistolica = root.findViewById(R.id.TXVpressioneSistolica);
                TXVPressioneDiastolica = root.findViewById(R.id.TXVpressioneDiastolica);
                TXVTemperatura = root.findViewById(R.id.TXVtemperatura);
                TXVGlicemiaMax = root.findViewById(R.id.TXVglicemia_max);
                TXVGlicemiaMin = root.findViewById(R.id.TXVglicemia_min);
                new getAVG().execute(date);

                reportViewModel.getAllReports(date, null).observe(getViewLifecycleOwner(), reports -> {
                    new getAVG().execute(date);
                    reportListAdapter.setReports(reports);
                    if(reports.size() > 0) {
                        recyclerView.setVisibility(View.VISIBLE);
                        CardNoReport.setVisibility(View.INVISIBLE);
                    }
                    else CardNoReport.setVisibility(View.VISIBLE);
                });
            }
        });


        //GESTISCE LO SWIPE TOP BOT LEFT E RIGHT
        recyclerView.setOnTouchListener(new OnSwipeTouchListener(getContext()) {

            @SuppressLint("ClickableViewAccessibility")
            public void onSwipeRight() {
                calendar.add(Calendar.DATE, -1);
                dataSel.setValue(Converters.StringToDate(SDF.format(calendar.getTime())));
            }

            @SuppressLint("ClickableViewAccessibility")
            public void onSwipeLeft() {
                calendar.add(Calendar.DATE, 1);
                dataSel.setValue(Converters.StringToDate(SDF.format(calendar.getTime())));
            }
        });

        //FAB
        FloatingActionButton fab = root.findViewById(R.id.FabAdd);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), NewReportActivity.class);
            Pair[] pairs = new Pair[1];
            pairs[0] = new Pair<View,String>(fab,"activity_trans");
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), pairs);
            startActivity(intent,options.toBundle());
        });

        return root;
    }

    //Tronca il valore double
    private double tronca(Double num){
        if(num == null ) return 0;
        else{
            num = num * 100;
            num = (double) Math.round(num);
            num = num / 100;
            return num;
        }

    }

    class getAVG extends AsyncTask<Date, Void, Void> {

        Double battito, pressionesis, pressionedia, glicemiamax, glicemiamin, temperatura;

        @Override
        protected void onPostExecute(Void aVoid) {
            if(battito != null) TXVBattiti.setText(String.valueOf(tronca(battito)));
            else TXVBattiti.setText("");
            if (pressionesis != null) TXVPressioneSistolica.setText(String.valueOf(tronca(pressionesis)));
            else TXVPressioneSistolica.setText("");
            if (pressionedia != null) TXVPressioneDiastolica.setText(String.valueOf(tronca(pressionedia)));
            else TXVPressioneDiastolica.setText("");
            if (temperatura != null) TXVTemperatura.setText(String.valueOf(tronca(temperatura)));
            else TXVTemperatura.setText("");
            if (glicemiamax != null) TXVGlicemiaMax.setText(String.valueOf(tronca(glicemiamax)));
            else TXVGlicemiaMax.setText("");
            if (glicemiamin != null) TXVGlicemiaMin.setText(String.valueOf(tronca(glicemiamin)));
            else TXVGlicemiaMin.setText("");
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Date... dates) {
            Date date = dates[0];
            battito = reportViewModel.getAvgVal(KEY_BATTITO, date, null);
            pressionesis = reportViewModel.getAvgVal(KEY_PRESSIONESIS,  date, null);
            pressionedia = reportViewModel.getAvgVal(KEY_PRESSIONEDIA, date, null);
            temperatura = reportViewModel.getAvgVal(KEY_TEMPERATURA,  date, null);
            glicemiamax = reportViewModel.getAvgVal(KEY_GLICEMIAMAX,  date, null);
            glicemiamin = reportViewModel.getAvgVal(KEY_GLICEMIAMIN,  date, null);
            return null;
        }
    }
}