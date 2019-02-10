package com.example.android.moviemea;

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


public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private MovieAdapter mAdapter;
    private ProgressBar mProgressIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ArrayList<MovieDetail> moviesList = populateMoviesList();

        mRecyclerView = findViewById(R.id.rv_movies);
        mAdapter = new MovieAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        new FetchMoviesTask().execute();
    }


    private class FetchMoviesTask extends AsyncTask<Void, Void, ArrayList<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Movie> doInBackground(Void... voids) {

            URL discoverMoviesUrl = NetworkUtils.buildDiscoverMoviesUrl();

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
