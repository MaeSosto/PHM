package com.example.personalhealthmonitor.Home;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.personalhealthmonitor.Database.Report;
import com.example.personalhealthmonitor.Database.Settings;
import com.example.personalhealthmonitor.R;
import com.example.personalhealthmonitor.Utilities.Converters;
import com.example.personalhealthmonitor.Utilities.OnSwipeTouchListener;
import com.example.personalhealthmonitor.Utilities.Utility;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.personalhealthmonitor.MainActivity.KEY_BATTITO;
import static com.example.personalhealthmonitor.MainActivity.KEY_GLICEMIAMAX;
import static com.example.personalhealthmonitor.MainActivity.KEY_GLICEMIAMIN;
import static com.example.personalhealthmonitor.MainActivity.KEY_PRESSIONEDIA;
import static com.example.personalhealthmonitor.MainActivity.KEY_PRESSIONESIS;
import static com.example.personalhealthmonitor.MainActivity.KEY_TEMPERATURA;
import static com.example.personalhealthmonitor.MainActivity.SDF;
import static com.example.personalhealthmonitor.MainActivity.filtro;
import static com.example.personalhealthmonitor.MainActivity.reportViewModel;
import static com.example.personalhealthmonitor.MainActivity.settingsViewModel;
import static com.example.personalhealthmonitor.Utilities.Utility.tronca;

public class HomeFragment extends Fragment {

    private MutableLiveData<Date> dataSel;
    private static Calendar calendar;

    private TextView TXVBattiti, TXVPressioneSistolica, TXVPressioneDiastolica, TXVTemperatura, TXVGlicemiaMax, TXVGlicemiaMin;
    private Button BTNfiltro;
    private int tmpfiltroDialog; //serve per prendere momentaneamente la scelta selezionata nel bottone del filtro
    ReportListAdapter reportListAdapter;
    RecyclerView recyclerView;
    CardView CardNoReport;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setto la data di oggi
        calendar = Calendar.getInstance();

        dataSel = new MutableLiveData<>();
        dataSel.setValue(Converters.StringToDate(SDF.format(calendar.getTime())));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        CardNoReport = root.findViewById(R.id.CardNoReport);

        //Recycler view
        recyclerView = root.findViewById(R.id.recyclerview);
        reportListAdapter = new ReportListAdapter(getContext());
        recyclerView.setAdapter(reportListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

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

                updateList();
            }
        });

        //BOTTONE DEL FILTRO
        BTNfiltro = root.findViewById(R.id.BTNfiltro);
        BTNfiltro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSingleChoiceDialog();
            }
        });

        filtro.observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                BTNfiltro = root.findViewById(R.id.BTNfiltro);
                if(filtro.getValue() > 1)BTNfiltro.setText(String.valueOf(filtro.getValue()));
                else BTNfiltro.setText("Filtro");
                updateList();
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

    private void updateList() {


        //CAMBIA LA LISTA DEGLI ELEMENTI
        settingsViewModel.getmAllSettingsFilter(filtro.getValue()).observe(getViewLifecycleOwner(), new Observer<List<Settings>>() {
            @Override
            public void onChanged(List<Settings> settings) {
                reportViewModel.getFilterReports(dataSel.getValue(), null, settings).observe(getViewLifecycleOwner(), new Observer<List<Report>>() {
                    @Override
                    public void onChanged(List<Report> reports) {
                        new getAVG().execute();
                        reportListAdapter.setReports(reports);
                        if(reports.size() > 0) {
                            recyclerView.setVisibility(View.VISIBLE);
                            CardNoReport.setVisibility(View.INVISIBLE);

                        }
                        else{
                            CardNoReport.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });
    }

    //APRE IL DIALOG DEL BOTTONE
    private void showSingleChoiceDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String[] list = getActivity().getResources().getStringArray(R.array.choiche_dialog);
        tmpfiltroDialog = filtro.getValue()-1;
        builder.setTitle(R.string.dialog_filtro).setSingleChoiceItems(list, tmpfiltroDialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tmpfiltroDialog = which;
            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                filtro.setValue(tmpfiltroDialog+1);
                if(tmpfiltroDialog > 0)BTNfiltro.setText(String.valueOf(filtro.getValue()));
                else BTNfiltro.setText("Filtro");
            }
        })
                .setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.create();
        builder.show();
    }

    class getAVG extends AsyncTask<Void, Void, Void> {

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
        protected Void doInBackground(Void... voids) {
            Date date = dataSel.getValue();
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