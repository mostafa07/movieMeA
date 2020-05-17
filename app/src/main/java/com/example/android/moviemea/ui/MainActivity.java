package com.example.android.moviemea.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.navigation.NavigationView;

import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.moviemea.R;
import com.example.android.moviemea.adapters.MovieAdapter;
import com.example.android.moviemea.data.AppSharedPreferences;
import com.example.android.moviemea.models.Movie;
import com.example.android.moviemea.utilities.NetworkUtils;
import com.example.android.moviemea.utilities.TheMoviesDbJsonUtils;

import java.net.URL;
import java.util.List;


public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<List<Movie>>, OnSharedPreferenceChangeListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int MOVIES_LOADER_ID = 100;
    private static final int INTENT_EXTRA_MODE_FROM_MAIN = 1001;

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private RecyclerView mRecyclerView;
    private MovieAdapter mAdapter;
    private ProgressBar mProgressBar;
    private View mEmptyView;
    private SwipeRefreshLayout mRefreshLayout;
    private LoaderManager mLoaderManager;

    private String mCurrentMoviesOrderPrefValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppSharedPreferences.init(getApplicationContext());
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        setupViews();
        initMoviesLoaderUponCreate();
        setupNavigationDrawerAndToolbar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCheckedNavMenuItem();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    private void setupNavigationDrawerAndToolbar() {
        mDrawerLayout = findViewById(R.id.main_drawer_layout);
        Toolbar mToolbar = findViewById(R.id.main_toolbar);
        mNavigationView = findViewById(R.id.main_navigation_view);

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        updateCheckedNavMenuItem();

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                final String nowPlaying = getString(R.string.movies_order_path_param_now_playing);
                final String mostPopular = getString(R.string.movies_order_path_param_popular);
                final String topRated = getString(R.string.movies_order_path_param_top_rated);
                final String upcoming = getString(R.string.movies_order_path_param_upcoming);

                switch (item.getItemId()) {
                    case (R.id.nav_now_playing): {
                        restartLoader(nowPlaying);
                        break;
                    }
                    case (R.id.nav_most_popular): {
                        restartLoader(mostPopular);
                        break;
                    }
                    case (R.id.nav_top_rated): {
                        restartLoader(topRated);
                        break;
                    }
                    case (R.id.nav_upcoming): {
                        restartLoader(upcoming);
                        break;
                    }
                    case (R.id.nav_favorites): {
                        Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case (R.id.nav_settings): {
                        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivity(intent);
                        break;
                    }
                    default:
                        break;
                }

                mRecyclerView.smoothScrollToPosition(0);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    private void setupViews() {
        mRecyclerView = findViewById(R.id.main_movies_recycler_view);
        mAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        GridLayoutManager gridLayoutManager =
                new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        mProgressBar = findViewById(R.id.main_progress_bar);
        mEmptyView = findViewById(R.id.main_empty_view);

        mRefreshLayout = findViewById(R.id.main_swipe_refresh_layout);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                restartLoader(mCurrentMoviesOrderPrefValue);
            }
        });
        mRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void initMoviesLoaderUponCreate() {
        mLoaderManager = getSupportLoaderManager();

        final String moviesOrderBundleKey = getResources().getString(R.string.movies_order_loader_bundle_key);
        final String moviesOrderPrefValue = AppSharedPreferences.read(AppSharedPreferences.MOVIES_ORDER_PATH_PARAM_PREF_KEY,
                AppSharedPreferences.MOVIES_ORDER_PATH_PARAM_DEFAULT_VALUE);
        Bundle bundle = new Bundle();
        bundle.putString(moviesOrderBundleKey, moviesOrderPrefValue);
        mLoaderManager.initLoader(MOVIES_LOADER_ID, bundle, MainActivity.this);

        mCurrentMoviesOrderPrefValue = moviesOrderPrefValue;
    }

    /* Overridden Methods */

    /* Handle Recycler View Item Clicks */
    @Override
    public void onClick(Movie movie) {
        Intent movieDetailIntent = new Intent(this, MovieDetailActivity.class);
        movieDetailIntent.putExtra("movieId", movie.getId());
        movieDetailIntent.putExtra("mode", INTENT_EXTRA_MODE_FROM_MAIN);
        startActivity(movieDetailIntent);
    }

    @Override
    public void onLongClick(Movie movie) {
        // TODO: handle long click on main activity items
        Toast.makeText(this, "Long click", Toast.LENGTH_SHORT).show();
    }

    /* Handle Options Menu Item Clicks */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<List<Movie>> onCreateLoader(int i, @Nullable Bundle bundle) {
        mProgressBar.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.INVISIBLE);

        return new MoviesAsyncTaskLoader(MainActivity.this, bundle);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Movie>> loader, List<Movie> movies) {
        mRefreshLayout.setRefreshing(false);
        mProgressBar.setVisibility(View.INVISIBLE);

        if (movies != null) {
            mAdapter.setMoviesData(movies);
            mRecyclerView.scheduleLayoutAnimation();
        } else {
            Toast.makeText(MainActivity.this, getString(R.string.error_no_data_found),
                    Toast.LENGTH_LONG).show();
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Movie>> loader) {
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_movies_order_key))) {
            final String moviesOrderPrefValue = AppSharedPreferences.read(AppSharedPreferences.MOVIES_ORDER_PATH_PARAM_PREF_KEY,
                    AppSharedPreferences.MOVIES_ORDER_PATH_PARAM_DEFAULT_VALUE);
            restartLoader(moviesOrderPrefValue);
        }
    }

    /* Helper Methods */

    /* Restart Loader and Update Current Movie Order Preference and Navigation Menu Item */
    private void restartLoader(String moviesOrderPrefValue) {
        final String moviesOrderLoaderBundleKey = getString(R.string.movies_order_loader_bundle_key);
        Bundle bundle = new Bundle();
        bundle.putString(moviesOrderLoaderBundleKey, moviesOrderPrefValue);
        mLoaderManager.restartLoader(MOVIES_LOADER_ID, bundle, MainActivity.this);

        mCurrentMoviesOrderPrefValue = moviesOrderPrefValue;
        updateCheckedNavMenuItem();
    }

    /* Highlight Navigation Menu Item According to Current Movie Order Preference */
    private void updateCheckedNavMenuItem() {
        if (mCurrentMoviesOrderPrefValue.equals(getString(R.string.pref_movies_order_entry_now_playing))) {
            mNavigationView.getMenu().findItem(R.id.nav_now_playing).setChecked(true);
        } else if (mCurrentMoviesOrderPrefValue.equals(getString(R.string.pref_movies_order_entry_popular))) {
            mNavigationView.getMenu().findItem(R.id.nav_most_popular).setChecked(true);
        } else if (mCurrentMoviesOrderPrefValue.equals(getString(R.string.pref_movies_order_entry_top_rated))) {
            mNavigationView.getMenu().findItem(R.id.nav_top_rated).setChecked(true);
        } else if (mCurrentMoviesOrderPrefValue.equals(getString(R.string.pref_movies_order_entry_upcoming))) {
            mNavigationView.getMenu().findItem(R.id.nav_upcoming).setChecked(true);
        }
    }

    /* AsyncTaskLoader Inner Class Used to Fetch Movies */

    private static class MoviesAsyncTaskLoader extends AsyncTaskLoader<List<Movie>> {

        private List<Movie> mData;
        private String mMoviesOrderNotFromSharedPreferences;

        public MoviesAsyncTaskLoader(Context context, Bundle bundle) {
            super(context);

            if (bundle != null) {
                final String moviesOrderBundleKey = context.getResources().getString(R.string.movies_order_loader_bundle_key);
                mMoviesOrderNotFromSharedPreferences = bundle.getString(moviesOrderBundleKey, null);
            }
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
        public List<Movie> loadInBackground() {
            URL moviesUrl = NetworkUtils.buildMoviesUrl(mMoviesOrderNotFromSharedPreferences);

            List<Movie> moviesList = null;
            try {
                final String moviesJsonStr = NetworkUtils.getResponseFromHttpUrl(moviesUrl);
                moviesList = TheMoviesDbJsonUtils.extractMovieListFromJsonStr(moviesJsonStr);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return moviesList;
        }

        @Override
        public void deliverResult(@Nullable List<Movie> data) {
            mData = data;
            super.deliverResult(data);
        }
    }
}
