package com.example.personalhealthmonitor.Utilities;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import com.example.personalhealthmonitor.Database.Notification;
import com.example.personalhealthmonitor.Database.NotificationViewModel;
import com.example.personalhealthmonitor.Database.Report;
import com.example.personalhealthmonitor.Database.ReportViewModel;
import com.example.personalhealthmonitor.Database.Settings;
import com.example.personalhealthmonitor.Database.SettingsViewModel;
import com.example.personalhealthmonitor.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class Utility extends Fragment {

    public static ReportViewModel reportViewModel;
    public static SettingsViewModel settingsViewModel;
    public static NotificationViewModel notificationViewModel;
    public static final String U_BATTITO = " bpm";
    public static final String U_TEMPERATURA = " °C";
    public static final String U_PRESSIONE = " mmHg";
    public static final String U_GLICEMIA = " mg/dl";
    public static final String KEY_BATTITO = "report_battito";
    public static final String KEY_TEMPERATURA = "report_temperatura";
    public static final String KEY_PRESSIONESIS = "report_pressione_sistolica";
    public static final String KEY_PRESSIONEDIA = "report_pressione_diastolica";
    public static final String KEY_GLICEMIAMAX = "report_glicemia_max";
    public static final String KEY_GLICEMIAMIN = "report_glicemia_min";
    public static final String KEY_NOTIFICATION = "NOTIFICATION";
    public static final String KEY_SETTIMANA = "Settimana";
    public static final String KEY_MESE = "Mese";
    public static final String KEY_ANNO = "Anno";
    public static final String KEY_TUTTO = "Tutto";
    public static final int BATTITORANGE1 = 40;
    public static final int BATTITORANGE2 = 180;
    public static final int TEMPERATURARANGE1 = 35;
    public static final int TEMPERATURARANGE2 = 43;
    public static final int PRESSIONERANGE1 = 40;
    public static final int PRESSIONERANGE2 = 180;
    public static final int GLICEMIARANGE1 = 50;
    public static final int GLICEMIARANGE2 = 200;
    public static MutableLiveData<Integer> filtro;
    public static SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
    public static final String KEY_REPORT_DAILY = "NOTIFY DAILY";
    public static final String KEY_WARNING = "NOTIFY WARNING";
    public static final String KEY_RIMANDA = "NOTIFY RIMANDA";
    public static final String CHANNEL_ID = "Daily_notification";
    public static final int NOTIFICATION_ID = 1;

    //Tronca il valore double
    public static double tronca(Double num){
        if(num == null ) return 0;
        else{
            num = num * 100;
            num = (double) Math.round(num);
            num = num / 100;
            return num;
        }
    }

    //PASSANDO LA CHIAVE RESTITUISCE IL VALORE SSALVATO NEL REPORT
    public static String KeyToPrompt(String key){
        switch (key){
            case KEY_BATTITO: return "Battito cardiaco";
            case KEY_TEMPERATURA: return "Temperatura corporea";
            case KEY_PRESSIONESIS: return "Pressione sistolica";
            case KEY_PRESSIONEDIA: return "Pressione diastolica";
            case KEY_GLICEMIAMAX: return "Glicemia massima";
            case KEY_GLICEMIAMIN: return "Glicemia minima";
        }
        return null;
    }

    //LE FUNZIONI SEGUENTI RESTITUISCONO LA DATA DEL PERIODO RICHIESTO
    public static Date PrimoGiornoSettimana(Calendar calendar){
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        return Converters.StringToDate(SDF.format(calendar.getTime()));
    }

    public static Date UltimoGiornoSettimana(Calendar calendar){
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calendar.add(Calendar.DATE, 6);
        return Converters.StringToDate(SDF.format(calendar.getTime()));
    }

    public static Date PrimoGiornoMese(Calendar calendar){
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return Converters.StringToDate(SDF.format(calendar.getTime()));
    }

    public static Date UltimoGiornoMese(Calendar calendar){
        int ultimo = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, ultimo);
        return Converters.StringToDate(SDF.format(calendar.getTime()));
    }

    public static Date PrimoGiornoAnno(Calendar calendar){
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        return Converters.StringToDate(SDF.format(calendar.getTime()));
    }

    public static Date UltimoGiornoAnno(Calendar calendar){
        int ultimo = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
        calendar.set(Calendar.DAY_OF_YEAR, ultimo);
        return Converters.StringToDate(SDF.format(calendar.getTime()));
    }

    //SETTA N REPORT RANDOMICI
    public static void randomData(int numReport){

        for(int i = 0; i<numReport; i++){
            Date randomDate = randomDate(Converters.StringToDate("01/01/2019"), Converters.StringToDate(SDF.format(Calendar.getInstance().getTime())));
            double battito = randomDouble(60, 100);
            double pressioneSistolica = randomDouble(60, 80);
            double pressioneDiastolica = randomDouble(60, 80);
            double temperatura = randomDouble(35, 38);
            double glicemiamax = randomDouble(60, 125);
            double glicemiamin = randomDouble(60, 125);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(randomDate);
            String giorno =SDF.format(calendar.getTime());
            String ora = String.valueOf(randomDate.getHours())+":"+ String.valueOf(randomDate.getMinutes());

            Report report = new Report(0, Converters.StringToDate(giorno), ora, battito, temperatura, pressioneSistolica, pressioneDiastolica, glicemiamax, glicemiamin, Converters.DateToString(randomDate));
            reportViewModel.setReport(report);
        }

        Settings tmpSettings = new Settings(0, KEY_BATTITO, 2, null, null, 0);
        settingsViewModel.insertSettings(tmpSettings);
        tmpSettings = new Settings(0, KEY_PRESSIONESIS, 2, null, null, 0);
        settingsViewModel.insertSettings(tmpSettings);
        tmpSettings = new Settings(0, KEY_PRESSIONEDIA, 2, null, null, 0);
        settingsViewModel.insertSettings(tmpSettings);
        tmpSettings = new Settings(0, KEY_TEMPERATURA, 2, null, null, 0);
        settingsViewModel.insertSettings(tmpSettings);
        tmpSettings = new Settings(0, KEY_GLICEMIAMAX, 2, null, null, 0);
        settingsViewModel.insertSettings(tmpSettings);
        tmpSettings = new Settings(0, KEY_GLICEMIAMIN, 2, null, null, 0);
        settingsViewModel.insertSettings(tmpSettings);
        //tmpSettings = new Settings(0, KEY_NOTIFICATION, 0, null, null, 0);
        //settingsViewModel.insertSettings(tmpSettings);

        Notification tmpNotification = new Notification(0, false, Calendar.getInstance().getTime().getHours(), Calendar.getInstance().getTime().getMinutes());
        notificationViewModel.insertNotification(tmpNotification);
    }

    //CREA VALORI DOUBLE RANDOM
    private static double randomDouble(double inizio, double fine){
        boolean bool = ThreadLocalRandom.current().nextBoolean();
        if(bool) {
            double number = ThreadLocalRandom.current().nextDouble(inizio, fine);
            int aux = (int) (number * 100);
            double result = aux / 100d;//12.43
            return result;
        }
        else return  0;
    }

    //CREA DATE RANDOM
    private static Date randomDate (Date d1, Date d2) {
        return new Date(ThreadLocalRandom.current().nextLong(d1.getTime(), d2.getTime()));
    }
}
