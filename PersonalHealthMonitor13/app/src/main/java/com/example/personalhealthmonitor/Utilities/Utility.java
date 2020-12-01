package com.example.personalhealthmonitor.Utilities;

import androidx.fragment.app.Fragment;

import com.example.personalhealthmonitor.Database.Notification;
import com.example.personalhealthmonitor.Database.Report;
import com.example.personalhealthmonitor.Database.Settings;
import com.example.personalhealthmonitor.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import static com.example.personalhealthmonitor.MainActivity.KEY_BATTITO;
import static com.example.personalhealthmonitor.MainActivity.KEY_GLICEMIAMAX;
import static com.example.personalhealthmonitor.MainActivity.KEY_GLICEMIAMIN;
import static com.example.personalhealthmonitor.MainActivity.KEY_NOTIFICATION;
import static com.example.personalhealthmonitor.MainActivity.KEY_PRESSIONEDIA;
import static com.example.personalhealthmonitor.MainActivity.KEY_PRESSIONESIS;
import static com.example.personalhealthmonitor.MainActivity.KEY_TEMPERATURA;
import static com.example.personalhealthmonitor.MainActivity.notificationViewModel;
import static com.example.personalhealthmonitor.MainActivity.reportViewModel;
import static com.example.personalhealthmonitor.MainActivity.settingsViewModel;

public class Utility extends Fragment {

    private static SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");

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

    public static void randomData(int numReport){

        for(int i = 0; i<numReport; i++){
            Date randomDate = randomDate(Converters.StringToDate("01/01/2019"), Converters.StringToDate("31/12/2020"));
            double battito = randomDouble(60, 100);
            double pressioneSistolica = randomDouble(60, 80);
            double pressioneDiastolica = randomDouble(60, 80);
            double temperatura = randomDouble(35, 38);
            double glicemiamax = randomDouble(60, 125);
            double glicemiamin = randomDouble(60, 125);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(randomDate);
            SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
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

        Notification tmpNotification = new Notification(0, true, Calendar.getInstance().getTime().getHours(), Calendar.getInstance().getTime().getMinutes());
        notificationViewModel.insertNotification(tmpNotification);
    }

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

    private static Date randomDate (Date d1, Date d2) {
        return new Date(ThreadLocalRandom.current().nextLong(d1.getTime(), d2.getTime()));
    }
}
