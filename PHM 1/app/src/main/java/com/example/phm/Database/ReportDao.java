package com.example.phm.Database;

import androidx.room.Dao;
import androidx.room.Insert;

@Dao
public interface ReportDao {
    @Insert
    public void addReport(Report report);

}
