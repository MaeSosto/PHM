package com.example.testmenu2.Database;

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

    @Query("DELETE FROM reports")
    void deleteAll();

    @Query("SELECT * FROM reports WHERE id=:reportId")
    LiveData<Report> getReport(int reportId);

    @RawQuery(observedEntities = Report.class)
    LiveData<List<Report>> getAllReports(SupportSQLiteQuery query);

    @Query("SELECT * FROM reports")
    LiveData<List<Report>> getAllReports();

    @Query("SELECT * FROM reports WHERE report_giorno=:date")
    LiveData<List<Report>> getAllReportInDate(Date date);

    @Query("SELECT * FROM reports WHERE report_giorno>= :date1 AND report_giorno<=:date2 ORDER BY report_giorno DESC, report_ora DESC")
    LiveData<List<Report>> getAllReportsInPeriod(Date date1, Date date2);

    @Query("SELECT * FROM reports WHERE :S")
    LiveData<List<Report>> getFilterReports(String S);

    @RawQuery(observedEntities = Report.class)
    LiveData<Double> getAVGInDate(SupportSQLiteQuery query);

    @RawQuery(observedEntities = Report.class)
    LiveData<List<AVG>> getAVG(SupportSQLiteQuery query);

}
