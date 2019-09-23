package com.example.android.moviemea.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<List<Movie>>, OnSharedPreferenceChangeListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int MOVIES_LOADER_ID = 100;

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private RecyclerView mRecyclerView;
    private MovieAdapter mAdapter;
    private ProgressBar mProgressBar;
    private View mEmptyView;
    private SwipeRefreshLayout mRefreshLayout;

    private LoaderManager mLoaderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppSharedPreferences.init(getApplicationContext());
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        setupNavigationDrawerAndToolbar();
        setupViews();

        mLoaderManager = getSupportLoaderManager();
        mLoaderManager.initLoader(MOVIES_LOADER_ID, null, MainActivity.this);
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

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case (R.id.nav_now_playing): {
                        AppSharedPreferences.write(AppSharedPreferences.MOVIES_ORDER_PATH_PARAM_PREF_KEY,
                                getString(R.string.movies_order_path_param_now_playing));
                        break;
                    }
                    case (R.id.nav_most_popular): {
                        AppSharedPreferences.write(AppSharedPreferences.MOVIES_ORDER_PATH_PARAM_PREF_KEY,
                                getString(R.string.movies_order_path_param_popular));
                        break;
                    }
                    case (R.id.nav_top_rated): {
                        AppSharedPreferences.write(AppSharedPreferences.MOVIES_ORDER_PATH_PARAM_PREF_KEY,
                                getString(R.string.movies_order_path_param_top_rated));
                        break;
                    }
                    case (R.id.nav_upcoming): {
                        AppSharedPreferences.write(AppSharedPreferences.MOVIES_ORDER_PATH_PARAM_PREF_KEY,
                                getString(R.string.movies_order_path_param_upcoming));
                        break;
                    }
                    case (R.id.nav_favorites): {
                        item.setChecked(true);
                        // TODO get favorite movies
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

        mProgressBar = findViewById(R.id.main_progress_bar);
        mEmptyView = findViewById(R.id.main_empty_view);

        mRefreshLayout = findViewById(R.id.main_swipe_refresh_layout);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mLoaderManager.restartLoader(MOVIES_LOADER_ID, null, MainActivity.this);
            }
        });
        mRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }


    /* Overridden Methods */

    /* Handle Recycler View Item Clicks */
    @Override
    public void onClick(Movie movie) {
        Intent movieDetailIntent = new Intent(this, MovieDetailActivity.class);
        movieDetailIntent.putExtra("movieId", movie.getId());
        startActivity(movieDetailIntent);
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

        return new MoviesAsyncTaskLoader(MainActivity.this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Movie>> loader, List<Movie> movies) {
        mRefreshLayout.setRefreshing(false);
        mProgressBar.setVisibility(View.INVISIBLE);

        if (movies != null) {
            mAdapter.setMoviesData(movies);
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
            mLoaderManager.restartLoader(MOVIES_LOADER_ID, null, MainActivity.this);

            String moviesOrderPrefValue = AppSharedPreferences.read(AppSharedPreferences.MOVIES_ORDER_PATH_PARAM_PREF_KEY,
                    AppSharedPreferences.MOVIES_ORDER_PATH_PARAM_DEFAULT_VALUE);

            if (moviesOrderPrefValue.equals(getString(R.string.pref_movies_order_entry_now_playing))) {
                mNavigationView.getMenu().findItem(R.id.nav_now_playing).setChecked(true);
            } else if (moviesOrderPrefValue.equals(getString(R.string.pref_movies_order_entry_popular))) {
                mNavigationView.getMenu().findItem(R.id.nav_most_popular).setChecked(true);
            } else if (moviesOrderPrefValue.equals(getString(R.string.pref_movies_order_entry_top_rated))) {
                mNavigationView.getMenu().findItem(R.id.nav_top_rated).setChecked(true);
            } else if (moviesOrderPrefValue.equals(getString(R.string.pref_movies_order_entry_upcoming))) {
                mNavigationView.getMenu().findItem(R.id.nav_upcoming).setChecked(true);
            }
        }
    }


    /* AsyncTaskLoader Inner Class Used to Fetch Movies */

    private static class MoviesAsyncTaskLoader extends AsyncTaskLoader<List<Movie>> {

        private List<Movie> mData;

        public MoviesAsyncTaskLoader(Context context) {
            super(context);
        }

        @Override
        protected void onStartLoading() {
            if (mData != null) {
                deliverResult(mData);
            } else {
                forceLoad();
            }
        }

        @Nullable
        @Override
        public List<Movie> loadInBackground() {
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
        public void deliverResult(@Nullable List<Movie> data) {
            mData = data;
            super.deliverResult(data);
        }
    }
}
