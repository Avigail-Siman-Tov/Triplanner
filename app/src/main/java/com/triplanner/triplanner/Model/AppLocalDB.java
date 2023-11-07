package com.triplanner.triplanner.Model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities ={Traveler.class,FavoriteCategories.class,Trip.class,OpenHours.class,Place.class}, version = 1,exportSchema = true)
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
//                .addMigrations(MIGRATION_1_2) // Add your migration class here
//                .fallbackToDestructiveMigration() // This allows destructive migrations as a fallback
                .build();
        return db;

    }

//    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
//        @Override
//        public void migrate(SupportSQLiteDatabase database) {
//            // Add SQL statements to modify the schema as needed for version 2.
//            // For example, you can add ALTER TABLE statements to add or modify columns.
//            database.execSQL("ALTER TABLE your_table_name ADD COLUMN new_column_name TEXT");
//        }
//    };

}



//public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
//    @Override
//    public void migrate(SupportSQLiteDatabase database) {
//            // Add SQL statements to modify the schema as needed for version 2.
//            // For example, you can add ALTER TABLE statements to add or modify columns.
//            database.execSQL("ALTER TABLE your_table ADD COLUMN new_column_name TEXT");
//            }
//}




