package com.example.testmenu2.statistiche;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.testmenu2.Database.AVG;
import com.example.testmenu2.Database.Report;
import com.example.testmenu2.Database.ReportViewModel;
import com.example.testmenu2.R;
import com.example.testmenu2.Utilities.Converters;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;

import java.util.Date;
import java.util.List;

public class StatisticheFragment extends Fragment {

    private StatisticheViewModel statisticheViewModel;
    private BarChart pressioneChart;
    private PieChart pieChart;
    private LineChart battitoChart, temperaturaChart, glicemiaChart;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        statisticheViewModel =
                new ViewModelProvider(this).get(StatisticheViewModel.class);
        View root = inflater.inflate(R.layout.fragment_statistiche, container, false);

        //BOTTONI
        Button BTNsettimana = root.findViewById(R.id.BTNsettimana);
        Button BTNmese = root.findViewById(R.id.BTNmese);
        Button BTNanno = root.findViewById(R.id.BTNanno);
        Button BTNtutti = root.findViewById(R.id.BTNtutti);

        //ETICHETTE
        TextView TXVPeriodo = root.findViewById(R.id.TXVPeriodo_val);
        statisticheViewModel.getStringPeriodo().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                TXVPeriodo.setText(s);
            }
        });
        TextView TXVNumReport = root.findViewById(R.id.TXVNumeroReport_val);
        statisticheViewModel.getStringnumReport().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                TXVNumReport.setText(s);
            }
        });

        statisticheViewModel.setReportViewModel(ViewModelProviders.of(this).get(ReportViewModel.class));
        statisticheViewModel.setOwner(getViewLifecycleOwner());

        UpdateDate("Settimana");


        BTNsettimana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateDate("Settimana");
            }
        });

        BTNmese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateDate("Mese");
            }
        });

        BTNanno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateDate("Anno");
            }
        });

        BTNtutti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateDate("Tutto");
            }
        });

        pieChart = root.findViewById(R.id.piechart);
        battitoChart = root.findViewById(R.id.battitochart);
        temperaturaChart = root.findViewById(R.id.temperaturachart);
        pressioneChart = root.findViewById(R.id.pressionechart);
        glicemiaChart = root.findViewById(R.id.glicemiachart);

        return root;
    }


    //DISEGNA IL GRAFICO A TORTA IN BASE AI DATI CONTENUTI NEI REPORT REGISTRATI TRA LE DUE DATE IN INPUT
    protected void UpdateDate(String periodo){

        statisticheViewModel.setChartData(periodo);

        //MODIFICO E OSSERVO LA LISTA DI REPORT IN BASE ALLA QUERY EFFETTUATA
        statisticheViewModel.getmReports().observe(getViewLifecycleOwner(),new Observer<List<Report>>() {
            @Override
            public void onChanged(List<Report> reports) {
                //AGGIORNO L'ETICHETTA CHE CONTA I REPORT DEL PERIODO
                statisticheViewModel.setStringnumReport(String.valueOf(reports.size()));

                //DISEGNO IL GRAFICO A TORTA IN BASE ALLA LISTA DEI REPORT RICEVUTI
                pieChart.setData(statisticheViewModel.getPieData(reports));
                pieChart.setDescription(statisticheViewModel.getDescription(""));
                pieChart.animateXY(1000, 1000);
                //pieChart.invalidate();
            }
        });

        statisticheViewModel.getBattitoAVG().observe(getViewLifecycleOwner(), new Observer<List<AVG>>() {
            @Override
            public void onChanged(List<AVG> avgs) {
                statisticheViewModel.setLineChart(battitoChart, avgs);
            }
        });

        statisticheViewModel.getPressioneSistolicaAVG().observe(getViewLifecycleOwner(), new Observer<List<AVG>>() {
            @Override
            public void onChanged(List<AVG> avgs) {
                statisticheViewModel.setPressioneSistolica(avgs);
                statisticheViewModel.setBarChart(pressioneChart);
            }
        });

        statisticheViewModel.getPressioneDiastolicaAVG().observe(getViewLifecycleOwner(), new Observer<List<AVG>>() {
            @Override
            public void onChanged(List<AVG> avgs) {
                statisticheViewModel.setPressioneDiastolica(avgs);
                statisticheViewModel.setBarChart(pressioneChart);
            }
        });

        statisticheViewModel.getTemperaturaAVG().observe(getViewLifecycleOwner(), new Observer<List<AVG>>() {
            @Override
            public void onChanged(List<AVG> avgs) {
                statisticheViewModel.setLineChart(temperaturaChart, avgs);
            }
        });

        statisticheViewModel.getGlicemiaAVG().observe(getViewLifecycleOwner(), new Observer<List<AVG>>() {
            @Override
            public void onChanged(List<AVG> avgs) {
                statisticheViewModel.setLineChart(glicemiaChart, avgs);
            }
        });
    }

}