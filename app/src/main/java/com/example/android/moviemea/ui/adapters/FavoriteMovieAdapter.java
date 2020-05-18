package com.example.android.moviemea.ui.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.moviemea.R;
import com.example.android.moviemea.models.local.FavoriteMovie;
import com.example.android.moviemea.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class FavoriteMovieAdapter extends RecyclerView.Adapter<FavoriteMovieAdapter.FavoriteMovieViewHolder> {

    private static final String LOG_TAG = FavoriteMovieAdapter.class.getSimpleName();

    private List<FavoriteMovie> mFavoriteMovies;
    private final FavoriteMovieOnClickHandler mClickHandler;

    public FavoriteMovieAdapter(FavoriteMovieOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }


    public interface FavoriteMovieOnClickHandler {

        void onClick(FavoriteMovie favoriteMovie);

        void onLongClick(FavoriteMovie favoriteMovie);
    }


    public List<FavoriteMovie> getFavoriteMoviesData() {
        return mFavoriteMovies;
    }

    public void setFavoriteMoviesData(List<FavoriteMovie> favoriteMovies) {
        mFavoriteMovies = new ArrayList<>(favoriteMovies);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoriteMovieAdapter.FavoriteMovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_list_favorite_movie, viewGroup, false);
        return new FavoriteMovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteMovieAdapter.FavoriteMovieViewHolder favoriteMovieViewHolder, int position) {
        FavoriteMovie favoriteMovie = mFavoriteMovies.get(position);

        // TODO get images from local database (for later)
        URL moviePosterFullUrl = NetworkUtils.buildImageUrl(favoriteMovie.getPosterPath());
        Picasso.get().load(moviePosterFullUrl.toString())
                // TODO resize or crop
                .into(favoriteMovieViewHolder.moviePosterImageView);
        favoriteMovieViewHolder.movieTitleTextView.setText(favoriteMovie.getTitle());
        favoriteMovieViewHolder.movieReleaseDateTextView.setText(favoriteMovie.getReleaseDate());
    }

    @Override
    public int getItemCount() {
        if (mFavoriteMovies == null)
            return 0;
        return mFavoriteMovies.size();
    }


    class FavoriteMovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        ImageView moviePosterImageView;
        TextView movieTitleTextView;
        TextView movieReleaseDateTextView;

        public FavoriteMovieViewHolder(@NonNull View rootView) {
            super(rootView);

            moviePosterImageView = rootView.findViewById(R.id.favorite_movie_poster_image_view);
            movieTitleTextView = rootView.findViewById(R.id.favorite_movie_title_text_view);
            movieReleaseDateTextView = rootView.findViewById(R.id.favorite_movie_release_date_text_view);
n
            rootView.setOnClickListener(this);
            rootView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            FavoriteMovie favoriteMovie = mFavoriteMovies.get(getAdapterPosition());
            mClickHandler.onClick(favoriteMovie);
        }

        @Override
        public boolean onLongClick(View v) {
            FavoriteMovie favoriteMovie = mFavoriteMovies.get(getAdapterPosition());
            mClickHandler.onLongClick(favoriteMovie);
            return true;
        }
    }
}
