package com.triplanner.triplanner.Model;
import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

//@Database(entities ={Traveler.class,FavoriteCategories.class,Trip.class,OpenHours.class,Place.class}, version = 2,exportSchema = true)
//abstract class AppLocalDbRepository extends RoomDatabase {
//    public abstract TravelerDao travelerDao();
//    public abstract FavoriteCategoriesDao favoriteCategoriesDao();
//    public abstract TripDao tripDao();
//    public abstract OpenHoursDao openHoursDao();
//    public abstract PlaceDao placeDao();
//}
//public class AppLocalDB {
//    public static AppLocalDbRepository getDatabase(Context context) {
//        AppLocalDbRepository db = Room.databaseBuilder(context,
//                        AppLocalDbRepository.class,
//                        "dbFileName.db")
//                .addMigrations(MIGRATION_1_2) // Add the migration here
//                .fallbackToDestructiveMigration() // Use this only during development; it will recreate the database on schema changes
//                .build();
//        Log.d("mylog", "Database creation completed.");
//        return db;
//
//    }
//
//    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
//        @Override
//        public void migrate(SupportSQLiteDatabase database) {
//            Log.d("mylog", "Starting migration from version 1 to 2...");
//            // Add the "picture" column to the Traveler table
//            database.execSQL("ALTER TABLE Traveler ADD COLUMN travelerPicture TEXT");
//            Log.d("my log", "Migration completed.");
//        }
//    };
//
//}


@Database(entities ={Traveler.class, FavoriteCategories.class, Trip.class, OpenHours.class, Place.class}, version = 2, exportSchema = true)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract TravelerDao travelerDao();
    public abstract FavoriteCategoriesDao favoriteCategoriesDao();
    public abstract TripDao tripDao();
    public abstract OpenHoursDao openHoursDao();
    public abstract PlaceDao placeDao();
}

public class AppLocalDB {
    private static AppLocalDbRepository database;

    public static AppLocalDbRepository getDatabase(Context context) {
        if (database == null) {
            synchronized (AppLocalDB.class) {
                if (database == null) {
                    database = Room.databaseBuilder(context, AppLocalDbRepository.class, "dbFileName.db")
                            .addMigrations(MIGRATION_1_2) // Add the migration here
                            .fallbackToDestructiveMigration() // Use this only during development; it will recreate the database on schema changes
                            .build();
                    Log.d("mylog", "Database creation completed.");
                }
            }
        }
        return database;
    }

    public static void closeDatabase() {
        if (database != null && database.isOpen()) {
            database.close();
            database = null;
            Log.d("mylog", "Database closed.");
        }
    }

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            Log.d("mylog", "Starting migration from version 1 to 2...");
            // Add the "picture" column to the Traveler table
            database.execSQL("ALTER TABLE Traveler ADD COLUMN travelerPicture TEXT");
            Log.d("mylog", "Migration completed.");
        }
    };
}






