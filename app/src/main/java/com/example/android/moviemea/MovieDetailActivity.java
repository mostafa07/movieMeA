package com.example.android.moviemea;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.moviemea.models.MovieDetail;
import com.example.android.moviemea.utilities.NetworkUtils;
import com.example.android.moviemea.utilities.TheMoviesDbJsonUtils;
import com.squareup.picasso.Picasso;

import java.net.URISyntaxException;
import java.net.URL;


public class MovieDetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = MovieDetail.class.getSimpleName();

    // private MovieDetail mMovieDetail;

    private TextView mMovieDetailsTV;
    private ImageView mMoviePosterIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mMovieDetailsTV = findViewById(R.id.movie_detail_all_text_view);
        mMoviePosterIV = findViewById(R.id.movie_detail_poster_image_view);

        int movieId = getIntent().getIntExtra("movieId", -1);
        if (movieId == -1) {
            mMovieDetailsTV.setText("Error: Movie id is unavailable");
        } else {
            new FetchMovieDetailsTask().execute(movieId);
        }
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
//                Glide.with(MovieDetailActivity.this)
//                        .load(moviePosterFullUrl.toString())
//                        .into(mMoviePosterIV);

                Picasso.get().load(moviePosterFullUrl.toString()).into(mMoviePosterIV);

                mMovieDetailsTV.setText(movieDetail.toString());
            }
        }
    }
}
