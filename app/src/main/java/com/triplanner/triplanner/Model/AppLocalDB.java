package com.triplanner.triplanner.Model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities ={Traveler.class,FavoriteCategories.class,Trip.class,OpenHours.class,Place.class}, version = 3,exportSchema = true)

//@Database(entities ={Traveler.class,FavoriteCategories.class,Trip.class,OpenHours.class,Place.class}, version = 1,exportSchema = true)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract TravelerDao travelerDao();
    public abstract FavoriteCategoriesDao favoriteCategoriesDao();
    public abstract TripDao tripDao();
    public abstract OpenHoursDao openHoursDao();
    public abstract PlaceDao placeDao();
}
public class AppLocalDB {
    public static AppLocalDbRepository getDatabase(Context context) {
        AppLocalDbRepository db = Room.databaseBuilder(context,
                        AppLocalDbRepository.class,
                        "dbFileName.db")
                .fallbackToDestructiveMigration()
                .build();
        return db;

    }
}

