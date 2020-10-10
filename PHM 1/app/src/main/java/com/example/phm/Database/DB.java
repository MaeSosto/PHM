package com.example.phm.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Report.class}, version = 1)
public abstract class DB extends RoomDatabase {

    public abstract ReportDao reportDao();

}
