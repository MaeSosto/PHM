package com.example.personalhealthmonitor.Statistiche;

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

import com.annimon.stream.function.Consumer;
import com.example.personalhealthmonitor.R;
import com.example.personalhealthmonitor.Utilities.Converters;
import com.example.personalhealthmonitor.Utilities.Utility;

import java.util.Date;
import static com.example.personalhealthmonitor.MainActivity.reportViewModel;

public class StatisticheFragment extends Fragment {
    private static final String KEY_SETTIMANA = "Settimana";
    private static final String KEY_MESE = "Mese";
    private static final String KEY_ANNO = "Anno";
    private static final String KEY_TUTTO = "Tutto";

    private MutableLiveData<String> periodo;
    private int giornoInizio, giornoFine, meseInizio, meseFine, annoInizio, annoFine;
    private String[] asseX;

    TextView TXVPeriodo;

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

        TXVPeriodo = root.findViewById(R.id.TXVPeriodo);
        setDate();

        periodo.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String string) {
                setDate();

                TextView NumReport = root.findViewById(R.id.TXVNumReport);
                reportViewModel.getCountVal(null, giornoInizio, meseInizio, annoInizio, giornoFine, meseFine, annoFine).observe(getViewLifecycleOwner(), new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        NumReport.setText(String.valueOf(integer));
                    }
                });

            }
        });

        Button BTNsettimana = root.findViewById(R.id.statistiche_btn1);
        BTNsettimana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                periodo.setValue(KEY_SETTIMANA);
            }
        });

        Button BTNmese = root.findViewById(R.id.statistiche_btn2);
        BTNmese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                periodo.setValue(KEY_MESE);
            }
        });

        Button BTNanno = root.findViewById(R.id.statistiche_btn3);
        BTNanno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                periodo.setValue(KEY_ANNO);
            }
        });

        Button BTNtutto = root.findViewById(R.id.statistiche_btn4);
        BTNtutto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                periodo.setValue(KEY_TUTTO);
            }
        });

        return root;
    }


    private void setDate(){
        Date inizio= null, fine = null;
        Log.i("PERIODO", periodo.getValue());
        switch (periodo.getValue()) {
            case KEY_SETTIMANA:
                inizio = Utility.PrimoGiornoSettimana();
                fine = Utility.UltimoGiornoSettimana();
                TXVPeriodo.setText("Dal " + Converters.DateToString(inizio) + " al " + Converters.DateToString(fine));
                asseX = new String[]{"Lun", "Mar", "Mer", "Gio", "Ven", "Sab", "Dom"};
                break;
            case KEY_MESE:
                inizio = Utility.PrimoGiornoMese();
                fine = Utility.UltimoGiornoMese();
                TXVPeriodo.setText("Dal " + Converters.DateToString(inizio) + " al " + Converters.DateToString(fine));
                break;
            case KEY_ANNO:
                inizio = Utility.PrimoGiornoAnno();
                fine = Utility.UltimoGiornoAnno();
                TXVPeriodo.setText("Dal " + Converters.DateToString(inizio) + " al " + Converters.DateToString(fine));
                break;
            case KEY_TUTTO:
                TXVPeriodo.setText(KEY_TUTTO);
                return;
        }
        giornoInizio = inizio.getDate();
        giornoFine = fine.getDate();
        meseInizio = inizio.getMonth()+1;
        meseFine = fine.getMonth()+1;
        annoInizio = inizio.getYear()-100;
        annoFine = fine.getYear()-100;
    }
}