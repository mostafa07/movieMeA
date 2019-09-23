package com.example.android.moviemea.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.moviemea.R;
import com.example.android.moviemea.models.MovieDetail;
import com.example.android.moviemea.utilities.NetworkUtils;
import com.example.android.moviemea.utilities.TheMoviesDbJsonUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;


public class MovieDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<MovieDetail> {

    private static final String LOG_TAG = MovieDetail.class.getSimpleName();
    private static final int MOVIE_DETAIL_LOADER_ID = 200;

    private ImageView mMoviePosterIV;
    private TextView mMovieTitleTV;
    private TextView mMovieReleaseDateTV;
    private TextView mMovieVoteAverageTV;
    private TextView mMovieOverviewTV;
    private ProgressBar mProgressBar;
    private View mEmptyViewTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        setupViews();

        final int movieId = getIntent().getIntExtra("movieId", -1);
        if (movieId != -1) {
            final String LOADER_BUNDLE_MOVIE_ID_KEY = "movieId";
            Bundle loaderBundle = new Bundle();
            loaderBundle.putInt(LOADER_BUNDLE_MOVIE_ID_KEY, movieId);
            getSupportLoaderManager().initLoader(MOVIE_DETAIL_LOADER_ID, loaderBundle, MovieDetailActivity.this);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mEmptyViewTV.setVisibility(View.VISIBLE);
            Toast.makeText(MovieDetailActivity.this, getString(R.string.error_no_movie_id),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void setupViews() {
        mMoviePosterIV = findViewById(R.id.movie_detail_poster_image_view);
        mMovieTitleTV = findViewById(R.id.movie_detail_title_text_view);
        mMovieReleaseDateTV = findViewById(R.id.movie_detail_release_date_text_view);
        mMovieVoteAverageTV = findViewById(R.id.movie_detail_vote_average_text_view);
        mMovieOverviewTV = findViewById(R.id.movie_detail_overview_text_view);
        mProgressBar = findViewById(R.id.movie_detail_progress_bar);
        mEmptyViewTV = findViewById(R.id.movie_detail_empty_view_text_view);
    }


    @NonNull
    @Override
    public Loader<MovieDetail> onCreateLoader(int i, @Nullable Bundle bundle) {
        mProgressBar.setVisibility(View.VISIBLE);
        mEmptyViewTV.setVisibility(View.INVISIBLE);

        final String LOADER_BUNDLE_MOVIE_ID_KEY = "movieId";
        final int movieId = bundle.getInt(LOADER_BUNDLE_MOVIE_ID_KEY, -1);
        return new MovieDetailAsyncTaskLoader(MovieDetailActivity.this, movieId);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<MovieDetail> loader, MovieDetail movieDetail) {
        mProgressBar.setVisibility(View.INVISIBLE);

        if (movieDetail != null) {
            URL moviePosterFullUrl = NetworkUtils.buildImageUrl(movieDetail.getPosterPath());
            Picasso.get().load(moviePosterFullUrl.toString())
                    .fit()
                    .into(mMoviePosterIV);
            mMovieVoteAverageTV.setBackgroundResource(R.drawable.circle);

            mMovieTitleTV.setText(movieDetail.getTitle());
            final String releaseDateStr = getString(R.string.release_date) + " " + movieDetail.getReleaseDate();
            mMovieReleaseDateTV.setText(releaseDateStr);
            mMovieVoteAverageTV.setText(movieDetail.getVoteAverage().toString());
            mMovieOverviewTV.setText(movieDetail.getOverview());
        } else {
            Toast.makeText(MovieDetailActivity.this, getString(R.string.error_no_movie_id),
                    Toast.LENGTH_LONG).show();
            mEmptyViewTV.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<MovieDetail> loader) {
    }


    /* AsyncTaskLoader Inner Class Used to Fetch Movie Details */

    private static class MovieDetailAsyncTaskLoader extends AsyncTaskLoader<MovieDetail> {

        private MovieDetail mData;
        private int mMovieId;

        public MovieDetailAsyncTaskLoader(Context context, int movieId) {
            super(context);
            mMovieId = movieId;
        }

        @Override
        protected void onStartLoading() {
            if (mData != null) {
                // Use cached data
                deliverResult(mData);
            } else {
                forceLoad();
            }
        }

        @Nullable
        @Override
        public MovieDetail loadInBackground() {
            URL movieDetailUrl = NetworkUtils.buildMovieDetailUrlById(mMovieId);

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
        public void deliverResult(@Nullable MovieDetail data) {
            mData = data;
            super.deliverResult(data);
        }
    }
}
