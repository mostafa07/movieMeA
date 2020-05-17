package com.example.android.moviemea.viewmodels.factories;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.moviemea.data.AppDatabase;
import com.example.android.moviemea.viewmodels.MovieDetailViewModel;


public class MovieDetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private AppDatabase mAppDatabase;
    private Integer mFavoriteMovieId;

    public MovieDetailViewModelFactory(AppDatabase appDatabase, Integer favoriteMovieId) {
        mAppDatabase = appDatabase;
        mFavoriteMovieId = favoriteMovieId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MovieDetailViewModel(mAppDatabase, mFavoriteMovieId);
    }
}
