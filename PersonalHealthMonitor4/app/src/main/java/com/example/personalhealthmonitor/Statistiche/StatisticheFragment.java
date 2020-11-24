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
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    private MutableLiveData<String> periodo;
    private Date inizio, fine;
    private String[] asseX;
    private TextView TXVNumReport;
    public PieChart pieChart;
    public LineChart battitoChart;

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

                TXVNumReport = root.findViewById(R.id.TXVNumReport);



                pieChart = root.findViewById(R.id.piechart);
                battitoChart = root.findViewById(R.id.battitochart);
                new ShowChartsTask().execute();

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

    //public PieData getPieData(){
    //
    //    PieDataSet pieDataSet = new PieDataSet(PiedataVals, periodo.getValue());
    //    pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
    //    pieDataSet.setValueTextSize(12);
    //    pieDataSet.setValueTextColor(Color.BLACK);
    //    PieData pieData = new PieData(pieDataSet);
    //    return pieData;
    //}


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


    class Params {
        private String val;
        private Date inizio;
        private Date fine;

        public String getVal() {
            return val;
        }

        public void setVal(String val) {
            this.val = val;
        }

        public Date getInizio() {
            return inizio;
        }

        public void setInizio(Date inizio) {
            this.inizio = inizio;
        }

        public Date getFine() {
            return fine;
        }

        public void setFine(Date fine) {
            this.fine = fine;
        }

        public Params(String val, Date inizio, Date fine) {
            this.val = val;
            this.inizio = inizio;
            this.fine = fine;


        }
    }

    class ShowChartsTask extends AsyncTask<Void, Integer, ArrayList<PieEntry>>{
        String TXVNumReports;

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

            TXVNumReport.setText(TXVNumReports);
        }

        @Override
        protected ArrayList<PieEntry> doInBackground(Void... voids) {
            int battito, glicemia, pressione, temperatura;
            ArrayList<PieEntry> PiedataVals = new ArrayList<>();

            battito = reportViewModel.getCOUNTVal(KEY_BATTITO,null, inizio, fine);
            pressione = reportViewModel.getCOUNTVal(KEY_PRESSIONESIS,KEY_PRESSIONEDIA, inizio, fine);
            glicemia = reportViewModel.getCOUNTVal(KEY_GLICEMIAMAX, KEY_GLICEMIAMIN, inizio, fine);
            temperatura = reportViewModel.getCOUNTVal(KEY_TEMPERATURA, null, inizio, fine);

            if(battito+ temperatura+ pressione+ glicemia > 0){
                if(battito > 0) PiedataVals.add(new PieEntry(battito, "Battito"));
                if(temperatura> 0) PiedataVals.add(new PieEntry(temperatura, "Temperatura"));
                if(pressione > 0) PiedataVals.add(new PieEntry(pressione, "Pressione"));
                if(glicemia > 0) PiedataVals.add(new PieEntry(glicemia, "Glicemia"));
            }
            else PiedataVals.add(new PieEntry(1, "Non ci sono dati"));

            TXVNumReports = String.valueOf(reportViewModel.getCOUNTVal(null, null, inizio, fine));

            return PiedataVals;
        }
    }
}


