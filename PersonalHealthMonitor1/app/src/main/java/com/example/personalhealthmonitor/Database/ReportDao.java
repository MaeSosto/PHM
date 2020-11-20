package com.example.personalhealthmonitor.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

@Dao
public interface ReportDao {
    @Insert
    void addReport(Report report);

    @Update
    void updateReport (Report report);

    @Delete
    void deleteReport (Report report);

    @Query("SELECT * FROM reports WHERE id=:reportId")
    Report getReport(int reportId);

    @RawQuery(observedEntities = Report.class)
    LiveData<Double> getAVGVal(SupportSQLiteQuery query);

    @RawQuery(observedEntities = Report.class)
    LiveData<List<Report>> getAllReports(SupportSQLiteQuery query);

}
