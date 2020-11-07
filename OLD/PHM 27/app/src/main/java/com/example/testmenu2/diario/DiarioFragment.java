package com.example.testmenu2.diario;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.testmenu2.Database.Report;
import com.example.testmenu2.Database.ReportViewModel;
import com.example.testmenu2.R;
import com.example.testmenu2.Utilities.Converters;
import com.example.testmenu2.home.ReportListAdapter;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DiarioFragment extends Fragment {

    private DiarioViewModel diarioViewModel;
    private static ReportListAdapter reportListAdapter;
    public static ReportViewModel reportViewModel;
    public static LiveData<List<Report>> mReports;
    private SimpleDateFormat SDF;
    TextView TXVGiorno;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        diarioViewModel =
                new ViewModelProvider(this).get(DiarioViewModel.class);
        View root = inflater.inflate(R.layout.fragment_diario, container, false);


        SDF = new SimpleDateFormat("dd/MM/yyyy");
        TXVGiorno = root.findViewById(R.id.TXVgiorno);
        TXVGiorno.setText("Tutti i report");

        /*List<EventDay> events = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        events.add(new EventDay(calendar, R.drawable.sample_three_icons));
        com.applandeo.materialcalendarview.CalendarView calendarView = (CalendarView) root.findViewById(R.id.calendarView);
        calendarView.setEvents(events);


         */
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

        CalendarView calendarView = (CalendarView) root.findViewById(R.id.calendarView);

        mReports.observe(getViewLifecycleOwner(), new Observer<List<Report>>() {
            @Override
            public void onChanged(List<Report> reports) {
                List<EventDay> events = new ArrayList<>();
                Date date = new Date();
                for (int i = 0; i<reports.size(); i++){
                    date = reports.get(i).getGiorno();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    events.add(new EventDay(calendar, R.drawable.ic_reportevent));
                    calendarView.setEvents(events);
                }
            }
        });


        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar clickedDayCalendar = eventDay.getCalendar();
                String giorno =SDF.format(clickedDayCalendar.getTime());

                TXVGiorno.setText("Report del "+ giorno);
                mReports = reportViewModel.getAllReportsInDate(Converters.StringToDate(giorno)); //RESTITUISCE LA LISTA DEI REPORT IN ORDINE DI DATA DI INSERIMENTO
                mReports.observe(getViewLifecycleOwner(), new Observer<List<Report>>() {
                    @Override
                    public void onChanged(List<Report> reports) {
                        reportListAdapter.setReports(reports);
                    }
                });


            }
        });

        return root;
    }


}