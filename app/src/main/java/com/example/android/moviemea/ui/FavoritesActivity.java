package com.example.android.moviemea.ui;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.moviemea.R;
import com.example.android.moviemea.adapters.FavoriteMovieAdapter;
import com.example.android.moviemea.database.AppDatabase;
import com.example.android.moviemea.database.FavoriteMovie;
import com.example.android.moviemea.executors.AppExecutors;
import com.example.android.moviemea.viewmodels.FavoritesViewModel;

import java.util.List;


public class FavoritesActivity extends AppCompatActivity implements FavoriteMovieAdapter.FavoriteMovieOnClickHandler {

    private RecyclerView mRecyclerView;
    private FavoriteMovieAdapter mAdapter;

    private AppDatabase mAppDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        setupViews();

        mAppDatabase = AppDatabase.getInstance(getApplicationContext());
        setupViewModel();
    }

    private void setupViews() {
        mRecyclerView = findViewById(R.id.favorite_movies_recycler_view);
        mAdapter = new FavoriteMovieAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
                final FavoriteMovie favoriteToDelete = mAdapter.getFavoriteMoviesData().get(viewHolder.getAdapterPosition());
                deleteFavorite(favoriteToDelete);
            }
        }).attachToRecyclerView(mRecyclerView);
    }

    private void setupViewModel() {
        FavoritesViewModel favoritesViewModel = ViewModelProviders.of(this).get(FavoritesViewModel.class);
        favoritesViewModel.getFavorites().observe(this, new Observer<List<FavoriteMovie>>() {
            @Override
            public void onChanged(List<FavoriteMovie> favoriteMovies) {
                mAdapter.setFavoriteMoviesData(favoriteMovies);
                mRecyclerView.scheduleLayoutAnimation();
            }
        });
    }

    // TODO: add menu item to delete all favorites
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(FavoriteMovie favoriteMovie) {
        Intent movieDetailIntent = new Intent(this, FavoriteMovieActivity.class);
        movieDetailIntent.putExtra("movieId", favoriteMovie.getId());
        startActivity(movieDetailIntent);
    }

    @Override
    public void onLongClick(FavoriteMovie favoriteMovie) {
        // TODO: handle long clicks on favorites activity items
        Toast.makeText(this, "Long click", Toast.LENGTH_SHORT).show();
    }

    private void deleteFavorite(final FavoriteMovie favoriteToDelete) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mAppDatabase.favoriteMovieDao().deleteFavorite(favoriteToDelete);
            }
        });
    }
}
