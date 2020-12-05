package com.example.personalhealthmonitor.Utilities;

import androidx.room.TypeConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.personalhealthmonitor.MainActivity.SDF;

public class Converters {
    @TypeConverter
    public static Date LongToDate(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long DateToLong(Date date) {
        return date == null ? null : date.getTime();
    }

    public static String DateToString(Date date){
        return SDF.format(date);
    }

    public static  Date StringToDate(String string){
        Date date= null;
        try {
            date = SDF.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

}