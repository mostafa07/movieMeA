package com.example.android.moviemea.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface FavoriteMovieDao {

    @Query("SELECT * FROM favorite_movie")
    LiveData<List<FavoriteMovie>> loadAllFavorites();

    @Query("SELECT * FROM favorite_movie WHERE id = :id")
    LiveData<FavoriteMovie> loadFavoriteById(int id);

    @Insert
    void insertFavorite(FavoriteMovie favoriteMovie);

    // TODO review strategy to find what is suitable for our case
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateFavorite(FavoriteMovie favoriteMovie);

    @Delete
    void deleteFavorite(FavoriteMovie favoriteMovie);

    @Query("DELETE FROM favorite_movie")
    void deleteAllFavorites();

    @Query("DELETE FROM favorite_movie WHERE id = :id")
    void deleteFavoriteById(int id);
}
