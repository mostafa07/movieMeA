package com.example.android.moviemea.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.moviemea.R;
import com.example.android.moviemea.models.Review;

import java.util.ArrayList;
import java.util.List;


public class UserReviewAdapter extends RecyclerView.Adapter<UserReviewAdapter.UserReviewViewHolder> {

    private static final String LOG_TAG = UserReviewAdapter.class.getSimpleName();

    private List<Review> mReviewsList;

    /* Helper Method to Update Reviews Data */
    public void setReviewsData(List<Review> reviewsList) {
        mReviewsList = new ArrayList<>(reviewsList);
        notifyDataSetChanged();
    }

    /* Helper Method to Clear Reviews Data */
    public void clearReviewsData() {
        if (mReviewsList != null)
            mReviewsList.clear();
        notifyDataSetChanged();
    }

    /* Adapter Overridden Methods */

    @NonNull
    @Override
    public UserReviewAdapter.UserReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_review_list_item,
                parent, false);
        return new UserReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserReviewAdapter.UserReviewViewHolder holder, int position) {
        Review review = mReviewsList.get(position);

        holder.reviewContentTV.setText(review.getContent());
        holder.authorTV.setText(review.getAuthor());
    }

    @Override
    public int getItemCount() {
        if (mReviewsList == null)
            return 0;
        return mReviewsList.size();
    }

    /* View Holder Class */

    class UserReviewViewHolder extends RecyclerView.ViewHolder {

        private TextView reviewContentTV;
        private TextView authorTV;

        /* Constructor of View Holder */
        public UserReviewViewHolder(@NonNull View rootView) {
            super(rootView);

            reviewContentTV = itemView.findViewById(R.id.user_review_content_text_view);
            authorTV = itemView.findViewById(R.id.user_review_author_text_view);
        }
    }
}
