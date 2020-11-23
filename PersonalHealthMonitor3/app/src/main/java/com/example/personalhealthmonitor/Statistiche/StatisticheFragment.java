package com.example.personalhealthmonitor.Statistiche;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.personalhealthmonitor.Database.AVG;
import com.example.personalhealthmonitor.Database.Report;
import com.example.personalhealthmonitor.R;
import com.example.personalhealthmonitor.Utilities.Converters;
import com.example.personalhealthmonitor.Utilities.Utility;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.personalhealthmonitor.MainActivity.KEY_BATTITO;
import static com.example.personalhealthmonitor.MainActivity.reportViewModel;

public class StatisticheFragment extends Fragment {
    private static final String KEY_SETTIMANA = "Settimana";
    private static final String KEY_MESE = "Mese";
    private static final String KEY_ANNO = "Anno";
    private static final String KEY_TUTTO = "Tutto";

    private MutableLiveData<String> periodo;
    private Date inizio, fine;
    private String[] asseX;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        periodo = new MutableLiveData<>();
        periodo.setValue(KEY_SETTIMANA);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_statistiche, container, false);

        TextView TXVPeriodo = root.findViewById(R.id.TXVPeriodo);
        periodo.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String string) {
                setDate();
                if(string.equals(KEY_TUTTO))  TXVPeriodo.setText(KEY_TUTTO);
                else  TXVPeriodo.setText("Dal " + Converters.DateToString(inizio) + " al " + Converters.DateToString(fine));

                TextView TXVNumReport = root.findViewById(R.id.TXVNumReport);
                TXVNumReport.setText(String.valueOf(reportViewModel.getCOUNTVal(null, inizio, fine)));


                PieChart pieChart = root.findViewById(R.id.piechart);
                pieChart.setData(getPieData());
                pieChart.setDescription(getDescription());
                pieChart.animateXY(1000, 1000);
                //pieChart.invalidate();
            }
        });




        //Quando clicco su uno dei 4 bottoni cambio il periodo
        Button BTNsettimana = root.findViewById(R.id.statistiche_btn1);
        BTNsettimana.setOnClickListener(v -> periodo.setValue(KEY_SETTIMANA));
        Button BTNmese = root.findViewById(R.id.statistiche_btn2);
        BTNmese.setOnClickListener(v -> periodo.setValue(KEY_MESE));
        Button BTNanno = root.findViewById(R.id.statistiche_btn3);
        BTNanno.setOnClickListener(v -> periodo.setValue(KEY_ANNO));
        Button BTNtutto = root.findViewById(R.id.statistiche_btn4);
        BTNtutto.setOnClickListener(v -> periodo.setValue(KEY_TUTTO));

        return root;
    }

    public PieData getPieData(){
        PieDataSet pieDataSet = new PieDataSet(setDataValues(), periodo.getValue());
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        pieDataSet.setValueTextSize(12);
        pieDataSet.setValueTextColor(Color.BLACK);
        PieData pieData = new PieData(pieDataSet);
        return pieData;
    }

    //Aggiungo i contenuti nel PieChart
    private ArrayList<PieEntry> setDataValues(){
        ArrayList<PieEntry> PiedataVals = new ArrayList<>();
        //IMPOSTO IL PIECHART
        //int battito = reportViewModel.getCOUNTVal(KEY_BATTITO, inizio, fine).
        //pressioneSistolica = 0, pressioneDiastolica=0, temperatura = 0, glicemia = 0;
//
        //for (int i = 0; i < reports.size(); i++) {
        //    Report report = reports.get(i);
        //    if(report.getBattito() != 0) battito++;
        //    if(report.getPressione_sistolica() != 0) pressioneSistolica++;
        //    if(report.getPressione_diastolica() != 0) pressioneDiastolica++;
        //    if(report.getTemperatura() != 0) temperatura++;
        //    if(report.getGlicemia() != 0) glicemia++;
        //}

        //PiedataVals.add(new PieEntry(battito, "Battito"));
        //PiedataVals.add(new PieEntry(temperatura, "Temperatura"));
        //PiedataVals.add(new PieEntry(pressioneSistolica, "Pressione Sistolica"));
        //PiedataVals.add(new PieEntry(pressioneDiastolica, "Pressione Diastolica"));
        //PiedataVals.add(new PieEntry(glicemia, "Glicemia"));
        return PiedataVals;
    }

    private void setDate(){
        switch (periodo.getValue()) {
            case KEY_SETTIMANA:
                inizio = Utility.PrimoGiornoSettimana();
                fine = Utility.UltimoGiornoSettimana();
                //asseX = new String[]{"Lun", "Mar", "Mer", "Gio", "Ven", "Sab", "Dom"};
                break;
            case KEY_MESE:
                inizio = Utility.PrimoGiornoMese();
                fine = Utility.UltimoGiornoMese();
                break;
            case KEY_ANNO:
                inizio = Utility.PrimoGiornoAnno();
                fine = Utility.UltimoGiornoAnno();
                break;
            case KEY_TUTTO:
               inizio = null;
               fine = null;
                return;
        }
    }

    private Description getDescription(){
        Description description = new Description();
        description.setText("");
        return description;
    }
}