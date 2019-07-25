package com.example.android.moviemea;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.moviemea.adapters.MovieAdapter;
import com.example.android.moviemea.data.AppPreferences;
import com.example.android.moviemea.models.Movie;
import com.example.android.moviemea.utilities.NetworkUtils;
import com.example.android.moviemea.utilities.TheMoviesDbJsonUtils;

import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

	private static final String LOG_TAG = MainActivity.class.getSimpleName();

	private RecyclerView mRecyclerView;
	private MovieAdapter mAdapter;
	private ProgressBar mProgressBar;
	private View mEmptyView;
	private SwipeRefreshLayout mRefreshLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setupViews();

		new FetchMoviesTask().execute();
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
				new FetchMoviesTask().execute();
			}
		});
		mRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
	}


	/* AsyncTask Inner Class Used to Fetch Movies */

	private class FetchMoviesTask extends AsyncTask<Void, Void, ArrayList<Movie>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mAdapter.clearMoviesData();

			mEmptyView.setVisibility(View.INVISIBLE);
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
			mRefreshLayout.setRefreshing(false);
			mProgressBar.setVisibility(View.INVISIBLE);

			if (moviesList != null) {
				mAdapter.setMoviesData(moviesList);
			} else {
				Toast.makeText(getApplicationContext(), getString(R.string.error_no_data_found), Toast.LENGTH_LONG).show();
				mEmptyView.setVisibility(View.VISIBLE);
			}
		}
	}


	/* Overridden Methods */

	/* Handle Recycler View Item Clicks */
	@Override
	public void onClick(Movie movie) {
		Intent movieDetailIntent = new Intent(this, MovieDetailActivity.class);
		movieDetailIntent.putExtra("movieId", movie.getId());
		startActivity(movieDetailIntent);
	}

	/* Options Menu Setup */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/* Handle Options Menu Item Clicks */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		mProgressBar.setVisibility(View.VISIBLE);

		switch (item.getItemId()) {
			case (R.id.action_now_playing): {
				AppPreferences.setMoviesOrderPathParam(getString(R.string.movies_order_path_param_now_playing));
				new FetchMoviesTask().execute();
				break;
			}
			case (R.id.action_most_popular): {
				AppPreferences.setMoviesOrderPathParam(getString(R.string.movies_order_path_param_popular));
				new FetchMoviesTask().execute();
				break;
			}
			case (R.id.action_top_rated): {
				AppPreferences.setMoviesOrderPathParam(getString(R.string.movies_order_path_param_top_rated));
				new FetchMoviesTask().execute();
				break;
			}
			case (R.id.action_upcoming): {
				AppPreferences.setMoviesOrderPathParam(getString(R.string.movies_order_path_param_upcoming));
				new FetchMoviesTask().execute();
				break;
			}
			default:
				break;
		}
		mRecyclerView.smoothScrollToPosition(0);

		return super.onOptionsItemSelected(item);
	}
}
