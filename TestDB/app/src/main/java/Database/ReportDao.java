package Database;

import androidx.room.Dao;
import androidx.room.Insert;

@Dao
public interface ReportDao {

    @Insert
    void addReport(Report report);
}
