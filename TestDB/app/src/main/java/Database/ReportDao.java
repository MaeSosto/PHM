package Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ReportDao {

    @Query("SELECT * FROM report")
    List<Report> getAll();

    @Query("SELECT * FROM report WHERE id IN (:reportId)")
    List<Report> loadAllByIds (int[] reportId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Report... reports);

    @Delete
    void delete(Report... report);
}
