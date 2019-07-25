package com.example.android.moviemea;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.moviemea.models.MovieDetail;
import com.example.android.moviemea.utilities.NetworkUtils;
import com.example.android.moviemea.utilities.TheMoviesDbJsonUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;


public class MovieDetailActivity extends AppCompatActivity {

	private static final String LOG_TAG = MovieDetail.class.getSimpleName();

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
			new FetchMovieDetailsTask().execute(movieId);
		} else {
			mProgressBar.setVisibility(View.INVISIBLE);
			mEmptyViewTV.setVisibility(View.VISIBLE);
			Toast.makeText(this, getString(R.string.error_no_movie_id), Toast.LENGTH_LONG).show();
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


	/* AsyncTask Inner Class Used to Fetch Movie Details */

	private class FetchMovieDetailsTask extends AsyncTask<Integer, Void, MovieDetail> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			mProgressBar.setVisibility(View.VISIBLE);
			mEmptyViewTV.setVisibility(View.INVISIBLE);
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
			mProgressBar.setVisibility(View.INVISIBLE);

			if (movieDetail != null) {
				URL moviePosterFullUrl = NetworkUtils.buildImageUrl(movieDetail.getPosterPath());
				Picasso.get().load(moviePosterFullUrl.toString()).into(mMoviePosterIV);
				mMovieVoteAverageTV.setBackgroundResource(R.drawable.circle);

				mMovieTitleTV.setText(movieDetail.getTitle());
				final String releaseDateStr = getString(R.string.release_date) + " " + movieDetail.getReleaseDate();
				mMovieReleaseDateTV.setText(releaseDateStr);
				mMovieVoteAverageTV.setText(movieDetail.getVoteAverage().toString());
				mMovieOverviewTV.setText(movieDetail.getOverview());
			} else {
				Toast.makeText(getApplicationContext(), getString(R.string.error_no_movie_id), Toast.LENGTH_LONG).show();
				mEmptyViewTV.setVisibility(View.VISIBLE);
			}
		}
	}
}
