package com.example.testmenu2.diario;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DiarioViewModel extends ViewModel {

    private static MutableLiveData<String> SGiorno;
    public static Calendar calendar;
    public static SimpleDateFormat SDF;

    public DiarioViewModel() {
        calendar = Calendar.getInstance();
        SDF = new SimpleDateFormat("dd/MM/yy");
        SGiorno = new MutableLiveData<>();
        SGiorno.setValue(SDF.format(calendar.getTime()));

    }


}