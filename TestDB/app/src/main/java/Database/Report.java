package Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Report {
    @PrimaryKey (autoGenerate = true)
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

    @ColumnInfo(name = "descrizione")
    public int descrizione;


    public int getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(int descrizione) {
        this.descrizione = descrizione;
    }
}


