package com.example.android.moviemea.viewmodels.factories;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.moviemea.database.AppDatabase;
import com.example.android.moviemea.viewmodels.FavoriteMovieViewModel;


public class FavoriteMovieViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase mAppDatabase;
    private final Integer mFavoriteMovieId;

    public FavoriteMovieViewModelFactory(AppDatabase appDatabase, Integer favoriteMovieId) {
        mAppDatabase = appDatabase;
        mFavoriteMovieId = favoriteMovieId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new FavoriteMovieViewModel(mAppDatabase, mFavoriteMovieId);
    }
}
