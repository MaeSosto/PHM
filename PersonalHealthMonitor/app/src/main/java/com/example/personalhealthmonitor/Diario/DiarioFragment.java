package com.example.personalhealthmonitor.Diario;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.example.personalhealthmonitor.Home.ReportListAdapter;
import com.example.personalhealthmonitor.R;
import com.example.personalhealthmonitor.Utilities.Converters;
import com.example.personalhealthmonitor.Utilities.OnSwipeTouchListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import static com.example.personalhealthmonitor.Utilities.Utility.*;


public class DiarioFragment extends Fragment {

    private CalendarView calendarView;
    private ReportListAdapter reportListAdapter;
    private RecyclerView recyclerView;
    private CardView CardNoReport;
    private TextView TXVGiorno;
    private Button BTNfiltro;
    private int tmpfiltroDialog; //serve per prendere momentaneamente la scelta selezionata nel bottone del filtro

    
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
        calendarView = root.findViewById(R.id.calendarView);
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
        calendarView.setOnForwardPageChangeListener(this::updateList);

        //SFOGLIO IL CALENDARIO IN INDIETRO
        calendarView.setOnPreviousPageChangeListener(this::updateList);

        //QUANDO CLICCHI SU UN GIORNO
        calendarView.setOnDayClickListener(eventDay -> {
            Calendar clickedDayCalendar = eventDay.getCalendar();

            TXVGiorno.setText(getString(R.string.diario_label3)+ Converters.DateToString(clickedDayCalendar.getTime()));
            settingsViewModel.getmAllSettingsFilter(filtro.getValue()).observe(getViewLifecycleOwner(), settings -> reportViewModel.getFilterReports(clickedDayCalendar.getTime(), null, settings).observe(getViewLifecycleOwner(), reports -> {
                reportListAdapter.setReports(reports);
                if(reports == null || reports.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    CardNoReport.setVisibility(View.INVISIBLE);
                }
                else CardNoReport.setVisibility(View.VISIBLE);
            }));
        });

        //BOTTONE DEL FILTRO
        BTNfiltro = root.findViewById(R.id.BTNfiltro);
        BTNfiltro.setOnClickListener(v -> showSingleChoiceDialog());
        filtro.observe(getViewLifecycleOwner(), integer -> {
            BTNfiltro = root.findViewById(R.id.BTNfiltro);
            if(filtro.getValue() > 1)BTNfiltro.setText(String.valueOf(filtro.getValue()));
            else BTNfiltro.setText("Filtro");
            updateList();
        });
        return root;
    }

    //APRE IL DIALOG DEL BOTTONE
    private void showSingleChoiceDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String[] list = getActivity().getResources().getStringArray(R.array.choiche_dialog);
        tmpfiltroDialog = filtro.getValue()-1;
        builder.setTitle(R.string.dialog_filtro).setSingleChoiceItems(list, tmpfiltroDialog, (dialog, which) -> tmpfiltroDialog = which).setPositiveButton("Ok", (dialog, which) -> {
            filtro.setValue(tmpfiltroDialog+1);
            if(tmpfiltroDialog > 0)BTNfiltro.setText(String.valueOf(filtro.getValue()));
            else BTNfiltro.setText("Filtro");
        })
                .setNegativeButton("Annulla", (dialog, which) -> {

                });
        builder.create();
        builder.show();
    }

    //AGGIORNA LA LISTA DEI REPORT
    private void updateList(){
        //Log.i("UPDATE", "");
        TXVGiorno.setText(R.string.diario_label2);
        Calendar Cmese = calendarView.getCurrentPageDate();
        int ultimo = Cmese.getActualMaximum(Calendar.DAY_OF_MONTH);
        Calendar Fmese = calendarView.getCurrentPageDate();
        Fmese.set(Calendar.DAY_OF_MONTH, ultimo);

        //CAMBIA LA LISTA DEGLI ELEMENTI
        settingsViewModel.getmAllSettingsFilter(filtro.getValue()).observe(getViewLifecycleOwner(), settings -> reportViewModel.getFilterReports(Cmese.getTime(), Fmese.getTime(), settings).observe(getViewLifecycleOwner(), reports -> {
            reportListAdapter.setReports(reports);
            if(reports.size() > 0) {
                recyclerView.setVisibility(View.VISIBLE);
                CardNoReport.setVisibility(View.INVISIBLE);

                List<EventDay> events = new ArrayList<>();
                for (int i = 0; i<reports.size(); i++){
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(reports.get(i).getData());
                    events.add(new EventDay(calendar, R.drawable.ic_reportevent));
                    calendarView.setEvents(events);
                }
            }
            else{
                CardNoReport.setVisibility(View.VISIBLE);
                List<EventDay> events = new ArrayList<>();
                calendarView.setEvents(events);
            }
        }));

    }
}