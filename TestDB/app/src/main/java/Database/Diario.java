package Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Report.class}, version = 1, exportSchema = false)
public abstract class Diario extends RoomDatabase {
    public abstract ReportDao reportDao();

}