package com.example.android.moviemea;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.moviemea.models.MovieDetail;
import com.example.android.moviemea.utilities.NetworkUtils;
import com.example.android.moviemea.utilities.TheMoviesDbJsonUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;


public class MovieDetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = MovieDetail.class.getSimpleName();

    private MovieDetail mMovie;

    private ImageView mMoviePosterIV;
    private TextView mMovieTitleTV;
    private TextView mMovieReleaseDateTV;
    private TextView mMovieVoteAverageTV;
    private TextView mMovieOverviewTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        setupViews();

        int movieId = getIntent().getIntExtra("movieId", -1);
        if (movieId == -1) {
            //TODO: empty view
            //mMovieDetailsTV.setText("Error: Movie id is unavailable");
            Toast.makeText(getApplicationContext(), "Couldn't Fetch Movie!", Toast.LENGTH_SHORT).show();
        } else {
            new FetchMovieDetailsTask().execute(movieId);
        }
    }


    private void setupViews() {
        mMoviePosterIV = findViewById(R.id.movie_detail_poster_image_view);
        mMovieTitleTV = findViewById(R.id.movie_detail_title_text_view);
        mMovieReleaseDateTV = findViewById(R.id.movie_detail_release_date_text_view);
        mMovieVoteAverageTV = findViewById(R.id.movie_detail_vote_average_text_view);
        mMovieOverviewTV = findViewById(R.id.movie_detail_overview_text_view);
    }


    /* AsyncTask Inner Class Used to Fetch Movie Details */

    private class FetchMovieDetailsTask extends AsyncTask<Integer, Void, MovieDetail> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected MovieDetail doInBackground(Integer... integers) {

            int movieId = integers[0];
            URL movieDetailUrl = NetworkUtils.buildMovieDetailUrlById(movieId);

            MovieDetail movieDetail = null;
            try {
                String movieDetailsJsonStr = NetworkUtils.getResponseFromHttpUrl(movieDetailUrl);
                movieDetail = TheMoviesDbJsonUtils.extractMovieDetailFromJsonStr(movieDetailsJsonStr);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return movieDetail;
        }

        @Override
        protected void onPostExecute(MovieDetail movieDetail) {
            if (movieDetail != null) {
                URL moviePosterFullUrl = NetworkUtils.buildImageUrl(movieDetail.getPosterPath());
                Picasso.get().load(moviePosterFullUrl.toString()).into(mMoviePosterIV);
                //TODO: set color based on vote average
                mMovieVoteAverageTV.setBackgroundResource(R.drawable.circle);

                mMovieTitleTV.setText(movieDetail.getTitle());
                mMovieReleaseDateTV.setText("Release date: " + movieDetail.getReleaseDate());
                mMovieVoteAverageTV.setText(Double.toString(movieDetail.getVoteAverage()));
                mMovieOverviewTV.setText(movieDetail.getOverview());

                mMovie = movieDetail;
            }
        }
    }
}
