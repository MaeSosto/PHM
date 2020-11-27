package com.example.personalhealthmonitor.Diario;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.personalhealthmonitor.Database.Report;
import com.example.personalhealthmonitor.Home.ReportListAdapter;
import com.example.personalhealthmonitor.R;
import com.example.personalhealthmonitor.Utilities.Converters;
import com.example.personalhealthmonitor.Utilities.OnSwipeTouchListener;
import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.personalhealthmonitor.MainActivity.reportViewModel;

public class DiarioFragment extends Fragment {

    private CalendarView calendarView;
    private ReportListAdapter reportListAdapter;
    private RecyclerView recyclerView;
    private CardView CardNoReport;
    private TextView TXVGiorno;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_diario, container, false);

        TXVGiorno = root.findViewById(R.id.TXVgiorno);

        //CONTAINER MAIN
        CardNoReport = root.findViewById(R.id.CardNoReport);
        recyclerView = root.findViewById(R.id.recyclerview);
        reportListAdapter = new ReportListAdapter(getContext());
        recyclerView.setAdapter(reportListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        //Calendario
        calendarView = (CalendarView) root.findViewById(R.id.calendarView);
        updateList();

        //GESTISCE LO SWIPE TOP BOT LEFT E RIGHT
        recyclerView.setOnTouchListener(new OnSwipeTouchListener(getContext()) {

            @SuppressLint("ClickableViewAccessibility")
            public void onSwipeRight() {
                Calendar calendar = calendarView.getCurrentPageDate();
                calendar.add(Calendar.MONTH, -1);
                try {
                    calendarView.setDate(calendar);
                } catch (OutOfDateRangeException e) {
                    e.printStackTrace();
                }
            }

            @SuppressLint("ClickableViewAccessibility")
            public void onSwipeLeft() {
                Calendar calendar = calendarView.getCurrentPageDate();
                calendar.add(Calendar.MONTH, 1);
                try {
                    calendarView.setDate(calendar);
                } catch (OutOfDateRangeException e) {
                    e.printStackTrace();
                }
            }
        });

        //SFOGLIO IL CALENDARIO IN AVANTI
        calendarView.setOnForwardPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                updateList();
            }
        });

        //SFOGLIO IL CALENDARIO IN INDIETRO
        calendarView.setOnPreviousPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                updateList();
            }
        });

        //QUANDO CLICCHI SU UN GIORNO
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar clickedDayCalendar = eventDay.getCalendar();

                TXVGiorno.setText(getString(R.string.diario_label3)+ Converters.DateToString(clickedDayCalendar.getTime()));
                reportViewModel.getAllReports(clickedDayCalendar.getTime(), null).observe(getViewLifecycleOwner(), new Observer<List<Report>>() {
                    @Override
                    public void onChanged(List<Report> reports) {
                        reportListAdapter.setReports(reports);
                        if(reports == null || reports.size() > 0) {
                            recyclerView.setVisibility(View.VISIBLE);
                            CardNoReport.setVisibility(View.INVISIBLE);
                        }
                        else CardNoReport.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        return root;
    }

    //AGGIORNA LA LISTA DEI REPORT
    private void updateList(){
        TXVGiorno.setText(R.string.diario_label2);
        Calendar Cmese = calendarView.getCurrentPageDate();
        int ultimo = Cmese.getActualMaximum(Calendar.DAY_OF_MONTH);
        Calendar Fmese = calendarView.getCurrentPageDate();
        Fmese.set(Calendar.DAY_OF_MONTH, ultimo);

        //CAMBIA LA LISTA DEGLI ELEMENTI
        reportViewModel.getAllReports(Cmese.getTime(), Fmese.getTime()).observe(getViewLifecycleOwner(), new Observer<List<Report>>() {
            @Override
            public void onChanged(List<Report> reports) {
                reportListAdapter.setReports(reports);
                if(reports.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    CardNoReport.setVisibility(View.INVISIBLE);
                }
                else CardNoReport.setVisibility(View.VISIBLE);

                List<EventDay> events = new ArrayList<>();
                for (int i = 0; i<reports.size(); i++){
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(reports.get(i).getData());
                    events.add(new EventDay(calendar, R.drawable.ic_reportevent));
                    calendarView.setEvents(events);
                }
            }
        });
    }
}