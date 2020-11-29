package com.example.testmenu2.statistiche;

import android.graphics.Color;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.testmenu2.Database.AVG;
import com.example.testmenu2.Database.Report;
import com.example.testmenu2.Database.ReportViewModel;
import com.example.testmenu2.MainActivity;
import com.example.testmenu2.Utilities.Converters;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StatisticheViewModel extends ViewModel {
    private final ReportViewModel reportViewModel = MainActivity.reportViewModel;
    private final SimpleDateFormat SDF;
    private Date inizio, fine;
    private static LiveData<List<Report>> mReports;
    private static LiveData<List<AVG>> battitoAVG, pressioneSistolicaAVG, pressioneDiastolicaAVG, temperaturaAVG, glicemiaAVG;
    private List<AVG> pressioneSistolica, pressioneDiastolica;
    private final MutableLiveData<String> periodo;
    private final MutableLiveData<String> TXVPeriodo;
    private final MutableLiveData<String> TXVnumReport;
    private final MutableLiveData<PieData> pieDataMutableLiveData;
    private final MutableLiveData<LineData> battitoMutableLineData;
    private final MutableLiveData<LineData> temperaturaMutableLineData;
    private final MutableLiveData<LineData> glicemiaMutableLineData;
    private final MutableLiveData<BarData> pressioneMutableBarData;
    int battito;

    public StatisticheViewModel() {
        SDF = new SimpleDateFormat("dd/MM/yyyy");
        inizio = new Date();
        fine = new Date();
        TXVPeriodo = new MutableLiveData<>();
        TXVnumReport = new MutableLiveData<>();
        pieDataMutableLiveData = new MutableLiveData<>();
        battitoMutableLineData = new MutableLiveData<>();
        temperaturaMutableLineData = new MutableLiveData<>();
        glicemiaMutableLineData = new MutableLiveData<>();
        pressioneMutableBarData = new MutableLiveData<>();

        periodo = new MutableLiveData<>();
        periodo.setValue("Settimana");
        periodo.observeForever( new Observer<String>() {
            @Override
            public void onChanged(String s) {
                TXVPeriodo.setValue(s);
                switch (s){
                    case "Settimana":
                        inizio = PrimoGiornoSettimana();
                        fine = UltimoGiornoSettimana();
                        TXVPeriodo.setValue("Dal " + Converters.DateToString(inizio) + " al " + Converters.DateToString(fine));
                        break;
                    case "Mese":
                        inizio = PrimoGiornoMese();
                        fine = UltimoGiornoMese();
                        TXVPeriodo.setValue("Dal " + Converters.DateToString(inizio) + " al " + Converters.DateToString(fine));
                        break;
                    case "Anno":
                        inizio = PrimoGiornoAnno();
                        fine = UltimoGiornoAnno();
                        TXVPeriodo.setValue("Dal " + Converters.DateToString(inizio) + " al " + Converters.DateToString(fine));
                        break;
                    case "Tutto":
                        inizio = null;
                        fine = null;
                        TXVPeriodo.setValue(s);
                        break;
                }

                //battito = reportViewModel.getCount("report_battito");

                mReports = reportViewModel.getAllReports(inizio, fine);
                mReports.observeForever(reports -> {
                    setTXVnumReport(String.valueOf(reports.size()));
                    pieDataMutableLiveData.setValue(getPieData(reports));
                });

                battitoAVG = reportViewModel.getAVG("report_battito", inizio, fine);
                battitoAVG.observeForever(avgs -> battitoMutableLineData.setValue(getLineData(avgs)));

                pressioneSistolicaAVG = reportViewModel.getAVG("report_pressione_sistolica", inizio, fine);
                pressioneSistolicaAVG.observeForever(avgs -> {
                    pressioneSistolica = avgs;
                    pressioneMutableBarData.setValue(getBarData());
                });

                pressioneDiastolicaAVG = reportViewModel.getAVG("report_pressione_diastolica", inizio, fine);
                pressioneDiastolicaAVG.observeForever(avgs -> {
                    pressioneDiastolica = avgs;
                    pressioneMutableBarData.setValue(getBarData());
                });

                temperaturaAVG = reportViewModel.getAVG("report_temperatura", inizio, fine);
                temperaturaAVG.observeForever(avgs -> temperaturaMutableLineData.setValue(getLineData(avgs)));

                glicemiaAVG = reportViewModel.getAVG("report_glicemia", inizio, fine);
                glicemiaAVG.observeForever(avgs -> glicemiaMutableLineData.setValue(getLineData(avgs)));
            }
        });
    }


    //COSTRUISCE IL PIECHART
    private PieData getPieData (List<Report> reports){
        ArrayList<PieEntry> PiedataVals = new ArrayList<>();

        int  pressioneSistolica = 0, pressioneDiastolica=0, temperatura = 0, glicemia = 0;

        for (int i = 0; i < reports.size(); i++) {
            Report report = reports.get(i);
            //if(report.getBattito() != 0) battito++;
            if(report.getPressione_sistolica() != 0) pressioneSistolica++;
            if(report.getPressione_diastolica() != 0) pressioneDiastolica++;
            if(report.getTemperatura() != 0) temperatura++;
            if(report.getGlicemia() != 0) glicemia++;
        }

        //PiedataVals.add(new PieEntry(battito, "Battito"));
        PiedataVals.add(new PieEntry(battito, "Battito"));
        PiedataVals.add(new PieEntry(temperatura, "Temperatura"));
        PiedataVals.add(new PieEntry(pressioneSistolica, "Pressione Sistolica"));
        PiedataVals.add(new PieEntry(pressioneDiastolica, "Pressione Diastolica"));
        PiedataVals.add(new PieEntry(glicemia, "Glicemia"));


        PieDataSet pieDataSet = new PieDataSet(PiedataVals, TXVPeriodo.getValue());
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        pieDataSet.setValueTextSize(12);
        pieDataSet.setValueTextColor(Color.BLACK);
        return new PieData(pieDataSet);
    }

    public BarData getBarData() {
        BarDataSet barDataSetSistolica = null, barDataSetDiastolica = null;
        ArrayList<BarEntry> sistolica = new ArrayList<>();
        ArrayList<BarEntry> diastolica = new ArrayList<>();
        int p = 1; //incrementa i giorni nel calendario
        int i = 0, k = 0;

        Calendar calendarInizio = Calendar.getInstance();
        Calendar calendarFine = Calendar.getInstance();
        Calendar calendarGiorno = Calendar.getInstance();

        if(inizio != null && fine != null){
            calendarInizio.setTime(inizio);
            calendarFine.setTime(fine);
            calendarFine.add(Calendar.DATE, 1);
        }
        else {
            Date start, end;
            if(pressioneSistolica == null){
                start = pressioneDiastolica.get(k).getGiorno();
                end = pressioneDiastolica.get(pressioneDiastolica.size()-1).getGiorno();
            }
            else if(pressioneDiastolica == null){
                start = pressioneSistolica.get(i).getGiorno();
                end = pressioneSistolica.get(pressioneSistolica.size()-1).getGiorno();
            }
            else {
                if (Converters.DateToLong(pressioneSistolica.get(i).getGiorno()) < Converters.DateToLong(pressioneDiastolica.get(k).getGiorno()))
                    start = pressioneSistolica.get(i).getGiorno();
                else start = pressioneDiastolica.get(k).getGiorno();
                if (Converters.DateToLong(pressioneSistolica.get(pressioneSistolica.size() - 1).getGiorno()) > Converters.DateToLong(pressioneDiastolica.get(pressioneDiastolica.size() - 1).getGiorno()))
                    end = pressioneSistolica.get(pressioneSistolica.size() - 1).getGiorno();
                else end = pressioneDiastolica.get(pressioneDiastolica.size() - 1).getGiorno();
            }
            calendarGiorno.setTime(start);
            calendarInizio.setTime(start);
            calendarFine.setTime(end);
            calendarFine.add(Calendar.DATE, 1);
        }

        while (!calendarInizio.equals(calendarFine)  ){
            if(pressioneSistolica != null && i < pressioneSistolica.size()) {
                AVG avgSis = pressioneSistolica.get(i);
                calendarGiorno.setTime(avgSis.getGiorno());
                if (calendarInizio.equals(calendarGiorno)) {
                    sistolica.add(new BarEntry(p, (float) avgSis.getMedia()));
                    i++;
                }
            }
            if(pressioneDiastolica != null && k < pressioneDiastolica.size()) {
                AVG avgDia = pressioneDiastolica.get(k);
                calendarGiorno.setTime(avgDia.getGiorno());
                if (calendarInizio.equals(calendarGiorno)) {
                    diastolica.add(new BarEntry(p, (float) avgDia.getMedia()));
                    k++;
                }
            }
            calendarInizio.add(Calendar.DATE, 1);
            p++;
        }

        barDataSetSistolica = new BarDataSet(sistolica, "Pressione sistolica");
        barDataSetDiastolica = new BarDataSet(diastolica, "Pressione diastolica");
        barDataSetSistolica.setValueTextSize(12);
        barDataSetSistolica.setValueTextColor(Color.BLACK);
        barDataSetSistolica.setColor(Color.RED);
        barDataSetDiastolica.setValueTextSize(12);
        barDataSetDiastolica.setValueTextColor(Color.BLACK);
        barDataSetDiastolica.setColor(Color.BLUE);
        return new BarData(barDataSetSistolica, barDataSetDiastolica);
    }


    public LineData getLineData(List<AVG> avgs){
        ArrayList<Entry> dataVals = new ArrayList<>();
        int i = 0;
        int p = 1;

        Calendar calendarInizio = Calendar.getInstance();
        Calendar calendarFine = Calendar.getInstance();
        Calendar calendarGiorno = Calendar.getInstance();

        if(inizio != null && fine != null){
            calendarInizio.setTime(inizio);
            calendarFine.setTime(fine);
        }
        else {
            calendarGiorno.setTime(avgs.get(i).getGiorno());
            calendarInizio.setTime(avgs.get(i).getGiorno());
            calendarFine.setTime(avgs.get((avgs.size())-1).getGiorno());
        }
        calendarFine.add(Calendar.DATE, 1);

        while (!calendarInizio.equals(calendarFine) && i<avgs.size()){
            AVG avg = avgs.get(i);
            calendarGiorno.setTime(avg.getGiorno());
            if(calendarInizio.equals(calendarGiorno)){
                dataVals.add(new Entry(p, (float) avg.getMedia()));
                i++;
            }
            calendarInizio.add(Calendar.DATE, 1);
            p++;
        }

        LineDataSet lineDataSet = new LineDataSet(dataVals, TXVPeriodo.getValue());
        lineDataSet.setValueTextSize(12);
        lineDataSet.setValueTextColor(Color.BLACK);
        return new LineData(lineDataSet);
    }

    public Description getDescription(){
        Description description = new Description();
        description.setText("");

        return description;
    }

    public Date PrimoGiornoSettimana (){
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        return Converters.StringToDate(SDF.format(calendar.getTime()));
    }

    public Date UltimoGiornoSettimana(){
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calendar.add(Calendar.DATE, 6);
        return Converters.StringToDate(SDF.format(calendar.getTime()));
    }

    public Date PrimoGiornoMese(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return Converters.StringToDate(SDF.format(calendar.getTime()));
    }

    public Date UltimoGiornoMese(){
        Calendar calendar = Calendar.getInstance();
        int ultimo = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, ultimo);
        return Converters.StringToDate(SDF.format(calendar.getTime()));
    }

    public Date PrimoGiornoAnno(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        return Converters.StringToDate(SDF.format(calendar.getTime()));
    }

    public Date UltimoGiornoAnno(){
        Calendar calendar = Calendar.getInstance();
        int ultimo = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
        calendar.set(Calendar.DAY_OF_YEAR, ultimo);
        return Converters.StringToDate(SDF.format(calendar.getTime()));
    }

    //SETTERS AND GETTERS
    public MutableLiveData<String> getTXVPeriodo() {
        return TXVPeriodo;
    }

    public MutableLiveData<String> getTXVnumReport() {
        return TXVnumReport;
    }

    public void setTXVnumReport(String TXVnumReport) {
        this.TXVnumReport.setValue(TXVnumReport);
    }

    public void setPeriodo(String periodo) {
        this.periodo.setValue(periodo);
    }

    public MutableLiveData<PieData> getPieDataMutableLiveData() {
        return pieDataMutableLiveData;
    }

    public MutableLiveData<LineData> getBattitoMutableLineData() {
        return battitoMutableLineData;
    }

    public MutableLiveData<LineData> getTemperaturaMutableLineData() {
        return temperaturaMutableLineData;
    }

    public MutableLiveData<LineData> getGlicemiaMutableLineData() {
        return glicemiaMutableLineData;
    }

    public MutableLiveData<BarData> getPressioneMutableBarData() {
        return pressioneMutableBarData;
    }
}