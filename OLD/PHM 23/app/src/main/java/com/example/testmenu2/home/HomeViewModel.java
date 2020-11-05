package com.example.testmenu2.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.testmenu2.Utilities.Converters;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HomeViewModel extends ViewModel {

    //private MutableLiveData<String> mText;
    private static MutableLiveData<Long> LGiorno;
    public static Calendar calendar;
    public static SimpleDateFormat SDF;


    public HomeViewModel() {
        /*mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");

         */

        //Imposto il giorno
        calendar = Calendar.getInstance();
        SDF = new SimpleDateFormat("dd/MM/yyyy");
        LGiorno = new MutableLiveData<>();
        LGiorno.setValue(Converters.StringToLong(SDF.format(calendar.getTime())));



    }

    public LiveData<Long> getSGiorno(){
        return LGiorno;
    }

    public void Ieri(){
        calendar.add(Calendar.DATE, -1);
        LGiorno.setValue(Converters.StringToLong(SDF.format(calendar.getTime())));
    }

    public void Domani(){
        calendar.add(Calendar.DATE, 1);
        LGiorno.setValue(Converters.StringToLong(SDF.format(calendar.getTime())));
    }

    /*public LiveData<String> getText() {
        return mText;
    }
     */
}