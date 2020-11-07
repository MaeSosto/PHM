package com.example.testmenu2.statistiche;

import android.graphics.Color;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.testmenu2.Database.Report;
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
import java.util.List;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

public class StatisticheViewModel extends ViewModel {

    private SimpleDateFormat SDF;

    public StatisticheViewModel() {
        SDF = new SimpleDateFormat("dd/MM/yyyy");
    }



    public PieData getPieData(List<Report> reports, String label){
        PieDataSet pieDataSet = new PieDataSet(getDataValues(reports), label);
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        pieDataSet.setValueTextSize(12);
        pieDataSet.setValueTextColor(Color.BLACK);
        PieData pieData = new PieData(pieDataSet);
        return pieData;
    }

    public ArrayList<PieEntry> getDataValues(List<Report> reports){
        ArrayList<PieEntry> dataVals = new ArrayList<>();
        int battito = 0, pressione = 0, temperatura = 0, glicemia = 0;

        for (int i = 0; i < reports.size(); i++) {
            Report report = reports.get(i);
            if(report.getBattito() != 0) battito++;
            if(report.getPressione() != 0) pressione++;
            if(report.getTemperatura() != 0) temperatura++;
            if(report.getGlicemia() != 0) glicemia++;
        }

        dataVals.add(new PieEntry(battito, "Battito"));
        dataVals.add(new PieEntry(temperatura, "Temperatura"));
        dataVals.add(new PieEntry(pressione, "Pressione"));
        dataVals.add(new PieEntry(glicemia, "Glicemia"));

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