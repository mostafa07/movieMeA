package com.example.android.moviemea.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.android.moviemea.database.AppDatabase;
import com.example.android.moviemea.database.FavoriteMovie;

import java.util.List;


public class FavoritesViewModel extends AndroidViewModel {

    private LiveData<List<FavoriteMovie>> mFavoritesLiveData;

    public FavoritesViewModel(@NonNull Application application) {
        super(application);
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        mFavoritesLiveData = appDatabase.favoriteMovieDao().loadAllFavorites();
    }

    public LiveData<List<FavoriteMovie>> getFavorites() {
        return mFavoritesLiveData;
    }
}
