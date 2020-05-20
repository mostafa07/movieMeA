package com.example.android.moviemea.ui.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.moviemea.R;
import com.example.android.moviemea.ui.adapters.UserReviewAdapter;
import com.example.android.moviemea.models.remote.Review;
import com.example.android.moviemea.utilities.NetworkUtils;
import com.example.android.moviemea.utilities.TheMoviesDbJsonUtils;

import java.net.URL;
import java.util.List;


public class UserReviewsFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<List<Review>>, UserReviewAdapter.UserReviewOnClickHandler {

    private static final String LOG_TAG = UserReviewsFragment.class.getSimpleName();
    private static final int USER_REVIEWS_LOADER_KEY = 100;

    private TextView mUserReviewsHeaderTextView;
    private RecyclerView mReviewsRecyclerView;
    private UserReviewAdapter mAdapter;

    /* Required Empty Constructor */
    public UserReviewsFragment() {
    }

    public static UserReviewsFragment newInstance(int movieId) {
        Bundle args = new Bundle();
        args.putInt("movieId", movieId);

        UserReviewsFragment fragment = new UserReviewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_reviews, container, false);

        mUserReviewsHeaderTextView = rootView.findViewById(R.id.user_reviews_header_text_view);
        mReviewsRecyclerView = rootView.findViewById(R.id.user_reviews_recycler_view);
        mAdapter = new UserReviewAdapter(this);
        mReviewsRecyclerView.setAdapter(mAdapter);
        mReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL, false));
        mReviewsRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL));

        final int movieId = getArguments().getInt("movieId", -1);

        final String LOADER_BUNDLE_REVIEWS_MOVIE_ID_KEY = "movieId";
        Bundle loaderBundle = new Bundle();
        loaderBundle.putInt(LOADER_BUNDLE_REVIEWS_MOVIE_ID_KEY, movieId);
        getLoaderManager().initLoader(USER_REVIEWS_LOADER_KEY, loaderBundle, UserReviewsFragment.this);

        return rootView;
    }


    @NonNull
    @Override
    public Loader<List<Review>> onCreateLoader(int id, @Nullable Bundle bundle) {
        return new ReviewsAsyncTaskLoader(requireContext(), bundle);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Review>> loader, List<Review> reviews) {
        if (reviews != null && !reviews.isEmpty()) {
            mAdapter.setReviewsData(reviews);
            mReviewsRecyclerView.scheduleLayoutAnimation();
        } else {
            mUserReviewsHeaderTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Review>> loader) {
    }

    @Override
    public void onClick(Review review) {
        UserReviewDialogFragment.newInstance(review).show(requireFragmentManager(), UserReviewsFragment.class.getSimpleName());
    }


    private static class ReviewsAsyncTaskLoader extends AsyncTaskLoader<List<Review>> {

        private List<Review> mData;
        private int mMovieId;

        public ReviewsAsyncTaskLoader(@NonNull Context context, Bundle bundle) {
            super(context);

            if (bundle != null) {
                final String reviewsMovieIdBundleKey = context.getResources().getString(R.string.reviews_movie_id_loader_bundle_key);
                mMovieId = bundle.getInt(reviewsMovieIdBundleKey, -1);
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
        public List<Review> loadInBackground() {
            URL reviewsUrl = NetworkUtils.buildMovieReviewsUrl(mMovieId);

            List<Review> reviewsList = null;
            try {
                final String reviewsJsonStr = NetworkUtils.getResponseFromHttpUrl(reviewsUrl);
                reviewsList = TheMoviesDbJsonUtils.extractReviewsListFromJsonStr(reviewsJsonStr);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return reviewsList;
        }

        @Override
        public void deliverResult(@Nullable List<Review> data) {
            mData = data;
            super.deliverResult(data);
        }
    }
}
