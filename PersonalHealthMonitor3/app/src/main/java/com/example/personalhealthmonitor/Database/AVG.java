package com.example.personalhealthmonitor.Database;

import androidx.room.ColumnInfo;

import java.util.Date;


public class AVG {

    @ColumnInfo(name = "media")
    private double media;

    @ColumnInfo(name = "giorno")
    private int giorno;

    public AVG(double media, int giorno) {
        this.media = media;
        this.giorno = giorno;
    }

    public double getMedia() {
        return media;
    }

    public int getGiorno() {
        return giorno;
    }
}
