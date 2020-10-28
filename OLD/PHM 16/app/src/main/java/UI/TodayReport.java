package UI;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.phm.MainActivity;
import com.example.phm.R;

import javax.crypto.NullCipher;

import Database.ReportViewModel;

import static com.example.phm.MainActivity.SGiorno;
import static com.example.phm.MainActivity.reportViewModel;


public class TodayReport extends Fragment {

    public static TextView todayreportVal, TXVBattiti, TXVPressione, TXVTemperatura, TXVGlicemia;
    public LiveData<Double> battiti, pressione, temperatura, glicemia;
    public static String giorno;

    public TodayReport() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_today_report, container, false);

        todayreportVal = view.findViewById(R.id.TXVTodayReport_val);
        TXVBattiti = view.findViewById(R.id.TXVbattito);
        TXVPressione = view.findViewById(R.id.TXVpressione);
        TXVTemperatura = view.findViewById(R.id.TXVtemperatura);
        TXVGlicemia = view.findViewById(R.id.TXVglicemia);

        refreshTodayReport();

        return view;
    }

    public void refreshTodayReport(){
        giorno = SGiorno;
        todayreportVal.setText(giorno);

        battiti = MainActivity.reportViewModel.getAVGDaily("battito", giorno);
        battiti.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                TXVBattiti.setText(nullValue(tronca(aDouble)));
            }
        });

        pressione = MainActivity.reportViewModel.getAVGDaily("pressione", giorno);
        pressione.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                TXVPressione.setText(nullValue(tronca(aDouble)));
            }
        });

        temperatura = MainActivity.reportViewModel.getAVGDaily("temperatura", giorno);
        temperatura.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                TXVTemperatura.setText(nullValue(tronca(aDouble)));
            }
        });

        glicemia = MainActivity.reportViewModel.getAVGDaily("glicemia", giorno);
        glicemia.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                TXVGlicemia.setText(nullValue(tronca(aDouble)));
            }
        });
    }

    //SE NON HO INSERITO DEI VALORI == 0 ALLORA STAMPO NULL A SCHERMO
    private String nullValue(Double val){
        if(val == 0){
            return "null";
        }
        return String.valueOf(val);
    }

    private double tronca(Double num){
        if(num == null ) return 0;
        else{
            num = num * 100;
            num = (double) Math.round(num);
            num = num / 100;
            return num;
        }

    }


}

