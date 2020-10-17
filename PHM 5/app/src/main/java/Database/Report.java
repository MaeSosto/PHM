package Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.DateFormat;

@Entity(tableName = "reports")
public class Report {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "report_data")
    private DateFormat data;

    @ColumnInfo(name = "report_battito")
    private double battito;

    @ColumnInfo(name = "report_temperatura")
    private double temperatura;

    @ColumnInfo(name = "report_glicemia")
    private double glicemia;

    @ColumnInfo(name = "report_nota")
    private String nota;

    //COSTRUTTORE
    public Report(int id, DateFormat data, double battito, double temperatura, double glicemia, String nota) {
        this.id = id;
        this.data = data;
        this.battito = battito;
        this.temperatura = temperatura;
        this.glicemia = glicemia;
        this.nota = nota;
    }

    //SETTER AND GETTER
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DateFormat getData() {
        return data;
    }

    public void setData(DateFormat data) {
        this.data = data;
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

    public double getGlicemia() {
        return glicemia;
    }

    public void setGlicemia(double glicemia) {
        this.glicemia = glicemia;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }
}
