package com.example.testmenu2.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import java.util.Date;

@Entity(tableName = "avgs")
public class AVG{
    @ColumnInfo(name = "media")
    private double avg;
    @ColumnInfo(name = "giorno")
    private Date giorno;

    public AVG(double avg, Date giorno){
        this.avg = avg;
        this.giorno = giorno;
    }

    public double getAvg() {
        return avg;
    }

    public Date getGiorno() {
        return giorno;
    }
}
