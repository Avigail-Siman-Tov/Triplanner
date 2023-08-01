package com.triplanner.triplanner.Model;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.triplanner.triplanner.Model.Trip;
import java.util.List;

@Dao
public interface TripDao {
    @Query("select * from Trip where travelerMail=:travelerMail")
    List<Trip> getTripByEmail(String travelerMail);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Trip... trips);

    @Delete
    void deleteTrip(Trip trip);
}