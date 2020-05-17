package com.example.android.moviemea.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.moviemea.data.AppDatabase;
import com.example.android.moviemea.models.local.FavoriteMovie;


public class MovieDetailViewModel extends ViewModel {

    private LiveData<FavoriteMovie> mFavoriteMovieLiveData;

    public MovieDetailViewModel(AppDatabase appDatabase, Integer favoriteMovieId) {
        mFavoriteMovieLiveData = appDatabase.favoriteMovieDao().loadFavoriteById(favoriteMovieId);
    }

    public LiveData<FavoriteMovie> getFavoriteMovie() {
        return mFavoriteMovieLiveData;
    }
}
