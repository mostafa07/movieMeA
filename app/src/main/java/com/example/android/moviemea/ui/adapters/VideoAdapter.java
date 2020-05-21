package com.example.android.moviemea.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.moviemea.R;
import com.example.android.moviemea.models.remote.Video;
import com.example.android.moviemea.utilities.AppConstants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private static final String LOG_TAG = VideoAdapter.class.getSimpleName();

    private List<Video> mVideosList;
    private VideoClickHandler mClickHandler;

    /* Constructor */
    public VideoAdapter(VideoClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    /* On Click Handler */
    public interface VideoClickHandler {

        void onClick(Video video);
    }

    /* Helper Method to Update Videos Data */
    public void setVideosData(List<Video> videosList) {
        mVideosList = new ArrayList<>(videosList);
        notifyDataSetChanged();
    }

    /* Helper Method to Clear Videos Data */
    public void clearVideosData() {
        if (mVideosList != null)
            mVideosList.clear();
        notifyDataSetChanged();
    }

    /* Adapter Overridden Methods */

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_list_video, parent, false);
        return new VideoViewHolder(viewDataBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mVideosList != null ? mVideosList.size() : 0;
    }

    /* View Holder Class */

    class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ViewDataBinding mViewDataBinding;

        /* Constructor of View Holder */
        public VideoViewHolder(ViewDataBinding viewDataBinding) {
            super(viewDataBinding.getRoot());
            mViewDataBinding = viewDataBinding;

            viewDataBinding.getRoot().setOnClickListener(this);
        }

        public void bind(int position) {
            final Video video = mVideosList.get(position);

            final String youtubeVideoThumbnailUrl = AppConstants.IMG_YOUTUBE_BASE_URL + video.getKey()
                    + AppConstants.IMG_YOUTUBE_DEFAULT_VIDEO_THUMBNAIL;
            final ImageView videoThumbnailImageView = mViewDataBinding.getRoot().findViewById(R.id.video_thumbnail_image_view);
            Picasso.get().load(youtubeVideoThumbnailUrl)
                    .fit()
                    .centerInside()
                    .into(videoThumbnailImageView);

            final TextView videoNameTextView = mViewDataBinding.getRoot().findViewById(R.id.video_name_text_view);
            videoNameTextView.setText(video.getName());

            mViewDataBinding.executePendingBindings();
        }

        @Override
        public void onClick(View v) {
            Video video = mVideosList.get(getAdapterPosition());
            mClickHandler.onClick(video);
        }
    }
}
