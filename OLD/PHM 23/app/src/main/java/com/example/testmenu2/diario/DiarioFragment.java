package com.example.testmenu2.diario;

import android.app.ActivityOptions;
import android.app.usage.UsageEvents;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.EventLog;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testmenu2.Database.Report;
import com.example.testmenu2.Database.ReportViewModel;
import com.example.testmenu2.R;
import com.example.testmenu2.home.ReportListAdapter;
import com.example.testmenu2.home.SendReportActivity;


import java.util.List;

public class DiarioFragment extends Fragment {

    private DiarioViewModel diarioViewModel;
    private static ReportListAdapter reportListAdapter;
    public static ReportViewModel reportViewModel;
    public static LiveData<List<Report>> mReports;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        diarioViewModel =
                new ViewModelProvider(this).get(DiarioViewModel.class);
        View root = inflater.inflate(R.layout.fragment_diario, container, false);

        CalendarView calendarView = root.findViewById(R.id.calendarView);

        //CONTAINER MAIN
        RecyclerView recyclerView = root.findViewById(R.id.recyclerview);
        reportListAdapter = new ReportListAdapter(getContext());
        recyclerView.setAdapter(reportListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        reportViewModel = ViewModelProviders.of(this).get(ReportViewModel.class);

        //CAMBIA LA LISTA DEGLI ELEMENTI
        mReports = reportViewModel.getAllReportsOrder(); //RESTITUISCE LA LISTA DEI REPORT IN ORDINE DI DATA DI INSERIMENTO
        mReports.observe(getViewLifecycleOwner(), new Observer<List<Report>>() {
            @Override
            public void onChanged(List<Report> reports) {
                reportListAdapter.setReports(reports);
            }
        });

        //QUANDO CLICCHI SU UNA DATA PRECISA
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "/" + (month+1) + "/" + year;
                Intent intent = new Intent(getContext(), TodayReport.class);
                intent.putExtra("giorno", date);


                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View,String>(calendarView,"activity_trans");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), pairs);
                startActivity(intent,options.toBundle());
            }
        });

        return root;
    }


}