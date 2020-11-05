package com.example.testmenu2.Utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Converters {

    public static Date LongToDate(Long value) {
        return value == null ? null : new Date(value);
    }


    public static Long DateToLong(Date date) {
        return date == null ? null : date.getTime();
    }

    public static String LongToString(Long val){
        Date date = LongToDate(val);
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = dateFormat.format(date);
        return strDate;
    }

    public static  Long StringToLong(String string){
        Date date= null;
        try {
            date = new SimpleDateFormat("dd/MM/yyyy").parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Long val = DateToLong(date);
        return val;
    }

}