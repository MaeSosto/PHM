package com.example.personalhealthmonitor.Home;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.personalhealthmonitor.Database.Report;
import com.example.personalhealthmonitor.Database.Settings;
import com.example.personalhealthmonitor.R;
import com.example.personalhealthmonitor.Utilities.Converters;
import com.example.personalhealthmonitor.Utilities.OnSwipeTouchListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import static com.example.personalhealthmonitor.Utilities.Utility.*;


public class HomeFragment extends Fragment {

    private static MutableLiveData<Date> dataSel;
    private static Calendar calendar;
    private TextView TXVBattiti, TXVPressioneSistolica, TXVPressioneDiastolica, TXVTemperatura, TXVGlicemiaMax, TXVGlicemiaMin;
    private Button BTNfiltro;
    private int tmpfiltroDialog; //serve per prendere momentaneamente la scelta selezionata nel bottone del filtro
    private ReportListAdapter reportListAdapter;
    private RecyclerView recyclerView;
    private CardView CardNoReport;

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

        //QUANDO CAMBIA LA DATA
        dataSel.observe(getViewLifecycleOwner(), date -> {
            TextView todayreportVal = root.findViewById(R.id.TXVTodayReport);
            todayreportVal.setText(getString(R.string.home_label1)+ SDF.format(date));

            TXVBattiti = root.findViewById(R.id.TXVbattito);
            TXVPressioneSistolica = root.findViewById(R.id.TXVpressioneSistolica);
            TXVPressioneDiastolica = root.findViewById(R.id.TXVpressioneDiastolica);
            TXVTemperatura = root.findViewById(R.id.TXVtemperatura);
            TXVGlicemiaMax = root.findViewById(R.id.TXVglicemia_max);
            TXVGlicemiaMin = root.findViewById(R.id.TXVglicemia_min);
            updateList();
        });

        //BOTTONE DEL FILTRO
        BTNfiltro = root.findViewById(R.id.BTNfiltro);
        BTNfiltro.setOnClickListener(v -> showSingleChoiceDialog());
        filtro.observe(getViewLifecycleOwner(), integer -> {
            BTNfiltro = root.findViewById(R.id.BTNfiltro);
            if(filtro.getValue() > 1)BTNfiltro.setText(String.valueOf(filtro.getValue()));
            else BTNfiltro.setText(R.string.home_BTNfiltro);
            updateList();
        });

        //BOTTONI DI DESTRA E SINISTRA
        root.findViewById(R.id.BTNleft).setOnClickListener(v -> {
            calendar.add(Calendar.DATE, -1);
            dataSel.setValue(Converters.StringToDate(SDF.format(calendar.getTime())));
        });
        root.findViewById(R.id.BTNright).setOnClickListener(v -> {
            calendar.add(Calendar.DATE, 1);
            dataSel.setValue(Converters.StringToDate(SDF.format(calendar.getTime())));
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

    //AGGIORNA TUTTO
    private void updateList() {
        //CAMBIA LA LISTA DEGLI ELEMENTI IN BASE AL FILTRO DEI SETTINGS
        settingsViewModel.getmAllSettingsFilter(filtro.getValue()).observe(getViewLifecycleOwner(), new Observer<List<Settings>>() {
            @Override
            public void onChanged(List<Settings> settings) { //PRIMA PRENDO I VALORI DAL FILTRO E POI IN BASE A QUELLO PRENDO I REPORT CHE MI INTERESSANO
                reportViewModel.getFilterReports(dataSel.getValue(), null, settings).observe(getViewLifecycleOwner(), new Observer<List<Report>>() {
                    @Override
                    public void onChanged(List<Report> reports) {
                        new getAVG().execute();
                        reportListAdapter.setReports(reports);
                        if(reports.size() > 0) {
                            recyclerView.setVisibility(View.VISIBLE);
                            CardNoReport.setVisibility(View.GONE);
                        }
                        else CardNoReport.setVisibility(View.VISIBLE);
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
                else BTNfiltro.setText(R.string.home_BTNfiltro);
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

    //MOSTRA LE MEDIE DEI VALORI NELLA PARTE ALTA DEL FRAGMENT
    class getAVG extends AsyncTask<Void, Void, Void> {
        Double battito, pressionesis, pressionedia, glicemiamax, glicemiamin, temperatura;

        @Override
        protected void onPostExecute(Void aVoid) {
            if(battito != null) TXVBattiti.setText(tronca(battito) + U_BATTITO);
            else TXVBattiti.setText("");
            if (pressionesis != null) TXVPressioneSistolica.setText(tronca(pressionesis) + U_PRESSIONE);
            else TXVPressioneSistolica.setText("");
            if (pressionedia != null) TXVPressioneDiastolica.setText(tronca(pressionedia) + U_PRESSIONE);
            else TXVPressioneDiastolica.setText("");
            if (temperatura != null) TXVTemperatura.setText(tronca(temperatura) + U_TEMPERATURA);
            else TXVTemperatura.setText("");
            if (glicemiamax != null) TXVGlicemiaMax.setText(tronca(glicemiamax) + U_GLICEMIA);
            else TXVGlicemiaMax.setText("");
            if (glicemiamin != null) TXVGlicemiaMin.setText(tronca(glicemiamin) + U_GLICEMIA);
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

    //QUANDO VADO IN NEWREPORTACTIVITY CHIAMO QUESTA FUNZIONE PER SETTARE IL GIORNO ATTUALE
    public static void setToday(){
        calendar.setTime(Calendar.getInstance().getTime());
        dataSel.setValue(Converters.StringToDate(SDF.format(calendar.getTime())));
    }
}