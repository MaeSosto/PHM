package com.example.testmenu2.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.testmenu2.Database.Report;
import com.example.testmenu2.Database.ReportViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class HomeViewModel extends ViewModel {

    //private MutableLiveData<String> mText;
    private static MutableLiveData<String> SGiorno;
    public static Calendar calendar;
    public static SimpleDateFormat SDF;


    public HomeViewModel() {
        /*mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");

         */

        //Imposto il giorno
        calendar = Calendar.getInstance();
        SDF = new SimpleDateFormat("dd/MM/yy");
        SGiorno = new MutableLiveData<>();
        SGiorno.setValue(SDF.format(calendar.getTime()));



    }

    public LiveData<String> getSGiorno(){
        return SGiorno;
    }

    public void Ieri(){
        calendar.add(Calendar.DATE, -1);
        SGiorno.setValue(SDF.format(calendar.getTime()));
    }

    public void Domani(){
        calendar.add(Calendar.DATE, 1);
        SGiorno.setValue(SDF.format(calendar.getTime()));
    }

    /*public LiveData<String> getText() {
        return mText;
    }
     */
}