package com.example.android.moviemea.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.moviemea.data.AppDatabase;
import com.example.android.moviemea.models.local.FavoriteMovie;


public class FavoriteMovieViewModel extends ViewModel {

    private LiveData<FavoriteMovie> mFavoriteMovieLiveData;

    public FavoriteMovieViewModel(AppDatabase appDatabase, Integer favoriteMovieId) {
        mFavoriteMovieLiveData = appDatabase.favoriteMovieDao().loadFavoriteById(favoriteMovieId);
    }

    public LiveData<FavoriteMovie> getFavoriteMovie() {
        return mFavoriteMovieLiveData;
    }
}
