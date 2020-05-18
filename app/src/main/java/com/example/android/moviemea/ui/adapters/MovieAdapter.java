package com.example.android.moviemea.ui.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.moviemea.R;
import com.example.android.moviemea.models.remote.Movie;
import com.example.android.moviemea.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    private List<Movie> mMoviesList;
    private final MovieAdapterOnClickHandler mClickHandler;

    /* Constructor */
    public MovieAdapter(MovieAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    /* On Click Handler */
    public interface MovieAdapterOnClickHandler {

        void onClick(Movie movie);

        void onLongClick(Movie movie);
    }

    /* Helper Method to Update Movies Data */
    public void setMoviesData(List<Movie> moviesList) {
        mMoviesList = new ArrayList<>(moviesList);
        notifyDataSetChanged();
    }

    /* Helper Method to Clear Movies Data */
    public void clearMoviesData() {
        if (mMoviesList != null)
            mMoviesList.clear();
        notifyDataSetChanged();
    }

    /* Adapter Overridden Methods */

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_grid_movie, viewGroup, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int position) {
        Movie movieAtPosition = mMoviesList.get(position);

        URL moviePosterFullUrl = NetworkUtils.buildImageUrl(movieAtPosition.getPosterPath());
        Picasso.get().load(moviePosterFullUrl.toString())
                .fit()
                .into(movieViewHolder.mMoviePosterIV);

        //movieViewHolder.mMovieTitleTV.setText(movieAtPosition.getTitle());
    }

    @Override
    public int getItemCount() {
        if (mMoviesList == null)
            return 0;
        return mMoviesList.size();
    }

    /* View Holder Class */

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        //private TextView mMovieTitleTV;
        private ImageView mMoviePosterIV;

        /* Constructor of View Holder */
        public MovieViewHolder(@NonNull View rootView) {
            super(rootView);

            //mMovieTitleTV = rootView.findViewById(R.id.movie_title_text_view);
            mMoviePosterIV = rootView.findViewById(R.id.movie_poster_image_view);

            rootView.setOnClickListener(this);
            rootView.setOnLongClickListener(this);
        }

        /* View Holder Overridden Methods */
        @Override
        public void onClick(View view) {
            Movie movie = mMoviesList.get(getAdapterPosition());
            mClickHandler.onClick(movie);
        }

        @Override
        public boolean onLongClick(View v) {
            Movie movie = mMoviesList.get(getAdapterPosition());
            mClickHandler.onLongClick(movie);
            return true;
        }
    }
}
