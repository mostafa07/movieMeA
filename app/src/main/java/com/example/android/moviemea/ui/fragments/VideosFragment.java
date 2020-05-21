package com.example.android.moviemea.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.example.android.moviemea.models.remote.Video;
import com.example.android.moviemea.ui.adapters.VideoAdapter;
import com.example.android.moviemea.utilities.NetworkUtils;
import com.example.android.moviemea.utilities.TheMoviesDbJsonUtils;

import java.net.URL;
import java.util.List;


public class VideosFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<List<Video>>, VideoAdapter.VideoClickHandler {

    private static final String LOG_TAG = VideosFragment.class.getSimpleName();
    private static final int VIDEOS_LOADER_KEY = 200;

    private TextView mVideosHeaderTextView;
    private RecyclerView mVideosRecyclerView;
    private VideoAdapter mAdapter;

    /* Required Empty Constructor */
    public VideosFragment() {
    }

    public static VideosFragment newInstance(int movieId) {
        Bundle args = new Bundle();
        args.putInt("movieId", movieId);

        VideosFragment fragment = new VideosFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_videos, container, false);

        mVideosHeaderTextView = rootView.findViewById(R.id.videos_header_text_view);
        mVideosRecyclerView = rootView.findViewById(R.id.videos_recycler_view);
        mAdapter = new VideoAdapter(this);
        mVideosRecyclerView.setAdapter(mAdapter);
        mVideosRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        mVideosRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        final int movieId = getArguments().getInt("movieId", -1);

        final String LOADER_BUNDLE_VIDEOS_MOVIE_ID_KEY = "movieId";
        Bundle loaderBundle = new Bundle();
        loaderBundle.putInt(LOADER_BUNDLE_VIDEOS_MOVIE_ID_KEY, movieId);
        getLoaderManager().initLoader(VIDEOS_LOADER_KEY, loaderBundle, VideosFragment.this);

        return rootView;
    }


    @NonNull
    @Override
    public Loader<List<Video>> onCreateLoader(int id, @Nullable Bundle bundle) {
        return new VideosAsyncTaskLoader(requireContext(), bundle);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Video>> loader, List<Video> videos) {
        if (videos != null && !videos.isEmpty()) {
            mAdapter.setVideosData(videos);
            mVideosRecyclerView.scheduleLayoutAnimation();
        } else {
            mVideosHeaderTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Video>> loader) {
    }


    @Override
    public void onClick(Video video) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + video.getKey())));
    }


    private static class VideosAsyncTaskLoader extends AsyncTaskLoader<List<Video>> {

        private List<Video> mData;
        private int mMovieId;

        public VideosAsyncTaskLoader(@NonNull Context context, Bundle bundle) {
            super(context);

            if (bundle != null) {
                final String videosMovieIdBundleKey = context.getResources().getString(R.string.videos_movie_id_loader_bundle_key);
                mMovieId = bundle.getInt(videosMovieIdBundleKey, -1);
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
        public List<Video> loadInBackground() {
            URL videosUrl = NetworkUtils.buildMovieVideosUrl(mMovieId);

            List<Video> videosList = null;
            try {
                final String videosJsonStr = NetworkUtils.getResponseFromHttpUrl(videosUrl);
                videosList = TheMoviesDbJsonUtils.extractVideosListFromJsonStr(videosJsonStr);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return videosList;
        }

        @Override
        public void deliverResult(@Nullable List<Video> data) {
            mData = data;
            super.deliverResult(data);
        }
    }
}
