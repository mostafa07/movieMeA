package com.example.android.moviemea;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;

import com.example.android.moviemea.adapters.MovieAdapter;
import com.example.android.moviemea.models.Movie;
import com.example.android.moviemea.utilities.NetworkUtils;
import com.example.android.moviemea.utilities.TheMoviesDbJsonUtils;

import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private MovieAdapter mAdapter;
    private ProgressBar mProgressIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.movies_recycler_view);
        mAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        new FetchMoviesTask().execute();
    }

    /* Handle Recycler View Item Clicks */

    @Override
    public void onClick(Movie movie) {

        Intent movieDetailIntent = new Intent(this, MovieDetailActivity.class);
        movieDetailIntent.putExtra("movieId", movie.getId());
        startActivity(movieDetailIntent);
    }


    /* AsyncTask Inner Class Used to Fetch Movies */

    private class FetchMoviesTask extends AsyncTask<Void, Void, ArrayList<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Movie> doInBackground(Void... voids) {

            final int pageNum = 1;
            URL discoverMoviesUrl = NetworkUtils.buildMoviesUrl(pageNum);

            ArrayList<Movie> moviesList = null;
            try {
                String moviesJsonStr = NetworkUtils.getResponseFromHttpUrl(discoverMoviesUrl);
                moviesList = (ArrayList<Movie>) TheMoviesDbJsonUtils.extractMovieListFromJsonStr(moviesJsonStr);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return moviesList;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> moviesList) {
            if (moviesList != null)
                mAdapter.setMoviesData(moviesList);
        }
    }
}
