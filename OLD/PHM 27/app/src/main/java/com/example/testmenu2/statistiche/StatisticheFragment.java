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
    private PieChart pieChart;
    private String periodo;

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
        periodo = "Settimana";


        reportViewModel = ViewModelProviders.of(this).get(ReportViewModel.class);
        UpdateDate(statisticheViewModel.PrimoGiornoSettimana(), statisticheViewModel.UltimoGiornoSettimana());


        BTNsettimana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                periodo = "Settimana";
                UpdateDate(statisticheViewModel.PrimoGiornoSettimana(), statisticheViewModel.UltimoGiornoSettimana());
            }
        });

        BTNmese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                periodo = "Mese";
                UpdateDate(statisticheViewModel.PrimoGiornoMese(), statisticheViewModel.UltimoGiornoMese());
            }
        });

        BTNanno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                periodo = "Anno";
                UpdateDate(statisticheViewModel.PrimoGiornoAnno(), statisticheViewModel.UltimoGiornoAnno());
            }
        });

        BTNtutti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                periodo = "Tutto";
                UpdateDate(null, null);
            }
        });

        pieChart = root.findViewById(R.id.piechart);



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
               TXVNumReport.setText(String.valueOf(reports.size()));

               pieChart.setData(statisticheViewModel.getPieData(reports, periodo));
               pieChart.setDescription(statisticheViewModel.getDescription(""));
               pieChart.animateXY(1000, 1000);
               pieChart.invalidate();
           }
        });
    }

}