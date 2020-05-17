package com.example.android.moviemea.ui.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
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
import com.example.android.moviemea.models.remote.MovieDetail;
import com.example.android.moviemea.utilities.NetworkUtils;
import com.example.android.moviemea.viewmodels.FavoriteMovieViewModel;
import com.example.android.moviemea.viewmodels.factories.FavoriteMovieViewModelFactory;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.URL;


public class FavoriteMovieActivity extends AppCompatActivity {

    private static final String LOG_TAG = MovieDetail.class.getSimpleName();

    private AppDatabase mAppDatabase;
    private FavoriteMovie mFavoriteMovie;

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
        setContentView(R.layout.activity_favorite_movie);

        setupViews();

        mAppDatabase = AppDatabase.getInstance(getApplicationContext());

        final int movieId = getIntent().getIntExtra("movieId", -1);

        if (movieId != -1) {
            mProgressBar.setVisibility(View.VISIBLE);
            mEmptyViewTV.setVisibility(View.INVISIBLE);

            setupViewModel(movieId);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mEmptyViewTV.setVisibility(View.VISIBLE);
            Toast.makeText(FavoriteMovieActivity.this, getString(R.string.error_no_movie_id),
                    Toast.LENGTH_LONG).show();
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
        FavoriteMovieViewModelFactory viewModelFactory =
                new FavoriteMovieViewModelFactory(mAppDatabase, movieId);
        final FavoriteMovieViewModel favoriteMovieViewModel =
                ViewModelProviders.of(this, viewModelFactory).get(FavoriteMovieViewModel.class);

        favoriteMovieViewModel.getFavoriteMovie().observe(this, new Observer<FavoriteMovie>() {
            @Override
            public void onChanged(FavoriteMovie favoriteMovie) {
                favoriteMovieViewModel.getFavoriteMovie().removeObserver(this);

                populateViews(favoriteMovie);

                mFavoriteMovie = favoriteMovie;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favorite_movie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.menu_favorite_movie_share): {
                // TODO implement share functionality
                break;
            }
            case (R.id.menu_favorite_movie_delete): {
                if (mFavoriteMovie != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(getString(R.string.delete_favorite_dialog_title, mFavoriteMovie.getTitle()));
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                @Override
                                public void run() {
                                    final File imageFilePath = new File(getApplicationContext().getFilesDir(),
                                            mFavoriteMovie.getPosterPath());
                                    if (imageFilePath.exists()) {
                                        imageFilePath.delete();
                                    }

                                    mAppDatabase.favoriteMovieDao().deleteFavorite(mFavoriteMovie);
                                }
                            });
                            Toast.makeText(FavoriteMovieActivity.this, "", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.show();
                }
                break;
            }
            default:
                break;
        }
        return true;
    }

    private void populateViews(FavoriteMovie favoriteMovie) {
        final File imageFile = new File(getApplicationContext().getFilesDir(), favoriteMovie.getPosterPath());
        if (imageFile.exists()) {
            // load image from internal storage path
            Picasso.get().load(imageFile)
                    //.fit()
                    .into(mMoviePosterIV);
        } else {
            // load image from online API
            URL moviePosterFullUrl = NetworkUtils.buildImageUrl(favoriteMovie.getPosterPath());
            Picasso.get().load(moviePosterFullUrl.toString())
                    //.fit()
                    .into(mMoviePosterIV);
        }

        mMovieVoteAverageTV.setBackgroundResource(R.drawable.circle);

        mMovieTitleTV.setText(favoriteMovie.getTitle());
        final String releaseDateStr = getString(R.string.release_date) + " " + favoriteMovie.getReleaseDate();
        mMovieReleaseDateTV.setText(releaseDateStr);
        mMovieVoteAverageTV.setText(favoriteMovie.getVoteAverage().toString());
        mMovieOverviewTV.setText(favoriteMovie.getOverview());
    }
}
