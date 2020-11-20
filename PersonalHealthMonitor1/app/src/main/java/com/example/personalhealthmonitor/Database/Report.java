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
    private int giorno;

    @ColumnInfo(name = "report_mese")
    private int mese;

    @ColumnInfo(name = "report_anno")
    private int anno;

    @ColumnInfo(name = "report_ora")
    private String ora;

    @ColumnInfo(name = "report_battito")
    private double battito;

    @ColumnInfo(name = "report_temperatura")
    private double temperatura;

    @ColumnInfo(name = "report_pressione_sistolica")
    private double pressione_sistolica;

    @ColumnInfo(name = "report_pressione_diastolica")
    private double pressione_diastolica;

    @ColumnInfo(name = "report_glicemia_max")
    private double glicemiamax;

    @ColumnInfo(name = "report_glicemia_min")
    private double glicemiamin;

    @ColumnInfo(name = "report_nota")
    private String nota;

    //COSTRUTTORE

    public Report(int id, int giorno, int mese, int anno, String ora, double battito, double temperatura, double pressione_sistolica, double pressione_diastolica, double glicemiamax, double glicemiamin, String nota) {
        this.id = id;
        this.giorno = giorno;
        this.mese = mese;
        this.anno = anno;
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

    public int getGiorno() {
        return giorno;
    }

    public void setGiorno(int giorno) {
        this.giorno = giorno;
    }

    public int getMese() {
        return mese;
    }

    public void setMese(int mese) {
        this.mese = mese;
    }

    public int getAnno() {
        return anno;
    }

    public void setAnno(int anno) {
        this.anno = anno;
    }

    public String getOra() {
        return ora;
    }

    public void setOra(String ora) {
        this.ora = ora;
    }

    public double getBattito() {
        return battito;
    }

    public void setBattito(double battito) {
        this.battito = battito;
    }

    public double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(double temperatura) {
        this.temperatura = temperatura;
    }

    public double getPressione_sistolica() {
        return pressione_sistolica;
    }

    public void setPressione_sistolica(double pressione_sistolica) {
        this.pressione_sistolica = pressione_sistolica;
    }

    public double getPressione_diastolica() {
        return pressione_diastolica;
    }

    public void setPressione_diastolica(double pressione_diastolica) {
        this.pressione_diastolica = pressione_diastolica;
    }

    public double getGlicemiamax() {
        return glicemiamax;
    }

    public void setGlicemiamax(double glicemiamax) {
        this.glicemiamax = glicemiamax;
    }

    public double getGlicemiamin() {
        return glicemiamin;
    }

    public void setGlicemiamin(double glicemiamin) {
        this.glicemiamin = glicemiamin;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }
}
