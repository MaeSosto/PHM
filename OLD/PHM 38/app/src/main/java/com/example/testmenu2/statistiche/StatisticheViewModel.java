package com.example.testmenu2.statistiche;

import android.graphics.Color;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.testmenu2.Database.AVG;
import com.example.testmenu2.Database.Report;
import com.example.testmenu2.Database.ReportViewModel;
import com.example.testmenu2.Utilities.Converters;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
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

import java.security.Key;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StatisticheViewModel extends ViewModel {

    private SimpleDateFormat SDF;
    private Date inizio, fine;
    private ReportViewModel reportViewModel;
    private static LiveData<List<Report>> mReports;
    private static LiveData<List<AVG>> battitoAVG, pressioneSistolicaAVG, pressioneDiastolicaAVG, temperaturaAVG, glicemiaAVG;
    private List<AVG> pressioneSistolica, pressioneDiastolica;
    private MutableLiveData<String> StringPeriodo, StringnumReport;
    private LifecycleOwner owner;

    public StatisticheViewModel() {
        SDF = new SimpleDateFormat("dd/MM/yyyy");
        inizio = new Date();
        fine = new Date();
        StringPeriodo = new MutableLiveData<>();
        StringnumReport = new MutableLiveData<>();
    }




    protected void setLineChart(LineChart lineChart, List<AVG> avgs){
        lineChart.setData(getLineData(avgs));
        lineChart.setDescription(getDescription(""));
        lineChart.animateXY(1000, 1000);
        lineChart.setTouchEnabled(true);
        lineChart.setPinchZoom(true);
    }

    protected void setBarChart(BarChart barChart){
        barChart.setData(getBarData(pressioneSistolica, pressioneDiastolica));
        barChart.setDescription(getDescription(""));
        barChart.animateXY(1000, 1000);
        barChart.setTouchEnabled(true);
        barChart.setPinchZoom(true);
    }

    protected void setChartData(String periodo){
        setPeriodo(periodo); //imposto le date del periodo
        Date inizio = getInizio(); //prendo le date del periodo
        Date fine = getFine();

        //SE NON FORNISCO UN PERIODO ALLORA PRENDO TUTTI I REPORT SALVATI NEL DB
        if (periodo == "Tutto"){
            StringPeriodo.setValue(periodo);
            mReports = reportViewModel.getAllReports();
            battitoAVG = reportViewModel.getAVGAll("Battito");
            pressioneSistolicaAVG = reportViewModel.getAVGAll("PressioneSistolica");
            pressioneDiastolicaAVG = reportViewModel.getAVGAll("PressioneDiastolica");
            temperaturaAVG = reportViewModel.getAVGAll("Temperatura");
            glicemiaAVG = reportViewModel.getAVGAll("Glicemia");

        }
        //ALTRIMENTI PRENDO SOLO I REPORT DI QUEL DETERMINATO PERIODO
        else {
            StringPeriodo.setValue("Dal " + Converters.DateToString(inizio) + " al " + Converters.DateToString(fine));
            mReports = reportViewModel.getAllReportsInPeriod(inizio, fine);
            battitoAVG = reportViewModel.getAVGInPeriod("Battito", inizio, fine);
            pressioneSistolicaAVG =  reportViewModel.getAVGInPeriod("PressioneSistolica", inizio, fine);
            pressioneDiastolicaAVG =  reportViewModel.getAVGInPeriod("PressioneDiastolica", inizio, fine);
            temperaturaAVG = reportViewModel.getAVGInPeriod("Temperatura", inizio, fine);
            glicemiaAVG = reportViewModel.getAVGInPeriod("Glicemia", inizio, fine);

        }
    }

    //MEMORIZZO LA DATA INIZIALE E FINALE DEL PERIODO - SE NULL ALLORA è TUTTO
    public void setDates(Date date1, Date date2){
        //Log.i("setDates", "dal "+date1.toString() + " al "+ date2.toString());

        this.inizio = date1;
        this.fine = date2;
    }

    //RESTITUISCO LA DATA DI INIZIO
    public Date getInizio(){
        return inizio;
    }

    //RESTITUISCO LA DATA DI FINE
    public Date getFine(){
        return fine;
    }

    //Imposto il PieChart
    public PieData getPieData(List<Report> reports){
        PieDataSet pieDataSet = new PieDataSet(setDataValues(reports), StringPeriodo.getValue());
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        pieDataSet.setValueTextSize(12);
        pieDataSet.setValueTextColor(Color.BLACK);
        PieData pieData = new PieData(pieDataSet);
        return pieData;
    }
    //Aggiungo i contenuti nel PieChart
    private ArrayList<PieEntry> setDataValues(List<Report> reports){
        ArrayList<PieEntry> PiedataVals = new ArrayList<>();
        //IMPOSTO IL PIECHART
        int battito = 0, pressioneSistolica = 0, pressioneDiastolica=0, temperatura = 0, glicemia = 0;

        for (int i = 0; i < reports.size(); i++) {
            Report report = reports.get(i);
            if(report.getBattito() != 0) battito++;
            if(report.getPressione_sistolica() != 0) pressioneSistolica++;
            if(report.getPressione_diastolica() != 0) pressioneDiastolica++;
            if(report.getTemperatura() != 0) temperatura++;
            if(report.getGlicemia() != 0) glicemia++;
        }

        PiedataVals.add(new PieEntry(battito, "Battito"));
        PiedataVals.add(new PieEntry(temperatura, "Temperatura"));
        PiedataVals.add(new PieEntry(pressioneSistolica, "Pressione Sistolica"));
        PiedataVals.add(new PieEntry(pressioneDiastolica, "Pressione Diastolica"));
        PiedataVals.add(new PieEntry(glicemia, "Glicemia"));
        return PiedataVals;
    }

    public BarData getBarData(List<AVG> pressioneSistolica, List<AVG> pressioneDiastolica) {
        BarDataSet barDataSetSistolica = null, barDataSetDiastolica = null;
        ArrayList<BarEntry> sistolica = new ArrayList<BarEntry>();
        ArrayList<BarEntry> diastolica = new ArrayList<BarEntry>();
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
        BarData barData = new BarData(barDataSetSistolica, barDataSetDiastolica);
        return barData;
    }


    public LineData getLineData(List<AVG> avgs){
        LineDataSet lineDataSet = null;
        lineDataSet = new LineDataSet(getLineDataValues(avgs), StringPeriodo.getValue());

        //lineDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        lineDataSet.setValueTextSize(12);
        lineDataSet.setValueTextColor(Color.BLACK);
        LineData lineData = new LineData(lineDataSet);
        return lineData;
    }

    public ArrayList<Entry> getLineDataValues(List<AVG> avgs){
        ArrayList<Entry> dataVals = new ArrayList<Entry>();
        int i = 0;
        int p = 1;

        Calendar calendarInizio = Calendar.getInstance();
        Calendar calendarFine = Calendar.getInstance();
        Calendar calendarGiorno = Calendar.getInstance();

        if(inizio != null && fine != null){
            calendarInizio.setTime(inizio);
            calendarFine.setTime(fine);
            calendarFine.add(Calendar.DATE, 1);
        }
        else {
            calendarGiorno.setTime(avgs.get(i).getGiorno());
            calendarInizio.setTime(avgs.get(i).getGiorno());
            calendarFine.setTime(avgs.get((avgs.size())-1).getGiorno());
            calendarFine.add(Calendar.DATE, 1);
        }

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

        return dataVals;
    }

    public Description getDescription(String s){
        Description description = new Description();
        description.setText(s);

        return description;
    }

    public void setPeriodo(String periodo){
        //Log.i("setPeriodo: ", periodo);

        StringPeriodo.setValue(periodo);
        switch (periodo){
            case "Settimana": setDates(PrimoGiornoSettimana(), UltimoGiornoSettimana());
                break;
            case "Mese": setDates(PrimoGiornoMese(), UltimoGiornoMese());
                break;
            case "Anno": setDates(PrimoGiornoAnno(), UltimoGiornoAnno());
                break;
            case "Tutto": setDates(null, null);
                break;
        }
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

    public void setReportViewModel(ReportViewModel reportViewModel) {
        this.reportViewModel = reportViewModel;
    }

    public static LiveData<List<Report>> getmReports() {
        return mReports;
    }

    public static LiveData<List<AVG>> getBattitoAVG() {
        return battitoAVG;
    }

    public static LiveData<List<AVG>> getPressioneSistolicaAVG() {
        return pressioneSistolicaAVG;
    }

    public static LiveData<List<AVG>> getPressioneDiastolicaAVG() {
        return pressioneDiastolicaAVG;
    }

    public static LiveData<List<AVG>> getTemperaturaAVG() {
        return temperaturaAVG;
    }

    public static LiveData<List<AVG>> getGlicemiaAVG() {
        return glicemiaAVG;
    }

    public void setPressioneSistolica(List<AVG> pressioneSistolica) {
        this.pressioneSistolica = pressioneSistolica;
    }

    public void setPressioneDiastolica(List<AVG> pressioneDiastolica) {
        this.pressioneDiastolica = pressioneDiastolica;
    }

    public MutableLiveData<String> getStringPeriodo() {
        return StringPeriodo;
    }

    public MutableLiveData<String> getStringnumReport() {
        return StringnumReport;
    }

    public void setStringnumReport(String stringnumReport) {
        StringnumReport.setValue(stringnumReport);
    }

    public LifecycleOwner getOwner() {
        return owner;
    }

    public void setOwner(LifecycleOwner owner) {
        this.owner = owner;
    }
}