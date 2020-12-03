package com.example.personalhealthmonitor.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notification")
public class Notification {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "notification_status")
    private boolean status;

    @ColumnInfo(name = "notification_ora")
    private int ora;

    @ColumnInfo(name = "notification_minuti")
    private int minuti;

    public Notification(int id, boolean status, int ora, int minuti) {
        this.id = id;
        this.status = status;
        this.ora = ora;
        this.minuti = minuti;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getOra() {
        return ora;
    }

    public void setOra(int ora) {
        this.ora = ora;
    }

    public int getMinuti() {
        return minuti;
    }

    public void setMinuti(int minuti) {
        this.minuti = minuti;
    }
}


