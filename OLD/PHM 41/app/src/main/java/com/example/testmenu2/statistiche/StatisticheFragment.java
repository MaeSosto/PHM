package com.example.testmenu2.statistiche;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.testmenu2.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.PieData;

public class StatisticheFragment extends Fragment {

    private StatisticheViewModel statisticheViewModel;
    private BarChart pressioneChart;
    private PieChart pieChart;
    private LineChart battitoChart, temperaturaChart, glicemiaChart;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        statisticheViewModel = new ViewModelProvider(this).get(StatisticheViewModel.class);
        View root = inflater.inflate(R.layout.fragment_statistiche, container, false);

        //BOTTONI
        Button BTNsettimana = root.findViewById(R.id.BTNsettimana);
        BTNsettimana.setOnClickListener(v -> statisticheViewModel.setPeriodo("Settimana"));

        Button BTNmese = root.findViewById(R.id.BTNmese);
        BTNmese.setOnClickListener(v -> statisticheViewModel.setPeriodo("Mese"));

        Button BTNanno = root.findViewById(R.id.BTNanno);
        BTNanno.setOnClickListener(v -> {
            statisticheViewModel.setPeriodo("Anno");
        });

        Button BTNtutti = root.findViewById(R.id.BTNtutti);
        BTNtutti.setOnClickListener(v -> {
            statisticheViewModel.setPeriodo("Tutto");
        });

        //ETICHETTE
        TextView TXVPeriodo = root.findViewById(R.id.TXVPeriodo_val);
        statisticheViewModel.getTXVPeriodo().observe(getViewLifecycleOwner(), TXVPeriodo::setText);

        TextView TXVNumReport = root.findViewById(R.id.TXVNumeroReport_val);
        statisticheViewModel.getTXVnumReport().observe(getViewLifecycleOwner(), TXVNumReport::setText);

        //GRAFICI
        pieChart = root.findViewById(R.id.piechart);
        statisticheViewModel.getPieDataMutableLiveData().observe(getViewLifecycleOwner(), pieData -> {
            //DISEGNO IL GRAFICO A TORTA IN BASE ALLA LISTA DEI REPORT RICEVUTI
            pieChart.setData(pieData);
            pieChart.setDescription(new Description());
            pieChart.animateXY(1000, 1000);
            pieChart.invalidate();
        });


        /*statisticheViewModel.getPieChartMutableLiveData().observe(getViewLifecycleOwner(), new Observer<PieChart>() {
            @Override
            public void onChanged(PieChart tmppieChart) {
                pieChart = tmppieChart;
            }
        });

         */

        battitoChart = root.findViewById(R.id.battitochart);
        statisticheViewModel.getBattitoMutableLineData().observe(getViewLifecycleOwner(), lineData -> {
            battitoChart.setData(lineData);
            //Nessuna descrizione
            battitoChart.animateXY(1000, 1000);
            battitoChart.setTouchEnabled(true);
            battitoChart.setPinchZoom(true);
        });

        temperaturaChart = root.findViewById(R.id.temperaturachart);
        statisticheViewModel.getTemperaturaMutableLineData().observe(getViewLifecycleOwner(), lineData -> {
            temperaturaChart.setData(lineData);
            temperaturaChart.setDescription(statisticheViewModel.getDescription());
            temperaturaChart.animateXY(1000, 1000);
            temperaturaChart.setTouchEnabled(true);
            temperaturaChart.setPinchZoom(true);
        });

        glicemiaChart = root.findViewById(R.id.glicemiachart);
        statisticheViewModel.getGlicemiaMutableLineData().observe(getViewLifecycleOwner(), lineData -> {
            glicemiaChart.setData(lineData);
            glicemiaChart.setDescription(statisticheViewModel.getDescription());
            glicemiaChart.animateXY(1000, 1000);
            glicemiaChart.setTouchEnabled(true);
            glicemiaChart.setPinchZoom(true);
        });

        pressioneChart = root.findViewById(R.id.pressionechart);
        statisticheViewModel.getPressioneMutableBarData().observe(getViewLifecycleOwner(), barData -> {
            pressioneChart.setData(barData);
            pressioneChart.setDescription(statisticheViewModel.getDescription());
            pressioneChart.animateXY(1000, 1000);
            pressioneChart.setTouchEnabled(true);
            pressioneChart.setPinchZoom(true);
        });

        return root;
    }
}