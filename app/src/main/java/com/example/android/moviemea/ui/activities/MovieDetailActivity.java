package com.example.android.moviemea.ui.activities;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.moviemea.R;
import com.example.android.moviemea.data.AppDatabase;
import com.example.android.moviemea.models.local.FavoriteMovie;
import com.example.android.moviemea.executors.AppExecutors;
import com.example.android.moviemea.ui.fragments.UserReviewsFragment;
import com.example.android.moviemea.models.remote.MovieDetail;
import com.example.android.moviemea.ui.fragments.VideosFragment;
import com.example.android.moviemea.utilities.NetworkUtils;
import com.example.android.moviemea.utilities.TheMoviesDbJsonUtils;
import com.example.android.moviemea.viewmodels.MovieDetailViewModel;
import com.example.android.moviemea.viewmodels.factories.MovieDetailViewModelFactory;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;


public class MovieDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<MovieDetail> {

    private static final String LOG_TAG = MovieDetail.class.getSimpleName();
    private static final int MOVIE_DETAIL_LOADER_ID = 200;

    private AppDatabase mAppDatabase;
    private MovieDetail mMovieDetail;
    private boolean mIsFavorited;
    private boolean mIsInitiallyFavorited;

    private ImageView mMoviePosterIV;
    private TextView mMovieTitleTV;
    private TextView mMovieReleaseDateTV;
    private TextView mMovieVoteAverageTV;
    private TextView mMovieOverviewTV;
    private ProgressBar mProgressBar;
    private View mEmptyViewTV;
    private MenuItem mStarMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        setupViews();

        mAppDatabase = AppDatabase.getInstance(getApplicationContext());

        final int movieId = getIntent().getIntExtra("movieId", -1);
        if (movieId != -1) {
            final String LOADER_BUNDLE_MOVIE_ID_KEY = "movieId";
            Bundle loaderBundle = new Bundle();
            loaderBundle.putInt(LOADER_BUNDLE_MOVIE_ID_KEY, movieId);
            getSupportLoaderManager().initLoader(MOVIE_DETAIL_LOADER_ID, loaderBundle, MovieDetailActivity.this);

            displayReviewsAndVideosFragments(movieId);

            setupViewModel(movieId);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mEmptyViewTV.setVisibility(View.VISIBLE);
            Toast.makeText(MovieDetailActivity.this, getString(R.string.error_no_movie_id),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!mIsInitiallyFavorited && mIsFavorited) {
            final URL moviePosterFullUrl = NetworkUtils.buildImageUrl(mMovieDetail.getPosterPath());
            // TODO: research a way to avoid re-loading image from the internet, and instead get
            //  the one loaded into the image view
            Picasso.get().load(moviePosterFullUrl.toString())
                    .into(getImageDownloadTarget(mMovieDetail.getPosterPath()));

            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    final FavoriteMovie favoriteMovie = new FavoriteMovie(mMovieDetail.getId(),
                            mMovieDetail.getTitle(), mMovieDetail.getOverview(), mMovieDetail.getReleaseDate(),
                            mMovieDetail.getPosterPath(), mMovieDetail.getVoteAverage());

                    mAppDatabase.favoriteMovieDao().insertFavorite(favoriteMovie);
                }
            });
        } else if (mIsInitiallyFavorited && !mIsFavorited) {
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    final File imageFilePath = new File(getApplicationContext().getFilesDir(),
                            mMovieDetail.getPosterPath());
                    if (imageFilePath.exists()) {
                        imageFilePath.delete();
                    }
                    mAppDatabase.favoriteMovieDao().deleteFavoriteById(mMovieDetail.getId());
                }
            });
        }
    }

    private void setupViews() {
        mMoviePosterIV = findViewById(R.id.common_movie_detail_poster_image_view);
        mMovieTitleTV = findViewById(R.id.common_movie_detail_title_text_view);
        mMovieReleaseDateTV = findViewById(R.id.common_movie_detail_release_date_text_view);
        mMovieVoteAverageTV = findViewById(R.id.common_movie_detail_vote_average_text_view);
        mMovieOverviewTV = findViewById(R.id.common_movie_detail_overview_text_view);
        mProgressBar = findViewById(R.id.common_movie_detail_progress_bar);
        mEmptyViewTV = findViewById(R.id.common_movie_detail_empty_view_text_view);
    }

    private void setupViewModel(int movieId) {
        MovieDetailViewModelFactory viewModelFactory =
                new MovieDetailViewModelFactory(mAppDatabase, movieId);
        final MovieDetailViewModel movieDetailViewModel =
                ViewModelProviders.of(this, viewModelFactory).get(MovieDetailViewModel.class);

        movieDetailViewModel.getFavoriteMovie().observe(this, new Observer<FavoriteMovie>() {
            @Override
            public void onChanged(FavoriteMovie favoriteMovie) {
                movieDetailViewModel.getFavoriteMovie().removeObserver(this);

                if (favoriteMovie != null) {
                    mIsInitiallyFavorited = true;
                    mIsFavorited = true;
                } else {
                    mIsInitiallyFavorited = false;
                    mIsFavorited = false;
                }

                invalidateOptionsMenu();
            }
        });
    }

    private void displayReviewsAndVideosFragments(int movieId) {
        final VideosFragment videosFragment = VideosFragment.newInstance(movieId);
        final UserReviewsFragment userReviewsFragment = UserReviewsFragment.newInstance(movieId);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.movie_detail_videos_fragment_container, videosFragment)
                .add(R.id.movie_detail_reviews_fragment_container, userReviewsFragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movie_detail, menu);

        mStarMenuItem = menu.findItem(R.id.menu_movie_detail_star);
        mStarMenuItem.setIcon(mIsInitiallyFavorited ? R.drawable.ic_star_yellow_24dp : R.drawable.ic_star_border_white_24dp);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.menu_movie_detail_star): {
                mIsFavorited = !mIsFavorited;
                mStarMenuItem.setIcon(mIsFavorited ? R.drawable.ic_star_yellow_24dp : R.drawable.ic_star_border_white_24dp);
                break;
            }
            case (R.id.menu_movie_detail_share): {
                // TODO implement share functionality
                break;
            }
            default:
                break;
        }
        return true;
    }

    private void populateViews() {
        URL moviePosterFullUrl = NetworkUtils.buildImageUrl(mMovieDetail.getPosterPath());
        Picasso.get().load(moviePosterFullUrl.toString())
                //.fit()
                .into(mMoviePosterIV);

        mMovieVoteAverageTV.setBackgroundResource(R.drawable.circle);

        mMovieTitleTV.setText(mMovieDetail.getTitle());
        final String releaseDateStr = getString(R.string.release_date) + " " + mMovieDetail.getReleaseDate();
        mMovieReleaseDateTV.setText(releaseDateStr);
        mMovieVoteAverageTV.setText(mMovieDetail.getVoteAverage().toString());
        mMovieOverviewTV.setText(mMovieDetail.getOverview());
    }

    private Target getImageDownloadTarget(final String fileName) {
        return new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            File file = new File(getApplicationContext().getFilesDir(), fileName);
                            FileOutputStream fileOutputStream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                            fileOutputStream.flush();
                            fileOutputStream.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };
    }

    @NonNull
    @Override
    public Loader<MovieDetail> onCreateLoader(int i, @Nullable Bundle bundle) {
        mProgressBar.setVisibility(View.VISIBLE);
        mEmptyViewTV.setVisibility(View.INVISIBLE);

        // TODO: make it pass bundle directly
        final String LOADER_BUNDLE_MOVIE_ID_KEY = "movieId";
        final int movieId = bundle.getInt(LOADER_BUNDLE_MOVIE_ID_KEY, -1);
        return new MovieDetailAsyncTaskLoader(MovieDetailActivity.this, movieId);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<MovieDetail> loader, MovieDetail movieDetail) {
        mProgressBar.setVisibility(View.INVISIBLE);

        if (movieDetail != null) {
            // TODO review cleanliness of this solution
            mMovieDetail = movieDetail;

            populateViews();
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
            URL movieDetailUrl = NetworkUtils.buildMovieDetailUrl(mMovieId);

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
