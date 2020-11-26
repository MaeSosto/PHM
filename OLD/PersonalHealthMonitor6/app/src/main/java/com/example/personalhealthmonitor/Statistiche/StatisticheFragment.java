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
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
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
    public PieChart pieChart;
    public LineChart battitoChart;
    public LineChart temperaturaChart;


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

                new PieDataAsyncTask().execute();
                new LineDataAsyncTask().execute(KEY_BATTITO);
                new LineDataAsyncTask().execute(KEY_TEMPERATURA);

            }
        });

        pieChart = root.findViewById(R.id.piechart);
        battitoChart = root.findViewById(R.id.battitochart);
        temperaturaChart = root.findViewById(R.id.temperaturachart);


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
                break;
            case KEY_MESE:
                inizio = Utility.PrimoGiornoMese(Calendar.getInstance());
                fine = Utility.UltimoGiornoMese(Calendar.getInstance());
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

    class LineDataAsyncTask extends AsyncTask<String, Integer, ArrayList<Entry>>{
        private  ArrayList<String> xAxisLabel = new ArrayList<>();
        private String valore;

        @Override
        protected void onPostExecute(ArrayList<Entry> entries) {
            super.onPostExecute(entries);

            LineDataSet lineDataSet = new LineDataSet(entries, periodo.getValue());
            lineDataSet.setValueTextSize(12);
            lineDataSet.setValueTextColor(Color.BLACK);
            LineData lineData = new LineData(lineDataSet);

            if (valore == KEY_BATTITO){
                XAxis xAxis = battitoChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setDrawGridLines(true);
                xAxis.setGranularity(1);
                xAxis.setGranularityEnabled(true);
                battitoChart.setDescription(getDescription());
                battitoChart.animateXY(1000, 1000);
                battitoChart.setTouchEnabled(true);
                battitoChart.setPinchZoom(true);
                battitoChart.setData(lineData);
                battitoChart.setScaleEnabled(true);
                battitoChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));
                battitoChart.invalidate();
            }
            else{
                XAxis xAxis = temperaturaChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setDrawGridLines(true);
                xAxis.setGranularity(1);
                xAxis.setGranularityEnabled(true);
                temperaturaChart.setDescription(getDescription());
                temperaturaChart.animateXY(1000, 1000);
                temperaturaChart.setTouchEnabled(true);
                temperaturaChart.setPinchZoom(true);
                temperaturaChart.setData(lineData);
                temperaturaChart.setScaleEnabled(true);
                temperaturaChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));
                temperaturaChart.invalidate();
            }
        }

        @Override
        protected ArrayList<Entry> doInBackground(String... strings) {
            valore = strings[0];
            ArrayList<Entry> dataVals = new ArrayList<Entry>();
            Calendar Cinizio = Calendar.getInstance();
            Calendar Cfine = Calendar.getInstance();
            if(inizio == null && fine == null){
                Cinizio.setTime(Converters.StringToDate(SDF.format(reportViewModel.getMinMaxDateReport("MIN", valore))));
                Cfine.setTime(Converters.StringToDate(SDF.format(reportViewModel.getMinMaxDateReport("MAX", valore))));
            }
            else {
                Cinizio.setTime(Converters.StringToDate(SDF.format(inizio)));
                Cfine.setTime(Converters.StringToDate(SDF.format(fine)));
            }
            Cfine.add(Calendar.DATE, 1);
            int c= 0;
            while(Converters.DateToLong(Cinizio.getTime())< Converters.DateToLong(Cfine.getTime())){
                if(periodo.getValue() == KEY_ANNO || periodo.getValue() == KEY_TUTTO) {
                    Calendar tmp = Calendar.getInstance();
                    tmp.setTime(Cinizio.getTime());
                    tmp.setTime(Utility.UltimoGiornoMese(tmp));
                    Double value = reportViewModel.getAvgVal(valore, Cinizio.getTime(), tmp.getTime());
                    if(value != null){
                        dataVals.add(new Entry(c, value.floatValue()));
                    }
                    xAxisLabel.add(String.valueOf(Cinizio.getTime().getMonth()+1)+ "/"+ String.valueOf(Cinizio.getTime().getYear()-100));
                    Cinizio.add(Calendar.MONTH, 1);
                    Cinizio.setTime(Utility.PrimoGiornoMese(Cinizio));
                    c++;
                }
                else {
                    Double value = reportViewModel.getAvgVal(valore, Cinizio.getTime(), null);
                    if (value != null) dataVals.add(new Entry(c, value.floatValue()));
                    xAxisLabel.add(String.valueOf(Cinizio.getTime().getDate())+ "/"+ String.valueOf(Cinizio.getTime().getMonth()+1));
                    Cinizio.add(Calendar.DATE, 1);
                    c++;
                }
            }
            return dataVals;
        }
    }
}


