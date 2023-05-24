package com.triplanner.triplanner.Model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TravelerDao {
    @Query("select * from Traveler")
    List<Traveler> getAllTraveler();

    @Query("select * from Traveler where travelerMail=:travelerMail")
    List<Traveler> getTravelerByEmail(String travelerMail);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Traveler... travelers);

    @Delete
    void deleteTraveler(Traveler traveler);
}
