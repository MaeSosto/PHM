package Database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.DateFormat;
import java.util.Date;

@Entity
public class Report {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "dataeora")
    public int dataeora;

    @ColumnInfo(name = "battiti")
    public double battiti;

    @ColumnInfo(name = "pressione")
    public double pressione;

    @ColumnInfo(name = "temperatura")
    public double temperatura;

    @ColumnInfo(name = "glicemia")
    public double glicemia;

    @ColumnInfo(name = "ltacqua")
    public double ltacqua;

    @ColumnInfo(name = "peso")
    public double peso;

    @ColumnInfo(name = "passi")
    public int passi;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDataeora() {
        return dataeora;
    }

    public void setDataeora(int dataeora) {
        this.dataeora = dataeora;
    }

    public double getBattiti() {
        return battiti;
    }

    public void setBattiti(double battiti) {
        this.battiti = battiti;
    }

    public double getPressione() {
        return pressione;
    }

    public void setPressione(double pressione) {
        this.pressione = pressione;
    }

    public double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(double temperatura) {
        this.temperatura = temperatura;
    }

    public double getGlicemia() {
        return glicemia;
    }

    public void setGlicemia(double glicemia) {
        this.glicemia = glicemia;
    }

    public double getLtacqua() {
        return ltacqua;
    }

    public void setLtacqua(double ltacqua) {
        this.ltacqua = ltacqua;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public int getPassi() {
        return passi;
    }

    public void setPassi(int passi) {
        this.passi = passi;
    }

     /*@PrimaryKey
    @NonNull
    private String id;

    @NonNull
    @ColumnInfo(name = "report")
    private String mReport;

    public Report(String id, String mReport){
        this.id = id;
        this.mReport = mReport;
    }

     */

    public Report(int id, int dataeora, double battiti, double pressione, double temperatura, double glicemia, double ltacqua, double peso, int passi) {
        this.id = id;
        this.dataeora = dataeora;
        this.battiti = battiti;
        this.pressione = pressione;
        this.temperatura = temperatura;
        this.glicemia = glicemia;
        this.ltacqua = ltacqua;
        this.peso = peso;
        this.passi = passi;
    }
}
