package com.example.android.moviemea;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.moviemea.adapters.MovieAdapter;
import com.example.android.moviemea.models.MovieDetail;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<MovieDetail> moviesList = populateMoviesList();

        RecyclerView recyclerView = findViewById(R.id.rv_movies);
        recyclerView.setAdapter(new MovieAdapter(moviesList));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private ArrayList<MovieDetail> populateMoviesList() {

        ArrayList<MovieDetail> moviesList = new ArrayList<>();
        moviesList.add(new MovieDetail("MovieDetail O;RVNOWENVOW"));
        moviesList.add(new MovieDetail("MovieDetail O;RVNOWENVOW"));
        moviesList.add(new MovieDetail("MovieDetail O;RVNOWENVOW"));
        moviesList.add(new MovieDetail("MovieDetail O;RVNOWENVOW"));
        moviesList.add(new MovieDetail("MovieDetail O;RVNOWENVOW"));
        moviesList.add(new MovieDetail("MovieDetail O;RVNOWENVOW"));
        moviesList.add(new MovieDetail("MovieDetail O;RVNOWENVOW"));
        moviesList.add(new MovieDetail("MovieDetail O;RVNOWENVOW"));
        moviesList.add(new MovieDetail("MovieDetail O;RVNOWENVOW"));
        moviesList.add(new MovieDetail("MovieDetail O;RVNOWENVOW"));
        moviesList.add(new MovieDetail("MovieDetail O;RVNOWENVOW"));
        moviesList.add(new MovieDetail("MovieDetail O;RVNOWENVOW"));
        moviesList.add(new MovieDetail("MovieDetail O;RVNOWENVOW"));
        moviesList.add(new MovieDetail("MovieDetail O;RVNOWENVOW"));
        moviesList.add(new MovieDetail("MovieDetail O;RVNOWENVOW"));
        moviesList.add(new MovieDetail("MovieDetail O;RVNOWENVOW"));
        moviesList.add(new MovieDetail("MovieDetail O;RVNOWENVOW"));
        moviesList.add(new MovieDetail("MovieDetail O;RVNOWENVOW"));

        return moviesList;
    }
}
