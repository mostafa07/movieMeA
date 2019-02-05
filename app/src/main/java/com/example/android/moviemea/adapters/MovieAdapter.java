package com.example.android.moviemea.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.moviemea.R;
import com.example.android.moviemea.models.MovieDetail;

import java.util.List;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    private List<MovieDetail> mMoviesList;

    /* Constructor of Adapter */
    public MovieAdapter(List<MovieDetail> moviesList) {
        mMoviesList = moviesList;
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

        MovieDetail movieAtPosition = mMoviesList.get(position);

        movieViewHolder.mMovieTitleTV.setText(movieAtPosition.getTitle());
    }

    @Override
    public int getItemCount() {
        return mMoviesList.size();
    }


    /* View Holder Class */
    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mMovieTitleTV;

        /* Constructor of View Holder */
        public MovieViewHolder(@NonNull View rootView) {
            super(rootView);

            mMovieTitleTV = rootView.findViewById(R.id.tv_movie_title);
        }

        /* View Holder Overridden Methods */
        @Override
        public void onClick(View view) {
        }
    }
}
