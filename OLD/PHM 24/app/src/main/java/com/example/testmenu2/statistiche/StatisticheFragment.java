package com.example.testmenu2.statistiche;

import android.os.Bundle;
import android.util.Log;
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

import com.example.testmenu2.Database.Report;
import com.example.testmenu2.Database.ReportViewModel;
import com.example.testmenu2.R;
import com.example.testmenu2.Utilities.Converters;
import com.github.mikephil.charting.charts.PieChart;

import java.util.Date;
import java.util.List;

public class StatisticheFragment extends Fragment {

    private StatisticheViewModel statisticheViewModel;
    private Button BTNsettimana, BTNmese, BTNanno, BTNtutti;
    private TextView TXVPeriodo, TXVNumReport;
    public static LiveData<List<Report>> mReports;
    private ReportViewModel reportViewModel;

    int NumReport;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        statisticheViewModel =
                new ViewModelProvider(this).get(StatisticheViewModel.class);
        View root = inflater.inflate(R.layout.fragment_statistiche, container, false);

        BTNsettimana = root.findViewById(R.id.BTNsettimana);
        BTNmese = root.findViewById(R.id.BTNmese);
        BTNanno = root.findViewById(R.id.BTNanno);
        BTNtutti = root.findViewById(R.id.BTNtutti);
        TXVPeriodo = root.findViewById(R.id.TXVPeriodo_val);
        TXVNumReport = root.findViewById(R.id.TXVNumeroReport_val);


        reportViewModel = ViewModelProviders.of(this).get(ReportViewModel.class);
        UpdateDate(statisticheViewModel.PrimoGiornoSettimana(), statisticheViewModel.UltimoGiornoSettimana());


        BTNsettimana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateDate(statisticheViewModel.PrimoGiornoSettimana(), statisticheViewModel.UltimoGiornoSettimana());
            }
        });

        BTNmese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateDate(statisticheViewModel.PrimoGiornoMese(), statisticheViewModel.UltimoGiornoMese());
            }
        });

        BTNanno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateDate(statisticheViewModel.PrimoGiornoAnno(), statisticheViewModel.UltimoGiornoAnno());
            }
        });

        BTNtutti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateDate(null, null);
            }
        });

        PieChart pieChart = root.findViewById(R.id.piechart);
        pieChart.animateXY(1000, 1000);
        pieChart.invalidate();

        statisticheViewModel.getPeriodo().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.i("Periodo", s);
                pieChart.setData(statisticheViewModel.getPieData(s));
                pieChart.setDescription(statisticheViewModel.getDescription(s));
            }
        });

        return root;
    }

   private void UpdateDate(Date date1, Date date2){
        if (date1 == null && date2 == null){
            TXVPeriodo.setText("Tutto");
            mReports = reportViewModel.getAllReports();
        }
        else {
            TXVPeriodo.setText("Dal " + Converters.DateToString(date1) + " al " + Converters.DateToString(date2));
            mReports = reportViewModel.getAllReportsInPeriod(date1, date2);

        }
        mReports.observe(getViewLifecycleOwner(),new Observer<List<Report>>() {
           @Override
           public void onChanged(List<Report> reports) {
               NumReport = reports.size();
               Log.i("NUM REPORT", String.valueOf(NumReport));
               TXVNumReport.setText(String.valueOf(NumReport));
           }
        });
    }

}