package com.triplanner.triplanner.Model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface OpenHoursDao {

    @Query("select OpenHours.open from OpenHours where place_id=:placeId")
    List<String> getOpenHoursOfPlace(String placeId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(OpenHours... openHours);

    @Delete
    void deleteOpenHours(OpenHours openHours);
}
