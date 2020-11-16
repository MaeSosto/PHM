package com.example.testmenu2.diario;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.applandeo.materialcalendarview.EventDay;
import com.example.testmenu2.Database.Report;
import com.example.testmenu2.Database.ReportViewModel;
import com.example.testmenu2.Database.Settings;
import com.example.testmenu2.Database.SettingsViewModel;
import com.example.testmenu2.R;
import com.example.testmenu2.Utilities.Converters;

import java.security.acl.Owner;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DiarioViewModel extends ViewModel {

    private static MutableLiveData<String> SGiorno;
    public static Calendar calendar;
    public static SimpleDateFormat SDF;
    private int count = 0;

    public DiarioViewModel() {
        calendar = Calendar.getInstance();
        SDF = new SimpleDateFormat("dd/MM/yy");
        SGiorno = new MutableLiveData<>();
        SGiorno.setValue(SDF.format(calendar.getTime()));

    }

    public LiveData<List<Report>> getFilterQueryInDate(Fragment fragment, LifecycleOwner owner, int filtro, Date date){
        StringBuilder S = new StringBuilder();
        count = 0;

        Log.i("FILTRO", String.valueOf(filtro));
        SettingsViewModel settingsViewModel = ViewModelProviders.of(fragment).get(SettingsViewModel.class);
        LiveData<List<Settings>> settingsLiveData = settingsViewModel.getAllSettings();

        settingsLiveData.observe(owner, new Observer<List<Settings>>() {
            @Override
            public void onChanged(List<Settings> settings) {
                for (int i = 0; i<settings.size(); i++) {
                    Settings tmpSetting = settings.get(i);
                    String value = tmpSetting.getValore();
                    switch (value) {
                        case "Battito":
                            if(tmpSetting.getImportanza()>= filtro){
                                S.append("report_battito != 0 ");
                                count++;
                            }
                            break;
                        case "Pressione":
                            if(count > 0 && tmpSetting.getImportanza() >= filtro) S.append("AND report_pressione != 0 ");
                            else if(tmpSetting.getImportanza()>=filtro) {
                                S.append("report_pressione != 0 ");
                                count++;
                            }
                            break;
                        case "Temperatura":
                            if(count > 0 && tmpSetting.getImportanza() >= filtro) S.append("AND report_temperatura != 0 ");
                            else if(tmpSetting.getImportanza()>=filtro) {
                                S.append("report_temperatura != 0 ");
                                count++;
                            }
                            break;
                        case "Glicemia":
                            if(count > 0 && tmpSetting.getImportanza() >= filtro) S.append("AND report_glicemia != 0 ");
                            else if(tmpSetting.getImportanza()>= filtro) {
                                S.append("report_glicemia != 0 ");
                                count++;
                            }
                            break;
                    }
                }
            }
        });

        if(count > 0) S.append("AND report_giorno ="+ Converters.DateToLong(date));
        else S.append("report_giorno ="+ Converters.DateToLong(date));
        Log.i("QUERY", S.toString());

        ReportViewModel reportViewModel = ViewModelProviders.of(fragment).get(ReportViewModel.class);
        LiveData<List<Report>> LDReprot = reportViewModel.getFilterReports(S.toString());

        LDReprot.observe(owner, new Observer<List<Report>>() {
            @Override
            public void onChanged(List<Report> reports) {
                Log.i("LISTA REPORT", String.valueOf(reports.size()));
            }
        });

        return LDReprot;
    }

}