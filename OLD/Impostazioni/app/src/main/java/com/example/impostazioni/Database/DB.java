package com.example.impostazioni.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.impostazioni.Utilities.Converters;

@Database(entities = {Settings.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class DB extends RoomDatabase {
    public abstract SettingsDao settingsDao();

    public static volatile DB dbInstance;

    static DB getDatabase(final Context context){
        if(dbInstance == null){
            synchronized (DB.class){
                if(dbInstance == null){
                    dbInstance = Room.databaseBuilder(context.getApplicationContext(),
                            DB.class, "settings_database").build();
                }
            }
        }
        return dbInstance;
    }
}