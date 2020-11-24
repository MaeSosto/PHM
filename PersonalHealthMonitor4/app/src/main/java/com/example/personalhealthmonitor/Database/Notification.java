package com.example.personalhealthmonitor.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "notification")
public class Notification {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "notifies_enable")
    private boolean enable;

    @ColumnInfo(name = "notifies_hour")
    private Date hour;

    public Notification(int id, boolean enable, Date hour) {
        this.id = id;
        this.enable = enable;
        this.hour = hour;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public Date getHour() {
        return hour;
    }

    public void setHour(Date hour) {
        this.hour = hour;
    }
}