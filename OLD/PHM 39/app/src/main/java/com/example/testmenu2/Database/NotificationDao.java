package com.example.testmenu2.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NotificationDao {

    @Insert
    void addNotification(Notification notification);

    @Update
    void updateNotification(Notification notification);

    @Delete
    void deleteNotification(Notification notification);

    @Query("SELECT * FROM notification")
    LiveData<Notification> getNotification();
}
