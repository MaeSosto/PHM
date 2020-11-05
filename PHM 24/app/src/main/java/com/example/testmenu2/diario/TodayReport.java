package com.example.testmenu2.diario;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.testmenu2.Database.Report;
import com.example.testmenu2.Database.ReportViewModel;
import com.example.testmenu2.R;
import com.example.testmenu2.Utilities.Converters;
import com.example.testmenu2.home.ReportListAdapter;

import java.util.List;

public class TodayReport extends AppCompatActivity {
    private Bundle bundle;
    private static ReportListAdapter reportListAdapter;
    public static ReportViewModel reportViewModel;
    public static LiveData<List<Report>> mReports;
    private String Giorno;
    private LiveData<Report> LDReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_report);

        bundle = getIntent().getExtras();
        Giorno = bundle.getString("giorno");
        this.setTitle("Report del " + Giorno);


        //CONTAINER MAIN
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        reportListAdapter = new ReportListAdapter(this);
        recyclerView.setAdapter(reportListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reportViewModel = ViewModelProviders.of(this).get(ReportViewModel.class);

        //CAMBIA LA LISTA DEGLI ELEMENTI
        mReports = reportViewModel.getAllReportsInDate(Converters.StringToDate(Giorno));
        mReports.observe(this, new Observer<List<Report>>() {
            @Override
            public void onChanged(List<Report> reports) {
                reportListAdapter.setReports(reports);
            }
        });
    }
}