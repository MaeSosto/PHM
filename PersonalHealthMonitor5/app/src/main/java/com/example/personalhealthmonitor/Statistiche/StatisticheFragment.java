package com.example.personalhealthmonitor.Statistiche;

import android.graphics.Color;
import android.os.AsyncTask;
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

import com.example.personalhealthmonitor.R;
import com.example.personalhealthmonitor.Utilities.Converters;
import com.example.personalhealthmonitor.Utilities.Utility;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.example.personalhealthmonitor.MainActivity.KEY_BATTITO;
import static com.example.personalhealthmonitor.MainActivity.KEY_GLICEMIAMAX;
import static com.example.personalhealthmonitor.MainActivity.KEY_GLICEMIAMIN;
import static com.example.personalhealthmonitor.MainActivity.KEY_PRESSIONEDIA;
import static com.example.personalhealthmonitor.MainActivity.KEY_PRESSIONESIS;
import static com.example.personalhealthmonitor.MainActivity.KEY_TEMPERATURA;
import static com.example.personalhealthmonitor.MainActivity.reportViewModel;

public class StatisticheFragment extends Fragment {
    private static final String KEY_SETTIMANA = "Settimana";
    private static final String KEY_MESE = "Mese";
    private static final String KEY_ANNO = "Anno";
    private static final String KEY_TUTTO = "Tutto";
    private SimpleDateFormat SDF;
    private MutableLiveData<String> periodo;
    private TextView TXVNumReport;
    private Date inizio, fine;
    private String[] asseX;
    public PieChart pieChart;
    public LineChart battitoChart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDF = new SimpleDateFormat("dd/MM/yyyy");
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

                TXVNumReport = root.findViewById(R.id.TXVNumReport);


                //Log.i("Prova", String.valueOf(reportViewModel.getAvgVal(KEY_BATTITO, fine, null)));
                new PieDataAsyncTask().execute();
                new LineDataAsyncTask().execute();

            }
        });

        pieChart = root.findViewById(R.id.piechart);
        battitoChart = root.findViewById(R.id.battitochart);


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



    private void setDate(){
        switch (periodo.getValue()) {
            case KEY_SETTIMANA:
                inizio = Utility.PrimoGiornoSettimana(Calendar.getInstance());
                fine = Utility.UltimoGiornoSettimana(Calendar.getInstance());
                asseX = new String[]{"Lun", "Mar", "Mer", "Gio", "Ven", "Sab", "Dom"};
                break;
            case KEY_MESE:
                inizio = Utility.PrimoGiornoMese(Calendar.getInstance());
                fine = Utility.UltimoGiornoMese(Calendar.getInstance());
                asseX = new String[]{"1", "2", "3", "4", "5", "6", "7"};
                break;
            case KEY_ANNO:
                inizio = Utility.PrimoGiornoAnno(Calendar.getInstance());
                fine = Utility.UltimoGiornoAnno(Calendar.getInstance());
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


    class PieDataAsyncTask extends AsyncTask<Void, Integer, ArrayList<PieEntry>>{

        @Override
        protected void onPostExecute(ArrayList<PieEntry> pieEntries) {
            super.onPostExecute(pieEntries);

            PieDataSet pieDataSet = new PieDataSet(pieEntries, periodo.getValue());
            pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            pieDataSet.setValueTextSize(12);
            pieDataSet.setValueTextColor(Color.BLACK);
            PieData pieData = new PieData(pieDataSet);

            pieChart.setData(pieData);
            pieChart.setDescription(getDescription());
            pieChart.animateXY(1000, 1000);
            pieChart.invalidate();
        }

        @Override
        protected ArrayList<PieEntry> doInBackground(Void... voids) {

            TXVNumReport.setText(String.valueOf(reportViewModel.getCOUNTVal(null, null, inizio, fine)));
            ArrayList<PieEntry> PiedataVals = new ArrayList<>();

            PiedataVals.add(new PieEntry(reportViewModel.getCOUNTVal(KEY_BATTITO,null, inizio, fine), "Battito"));
            PiedataVals.add(new PieEntry( reportViewModel.getCOUNTVal(KEY_TEMPERATURA, null, inizio, fine) , "Temperatura"));
            PiedataVals.add(new PieEntry(reportViewModel.getCOUNTVal(KEY_PRESSIONESIS, KEY_PRESSIONEDIA, inizio, fine), "Pressione"));
            PiedataVals.add(new PieEntry(reportViewModel.getCOUNTVal(KEY_GLICEMIAMAX, KEY_GLICEMIAMIN, inizio, fine), "Glicemia"));

           return PiedataVals;
        }
    }

    class LineDataAsyncTask extends AsyncTask<Void, Integer, ArrayList<Entry>>{
        @Override
        protected void onPostExecute(ArrayList<Entry> entries) {
            super.onPostExecute(entries);

            LineDataSet lineDataSet = new LineDataSet(entries, periodo.getValue());

            //lineDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            lineDataSet.setValueTextSize(12);
            lineDataSet.setValueTextColor(Color.BLACK);
            LineData lineData = new LineData(lineDataSet);

            battitoChart.setData(lineData);
            battitoChart.setDescription(getDescription());
            battitoChart.animateXY(1000, 1000);
            battitoChart.setTouchEnabled(true);
            battitoChart.setPinchZoom(true);

           //XAxis xAxis = battitoChart.getXAxis();
           //xAxis.setValueFormatter(new IndexAxisValueFormatter(asseX));
           //xAxis.setCenterAxisLabels(true);
           //xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
           //xAxis.setGranularity(1);
           //xAxis.setGranularityEnabled(true);
        }

        @Override
        protected ArrayList<Entry> doInBackground(Void... voids) {
            ArrayList<Entry> dataVals = new ArrayList<Entry>();

            if(periodo.getValue() == KEY_SETTIMANA ){
                    Calendar Cinizio = Calendar.getInstance();
                    Cinizio.setTime(Converters.StringToDate(SDF.format(inizio)));
                    Calendar Cfine = Calendar.getInstance();
                    Cfine.setTime(Converters.StringToDate(SDF.format(fine)));
                    Cfine.add(Calendar.DATE, 1);
                    int c= 0;
                    while(Converters.DateToLong(Cinizio.getTime())< Converters.DateToLong(Cfine.getTime())){
                        Double battito = reportViewModel.getAvgVal(KEY_BATTITO, Cinizio.getTime(), null);

                        if(battito != null){
                            dataVals.add(new Entry(c, battito.floatValue()));
                            Log.i("Battito", Converters.DateToString(Cinizio.getTime())+ ":"+ String.valueOf(battito));

                        }
                        Cinizio.add(Calendar.DATE, 1);
                        c++;
                    }
            }
            else if(periodo.getValue() == KEY_MESE){
                Calendar Cinizio = Calendar.getInstance();
                Cinizio.setTime(Converters.StringToDate(SDF.format(inizio)));
                Cinizio.setTime(Utility.PrimoGiornoSettimana(Cinizio)); //prendo il primo giorno della settimana di questo mese
                Calendar Cfine = Calendar.getInstance();
                Cfine.setTime(Converters.StringToDate(SDF.format(fine)));
                Cfine.setTime(Utility.UltimoGiornoSettimana(Cfine)); //prendo l'ultimo giorno della settimana di questo mese
                int c = 0;
                while(Converters.DateToLong(Cinizio.getTime())< Converters.DateToLong(Cfine.getTime())){

                    Cinizio.add(Calendar.DATE, 1);
                    c++;
                }

            }


            return dataVals;
        }


    }
}


