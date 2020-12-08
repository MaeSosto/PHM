package com.example.personalhealthmonitor.Statistiche;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.personalhealthmonitor.Database.Report;
import com.example.personalhealthmonitor.R;
import com.example.personalhealthmonitor.Utilities.Converters;
import com.example.personalhealthmonitor.Utilities.Utility;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import static com.example.personalhealthmonitor.Utilities.Utility.*;

public class StatisticheFragment extends Fragment {
    private MutableLiveData<String> periodo;
    private TextView TXVNumReport;
    private Date inizio, fine;
    private PieChart pieChart;
    private LineChart battitoChart;
    private LineChart temperaturaChart;
    private BarChart pressioneChart;
    private BarChart glicemiaChart;
    private View root;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        periodo = new MutableLiveData<>();
        periodo.setValue(KEY_SETTIMANA);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root =  inflater.inflate(R.layout.fragment_statistiche, container, false);

        TextView TXVPeriodo = root.findViewById(R.id.TXVPeriodo);
        //IN BASE A COME CAMBIA IL PERIODO SELEZIONATO CAMBIA TUTTO IL RESTO
        periodo.observe(getViewLifecycleOwner(), string -> {
            setDate();
            if(string.equals(KEY_TUTTO))  TXVPeriodo.setText(KEY_TUTTO);
            else  TXVPeriodo.setText("Dal " + Converters.DateToString(inizio) + " al " + Converters.DateToString(fine));

            TXVNumReport = root.findViewById(R.id.TXVNumReport);
            //SE HO DEI REPORT ALLORA VISUALIZZO LE CARD CON I DATI A LORO ASSOCIATI ALTRIMENTI NO
            reportViewModel.getAllReports(inizio, fine).observe(getViewLifecycleOwner(), new Observer<List<Report>>() {
                @Override
                public void onChanged(List<Report> reports) {
                    TXVNumReport.setText(String.valueOf(reports.size()));
                    if(reports.size() > 0){
                        root.findViewById(R.id.CardNoReport).setVisibility(View.GONE);
                        root.findViewById(R.id.Cardinfo).setVisibility(View.VISIBLE);
                        root.findViewById(R.id.Cardbattito).setVisibility(View.VISIBLE);
                        root.findViewById(R.id.Cardpressione).setVisibility(View.VISIBLE);
                        root.findViewById(R.id.Cardtemperatura).setVisibility(View.VISIBLE);
                        root.findViewById(R.id.Cardglicemia).setVisibility(View.VISIBLE);
                        new PieDataAsyncTask().execute();
                        new LineDataAsyncTask().execute(KEY_BATTITO);
                        new LineDataAsyncTask().execute(KEY_TEMPERATURA);
                        new BarDataAsyncTask().execute(KEY_PRESSIONESIS, KEY_PRESSIONEDIA);
                        new BarDataAsyncTask().execute(KEY_GLICEMIAMAX, KEY_GLICEMIAMIN);
                    }
                    else{
                        root.findViewById(R.id.CardNoReport).setVisibility(View.VISIBLE);
                        root.findViewById(R.id.Cardinfo).setVisibility(View.GONE);
                        root.findViewById(R.id.Cardbattito).setVisibility(View.GONE);
                        root.findViewById(R.id.Cardpressione).setVisibility(View.GONE);
                        root.findViewById(R.id.Cardtemperatura).setVisibility(View.GONE);
                        root.findViewById(R.id.Cardglicemia).setVisibility(View.GONE);
                    }
                }
            });
        });

        pieChart = root.findViewById(R.id.piechart);
        battitoChart = root.findViewById(R.id.battitochart);
        temperaturaChart = root.findViewById(R.id.temperaturachart);
        pressioneChart = root.findViewById(R.id.pressionechart);
        glicemiaChart = root.findViewById(R.id.glicemiachart);


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

    //PRENDE LE DATE DI INIZIO E FINE DEL PERIODO INDICATO
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
        }
    }

    //SETTA LA DESCRIZIONE NULLA
    private Description getDescription(){
        Description description = new Description();
        description.setText("");
        return description;
    }


    //CONTA IL UMERO DI VALORI PRESENTI NEI VARI REPORT E CREA IL PIE CHART
    class PieDataAsyncTask extends AsyncTask<Void, Integer, ArrayList<PieEntry>>{

        @Override
        protected void onPostExecute(ArrayList<PieEntry> pieEntries) {
            super.onPostExecute(pieEntries);

            if(pieEntries.size() > 0) {
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
        }

        @Override
        protected ArrayList<PieEntry> doInBackground(Void... voids) {
            //TXVNumReport.setText(String.valueOf(reportViewModel.getCOUNTVal(null, null, inizio, fine)));
            ArrayList<PieEntry> PiedataVals = new ArrayList<>();
            PiedataVals.add(new PieEntry(reportViewModel.getCOUNTVal(KEY_BATTITO,null, inizio, fine), "Battito"));
            PiedataVals.add(new PieEntry( reportViewModel.getCOUNTVal(KEY_TEMPERATURA, null, inizio, fine) , "Temperatura"));
            PiedataVals.add(new PieEntry(reportViewModel.getCOUNTVal(KEY_PRESSIONESIS, KEY_PRESSIONEDIA, inizio, fine), "Pressione"));
            PiedataVals.add(new PieEntry(reportViewModel.getCOUNTVal(KEY_GLICEMIAMAX, KEY_GLICEMIAMIN, inizio, fine), "Glicemia"));
           return PiedataVals;
        }
    }

    //CREA IL CHART DEL BATTITO E DELLA TEMPERATURA PRENDENDO I DATI IN BASE AL PERIODO NEL DB
    class LineDataAsyncTask extends AsyncTask<String, Integer, ArrayList<Entry>>{
        private final ArrayList<String> xAxisLabel = new ArrayList<>();
        private String valore;

        @Override
        protected void onPostExecute(ArrayList<Entry> entries) {
            super.onPostExecute(entries);

            LineDataSet lineDataSet = new LineDataSet(entries, periodo.getValue());
            lineDataSet.setValueTextSize(12);
            lineDataSet.setCircleColor(Color.rgb(197, 123, 87));
            lineDataSet.setColor(Color.rgb(197, 123, 87));
            lineDataSet.setValueTextColor(Color.BLACK);
            LineData lineData = new LineData(lineDataSet);

            if (valore.equals(KEY_BATTITO)){
                if(entries.size() > 0) {
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
                else  root.findViewById(R.id.Cardbattito).setVisibility(View.GONE);
            }
            else{
                if(entries.size() > 0) {
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
                else root.findViewById(R.id.Cardtemperatura).setVisibility(View.GONE);
            }
        }

        @Override
        protected ArrayList<Entry> doInBackground(String... strings) {
            valore = strings[0];
            ArrayList<Entry> dataVals = new ArrayList<>();
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
                if(periodo.getValue().equals(KEY_ANNO) || periodo.getValue().equals(KEY_TUTTO)) {
                    Calendar tmp = Calendar.getInstance();
                    tmp.setTime(Cinizio.getTime());
                    tmp.setTime(Utility.UltimoGiornoMese(tmp));
                    Double value = reportViewModel.getAvgVal(valore, Cinizio.getTime(), tmp.getTime());
                    if(value != null){
                        dataVals.add(new Entry(c, value.floatValue()));
                    }
                    xAxisLabel.add((Cinizio.getTime().getMonth() + 1) + "/"+ (Cinizio.getTime().getYear() - 100));
                    Cinizio.add(Calendar.MONTH, 1);
                    Cinizio.setTime(Utility.PrimoGiornoMese(Cinizio));
                }
                else {
                    Double value = reportViewModel.getAvgVal(valore, Cinizio.getTime(), null);
                    if (value != null) dataVals.add(new Entry(c, value.floatValue()));
                    xAxisLabel.add(Cinizio.getTime().getDate() + "/"+ (Cinizio.getTime().getMonth() + 1));
                    Cinizio.add(Calendar.DATE, 1);
                }
                c++;
            }
            return dataVals;
        }
    }

    //CREA IL CHART DELLA PRESSIONE E DELLA GLICEMIA PRENDENDO I DATI IN BASE AL PERIODO NEL DB
    class BarDataAsyncTask extends AsyncTask<String, Integer, List<ArrayList<BarEntry>>>{
        private final ArrayList<String> xAxisLabel = new ArrayList<>();
        private String valore1;

        @Override
        protected void onPostExecute(List<ArrayList<BarEntry>> entries) {
            super.onPostExecute(entries);

            String label1, label2;
            if(valore1.equals(KEY_PRESSIONESIS)){
                label1 = getString(R.string.prompt_pressione_sistolica);
                label2 = getString(R.string.prompt_pressione_diastolica);
                BarData barData = new BarData();
                if(entries.get(0) != null){
                    BarDataSet barDataSet1 = new BarDataSet(entries.get(0), label1);
                    barDataSet1.setValueTextSize(12);
                    barDataSet1.setValueTextColor(Color.BLACK);
                    barDataSet1.setColor(Color.rgb(231, 76, 60));
                    barData.addDataSet(barDataSet1);
                }
                if(entries.get(1) != null){
                    BarDataSet barDataSet2 = new BarDataSet(entries.get(1), label2);
                    barDataSet2.setValueTextSize(12);
                    barDataSet2.setValueTextColor(Color.BLACK);
                    barDataSet2.setColor(Color.rgb(52, 152, 219));
                    barData.addDataSet(barDataSet2);
                }
                if(entries.get(0).size() > 0 || entries.get(1).size() > 0) {
                    XAxis xAxis = pressioneChart.getXAxis();
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setDrawGridLines(true);
                    xAxis.setGranularity(1);
                    xAxis.setGranularityEnabled(true);
                    pressioneChart.setDescription(getDescription());
                    pressioneChart.animateXY(1000, 1000);
                    pressioneChart.setTouchEnabled(true);
                    pressioneChart.setPinchZoom(true);
                    pressioneChart.setData(barData);
                    pressioneChart.setScaleEnabled(true);
                    pressioneChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));
                    pressioneChart.invalidate();
                }
                else root.findViewById(R.id.Cardpressione).setVisibility(View.GONE);
            }
            else{
                label1 = getString(R.string.prompt_glicemia_max);
                label2 = getString(R.string.prompt_glicemia_min);
                BarData barData = new BarData();
                if(entries.get(0) != null){
                    BarDataSet barDataSet1 = new BarDataSet(entries.get(0), label1);
                    barDataSet1.setValueTextSize(12);
                    barDataSet1.setValueTextColor(Color.BLACK);
                    barDataSet1.setColor(Color.rgb(231, 76, 60));
                    barData.addDataSet(barDataSet1);
                }
                if(entries.get(1) != null){
                    BarDataSet barDataSet2 = new BarDataSet(entries.get(1), label2);
                    barDataSet2.setValueTextSize(12);
                    barDataSet2.setValueTextColor(Color.BLACK);
                    barDataSet2.setColor(Color.rgb(52, 152, 219));
                    barData.addDataSet(barDataSet2);
                }
                if(entries.get(0).size() > 0 || entries.get(1).size() > 0) {
                    XAxis xAxis = glicemiaChart.getXAxis();
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setDrawGridLines(true);
                    xAxis.setGranularity(1);
                    xAxis.setGranularityEnabled(true);
                    glicemiaChart.setDescription(getDescription());
                    glicemiaChart.animateXY(1000, 1000);
                    glicemiaChart.setTouchEnabled(true);
                    glicemiaChart.setPinchZoom(true);
                    glicemiaChart.setData(barData);
                    glicemiaChart.setScaleEnabled(true);
                    glicemiaChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));
                    glicemiaChart.invalidate();
                }
                else root.findViewById(R.id.Cardglicemia).setVisibility(View.GONE);
            }
        }

        @Override
        protected List<ArrayList<BarEntry>> doInBackground(String... strings) {
            valore1 = strings[0];
            String valore2 = strings[1];
            ArrayList<BarEntry> barEntries1 = new ArrayList<>();
            ArrayList<BarEntry> barEntries2 = new ArrayList<>();
            Calendar Cinizio = Calendar.getInstance();
            Calendar Cfine = Calendar.getInstance();
            if(inizio == null && fine == null){
                if(Converters.DateToLong(reportViewModel.getMinMaxDateReport("MIN", valore1)) < Converters.DateToLong(reportViewModel.getMinMaxDateReport("MIN", valore2)))
                    Cinizio.setTime(Converters.StringToDate(SDF.format(reportViewModel.getMinMaxDateReport("MIN", valore1))));
                else Cinizio.setTime(Converters.StringToDate(SDF.format(reportViewModel.getMinMaxDateReport("MIN", valore2))));
                if(Converters.DateToLong(reportViewModel.getMinMaxDateReport("MAX", valore1)) < Converters.DateToLong(reportViewModel.getMinMaxDateReport("MAX", valore2)))
                    Cfine.setTime(Converters.StringToDate(SDF.format(reportViewModel.getMinMaxDateReport("MAX", valore2))));
                else Cfine.setTime(Converters.StringToDate(SDF.format(reportViewModel.getMinMaxDateReport("MAX", valore1))));
            }
            else {
                Cinizio.setTime(Converters.StringToDate(SDF.format(inizio)));
                Cfine.setTime(Converters.StringToDate(SDF.format(fine)));
            }
            Cfine.add(Calendar.DATE, 1);
            int c= 0;
            while(Converters.DateToLong(Cinizio.getTime())< Converters.DateToLong(Cfine.getTime())){
                if(periodo.getValue().equals(KEY_ANNO) || periodo.getValue().equals(KEY_TUTTO)) {
                    Calendar tmp = Calendar.getInstance();
                    tmp.setTime(Cinizio.getTime());
                    tmp.setTime(Utility.UltimoGiornoMese(tmp));
                    Double value1 = reportViewModel.getAvgVal(valore1, Cinizio.getTime(), tmp.getTime());
                    Double value2 = reportViewModel.getAvgVal(valore2, Cinizio.getTime(), tmp.getTime());
                    if(value1 != null) barEntries1.add(new BarEntry(c, value1.floatValue()));
                    if(value2 != null) barEntries2.add(new BarEntry(c, value2.floatValue()));

                    xAxisLabel.add((Cinizio.getTime().getMonth() + 1) + "/"+ (Cinizio.getTime().getYear() - 100));
                    Cinizio.add(Calendar.MONTH, 1);
                    Cinizio.setTime(Utility.PrimoGiornoMese(Cinizio));
                }
                else {
                    Double value1 = reportViewModel.getAvgVal(valore1, Cinizio.getTime(), null);
                    Double value2 = reportViewModel.getAvgVal(valore2, Cinizio.getTime(), null);
                    if(value1 != null) barEntries1.add(new BarEntry(c, value1.floatValue()));
                    if(value2 != null) barEntries2.add(new BarEntry(c, value2.floatValue()));
                    xAxisLabel.add(Cinizio.getTime().getDate() + "/"+ (Cinizio.getTime().getMonth() + 1));
                    Cinizio.add(Calendar.DATE, 1);
                }
                c++;
            }
            List<ArrayList<BarEntry>> list = new ArrayList<>();
            if(barEntries1 != null)list.add(barEntries1);
            if(barEntries2 != null)list.add(barEntries2);
            return list;
        }
    }
}


