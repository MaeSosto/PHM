package Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "reports")
public class Report {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "report_descrizione")
    private String descrizione;

    public Report(int id, String descrizione) {
        this.id = id;
        this.descrizione = descrizione;
    }

    //SETTER AND GETTER
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
}
