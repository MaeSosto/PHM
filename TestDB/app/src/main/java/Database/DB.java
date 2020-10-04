package Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Report.class}, version = 1, exportSchema = false)
public abstract class DB extends RoomDatabase {
    public abstract ReportDao reportDao();

}
