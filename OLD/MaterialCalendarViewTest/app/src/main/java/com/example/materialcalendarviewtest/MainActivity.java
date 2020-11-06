package com.example.materialcalendarviewtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.applandeo.materialcalendarview.CalendarUtils;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.graphics.Color.rgb;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<EventDay> events = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        events.add(new EventDay(calendar, R.drawable.sample_three_icons));
        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);
        calendarView.setEvents(events);

        List<Calendar> calendars = new ArrayList<>();
        Calendar punto = Calendar.getInstance();
        punto.set(2020, 10, 10);
        calendars.add(punto);
        punto.set(2020, 10, 11);
        calendars.add(punto);
        calendarView.setHighlightedDays(calendars);


    }

}

