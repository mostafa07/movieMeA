package com.example.android.moviemea.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.moviemea.R;
import com.example.android.moviemea.models.Movie;
import com.example.android.moviemea.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

//    private Context mContext;
    private List<Movie> mMoviesList;
    private final MovieAdapterOnClickHandler mClickHandler;

    /* Constructor of Adapter */

    public MovieAdapter(MovieAdapterOnClickHandler clickHandler) {
//        mContext = context;
        mClickHandler = clickHandler;
    }

    /* On Click Handler */

    public interface MovieAdapterOnClickHandler {
        public void onClick(Movie movie);
    }


    /* Setter Function to Update Movies Data */

    public void setMoviesData(List<Movie> moviesList) {
        mMoviesList = moviesList;
        notifyDataSetChanged();
    }


    /* Adapter Overridden Methods */

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_grid_item, viewGroup, false);
        MovieViewHolder viewHolder = new MovieViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int position) {

        Movie movieAtPosition = mMoviesList.get(position);

        movieViewHolder.mMovieTitleTV.setText(movieAtPosition.toString());

        URL moviePosterFullUrl = NetworkUtils.buildImageUrl(movieAtPosition.getPosterPath());
        Picasso.get().load(moviePosterFullUrl.toString()).into(movieViewHolder.mMoviePosterIV);
    }

    @Override
    public int getItemCount() {
        if (mMoviesList == null)
            return 0;
        return mMoviesList.size();
    }


    /* View Holder Class */

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mMovieTitleTV;
        private ImageView mMoviePosterIV;

        /* Constructor of View Holder */
        public MovieViewHolder(@NonNull View rootView) {
            super(rootView);

            mMovieTitleTV = rootView.findViewById(R.id.movie_title_text_view);
            mMoviePosterIV = rootView.findViewById(R.id.movie_poster_image_view);

            rootView.setOnClickListener(this);
        }

        /* View Holder Overridden Methods */
        @Override
        public void onClick(View view) {

            Movie movie = mMoviesList.get(getAdapterPosition());
            mClickHandler.onClick(movie);
        }
    }
}
