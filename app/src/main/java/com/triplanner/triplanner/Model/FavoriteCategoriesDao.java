package com.triplanner.triplanner.Model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavoriteCategoriesDao {
    @Query("select FavoriteCategories.category from FavoriteCategories where travelerMail=:travelerMail")
    List<String> getAllFavoriteCategoriesOfTraveler(String travelerMail);

    @Query("select * from FavoriteCategories where travelerMail=:travelerMail")
    List<FavoriteCategories> getAllFavoriteCategories(String travelerMail);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(FavoriteCategories... favoriteCategories);
    @Delete
    void deleteFavoriteCategories(FavoriteCategories favoriteCategories);
}
