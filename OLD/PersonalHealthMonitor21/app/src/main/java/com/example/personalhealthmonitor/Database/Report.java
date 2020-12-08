package com.example.personalhealthmonitor.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "reports")
public class Report {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "report_giorno")
    private final Date data;

    @ColumnInfo(name = "report_ora")
    private final String ora;

    @ColumnInfo(name = "report_battito")
    private final double battito;

    @ColumnInfo(name = "report_temperatura")
    private final double temperatura;

    @ColumnInfo(name = "report_pressione_sistolica")
    private final double pressione_sistolica;

    @ColumnInfo(name = "report_pressione_diastolica")
    private final double pressione_diastolica;

    @ColumnInfo(name = "report_glicemia_max")
    private final double glicemiamax;

    @ColumnInfo(name = "report_glicemia_min")
    private final double glicemiamin;

    @ColumnInfo(name = "report_nota")
    private final String nota;

    //COSTRUTTORE
    public Report(int id, Date data, String ora, double battito, double temperatura, double pressione_sistolica, double pressione_diastolica, double glicemiamax, double glicemiamin, java.lang.String nota) {
        this.id = id;
        this.data = data;
        this.ora = ora;
        this.battito = battito;
        this.temperatura = temperatura;
        this.pressione_sistolica = pressione_sistolica;
        this.pressione_diastolica = pressione_diastolica;
        this.glicemiamax = glicemiamax;
        this.glicemiamin = glicemiamin;
        this.nota = nota;
    }

    //SETTER AND GETTER
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Date getData() {
        return data;
    }
    public String getOra() {
        return ora;
    }
    public double getBattito() {
        return battito;
    }
    public double getTemperatura() {
        return temperatura;
    }
    public double getPressione_sistolica() {
        return pressione_sistolica;
    }
    public double getPressione_diastolica() {return pressione_diastolica;}
    public double getGlicemiamax() {return glicemiamax;}
    public double getGlicemiamin() {return glicemiamin;}
    public String getNota() {return nota;}
}
