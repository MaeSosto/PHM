package com.example.testmenu2.statistiche;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.testmenu2.Utilities.Converters;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class StatisticheViewModel extends ViewModel {

    private MutableLiveData<String> periodo;
    SimpleDateFormat SDF;


    public StatisticheViewModel() {
        periodo = new MutableLiveData<>();
        SDF = new SimpleDateFormat("dd/MM/yyyy");
        periodo.setValue("settimana");

    }

    public void setPeriodo(String s){
        periodo.setValue(s);
    }

    public LiveData<String> getPeriodo(){
        return periodo;
    }

    public PieData getPieData(String label){
        PieDataSet pieDataSet = new PieDataSet(dataValues1(), label);
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData pieData = new PieData(pieDataSet);

        return pieData;
    }

    public ArrayList<PieEntry> dataValues1(){
        ArrayList<PieEntry> dataVals = new ArrayList<>();

        dataVals.add(new PieEntry(15, "Sun"));
        dataVals.add(new PieEntry(34, "Mon"));
        dataVals.add(new PieEntry(23, "Tue"));
        dataVals.add(new PieEntry(86, "Wed"));
        dataVals.add(new PieEntry(26, "Thu"));
        dataVals.add(new PieEntry(17, "Fri"));
        dataVals.add(new PieEntry(63, "Sat"));
        return dataVals;
    }

    public Description getDescription(String s){
        Description description = new Description();
        description.setText(s);

        return description;
    }

    public Date PrimoGiornoSettimana (){
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        Date giorno = Converters.StringToDate(SDF.format(calendar.getTime()));
        return giorno;
    }

    public Date UltimoGiornoSettimana(){
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calendar.add(Calendar.DATE, 6);
        Date giorno = Converters.StringToDate(SDF.format(calendar.getTime()));
        return giorno;
    }

    public Date PrimoGiornoMese(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date giorno = Converters.StringToDate(SDF.format(calendar.getTime()));
        return giorno;
    }

    public Date UltimoGiornoMese(){
        Calendar calendar = Calendar.getInstance();
        int ultimo = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, ultimo);
        Date giorno = Converters.StringToDate(SDF.format(calendar.getTime()));
        return giorno;
    }

    public Date PrimoGiornoAnno(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        Date giorno = Converters.StringToDate(SDF.format(calendar.getTime()));
        return giorno;
    }

    public Date UltimoGiornoAnno(){
        Calendar calendar = Calendar.getInstance();
        int ultimo = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
        calendar.set(Calendar.DAY_OF_YEAR, ultimo);
        Date giorno = Converters.StringToDate(SDF.format(calendar.getTime()));
        return giorno;
    }

}