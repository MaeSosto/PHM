package Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ReportDao {

    @Insert
    void setReport(Report report);

    @Query("SELECT * FROM report")
    LiveData<List<Report>> getReports();
}
