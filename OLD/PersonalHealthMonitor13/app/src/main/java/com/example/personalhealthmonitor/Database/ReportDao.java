package com.example.personalhealthmonitor.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.Date;
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
    LiveData<Report> getReport(int reportId);

    @RawQuery(observedEntities = Report.class)
    LiveData<List<Report>> getAllReports(SupportSQLiteQuery query);

    @RawQuery(observedEntities = Report.class)
    Double getAVGVal(SupportSQLiteQuery query);

    @RawQuery(observedEntities = Report.class)
    int getCOUNTVal(SupportSQLiteQuery query);

    @RawQuery(observedEntities = Report.class)
    Date getMinMaxDateReport(SupportSQLiteQuery query);

    @Query("SELECT * FROM reports WHERE id = 0")
    LiveData<List<Report>> emptyReportList();
}
