package UI;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.phm.MainActivity;
import com.example.phm.R;


public class TodayReport extends Fragment {

    TextView todayreportVal;


    public TodayReport() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_today_report, container, false);


        todayreportVal = view.findViewById(R.id.TXVTodayReport_val);
        todayreportVal.setText(MainActivity.SGiorno);

        return view;
    }
}