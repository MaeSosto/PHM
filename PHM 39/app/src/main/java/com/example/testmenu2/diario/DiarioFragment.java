package com.example.testmenu2.diario;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.testmenu2.Database.Report;
import com.example.testmenu2.Database.ReportViewModel;
import com.example.testmenu2.R;
import com.example.testmenu2.Utilities.Converters;
import com.example.testmenu2.home.ReportListAdapter;
import com.example.testmenu2.statistiche.StatisticheViewModel;


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
    private TextView TXVGiorno;
    private CalendarView calendarView;
    private Button BTNfiltro;
    int filtro = 1;
    int tmpfiltroDialog = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        diarioViewModel =
                new ViewModelProvider(this).get(DiarioViewModel.class);
        View root = inflater.inflate(R.layout.fragment_diario, container, false);


        SDF = new SimpleDateFormat("dd/MM/yyyy");
        TXVGiorno = root.findViewById(R.id.TXVgiorno);
        TXVGiorno.setText(R.string.diario_label2);
        BTNfiltro = root.findViewById(R.id.BTNfiltro);

        //CONTAINER MAIN
        RecyclerView recyclerView = root.findViewById(R.id.recyclerview);
        reportListAdapter = new ReportListAdapter(getContext());
        recyclerView.setAdapter(reportListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        reportViewModel = ViewModelProviders.of(this).get(ReportViewModel.class);

        calendarView = (CalendarView) root.findViewById(R.id.calendarView);
        updateList();

        //BOTTONE DEL FILTRO
        BTNfiltro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSingleChoiceDialog();
            }
        });

        //QUANDO CLICCHI SU UN GIORNO
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar clickedDayCalendar = eventDay.getCalendar();
                String giorno =SDF.format(clickedDayCalendar.getTime());
                TXVGiorno.setText(getString(R.string.diaario_label3)+ giorno);
                Log.i("DEBUG", String.valueOf(Converters.StringToDate(giorno)));
                mReports = reportViewModel.getAllReports(Converters.StringToDate(giorno), null); //RESTITUISCE LA LISTA DEI REPORT IN ORDINE DI DATA DI INSERIMENTO
                //mReports = diarioViewModel.getFilterQueryInDate(getParentFragment(), getViewLifecycleOwner(), filtro, Converters.StringToDate(giorno));
                mReports.observe(getViewLifecycleOwner(), new Observer<List<Report>>() {
                    @Override
                    public void onChanged(List<Report> reports) {
                        reportListAdapter.setReports(reports);
                    }
                });
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

        return root;
    }

    //APRE IL DIALOG DEL BOTTONE
    private void showSingleChoiceDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String[] list = getActivity().getResources().getStringArray(R.array.choiche_dialog);
        tmpfiltroDialog = filtro-1;
        builder.setTitle(R.string.dialog_filtro).setSingleChoiceItems(list, tmpfiltroDialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tmpfiltroDialog = which;
            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                filtro = tmpfiltroDialog+1;
                if(tmpfiltroDialog > 0)BTNfiltro.setText(String.valueOf(filtro));
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

    //AGGIORNA LA LISTA DEI REPORT
    private void updateList(){
        Calendar Cinizio = calendarView.getCurrentPageDate();
        Date Dinizio = Cinizio.getTime();
        int ultimo = Cinizio.getActualMaximum(Calendar.DAY_OF_MONTH);
        Cinizio.set(Calendar.DAY_OF_MONTH, ultimo);
        Date Dfine = Converters.StringToDate(SDF.format(Cinizio.getTime()));

        //CAMBIA LA LISTA DEGLI ELEMENTI
        mReports = reportViewModel.getAllReports(Dinizio, Dfine); //RESTITUISCE LA LISTA DEI REPORT IN ORDINE DI DATA DI INSERIMENTO
        mReports.observe(getViewLifecycleOwner(), new Observer<List<Report>>() {
            @Override
            public void onChanged(List<Report> reports) {
                reportListAdapter.setReports(reports);
            }
        });

        //DISEGNA GLI EVENTI
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
    }

}