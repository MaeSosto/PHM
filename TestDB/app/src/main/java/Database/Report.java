package Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;



@Entity(tableName = "report")
public class Report {

    @PrimaryKey(autoGenerate = true)
    public int id;


    @ColumnInfo(name = "report_descrizione")
    public String descrizione;

    //Setter e getter
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
