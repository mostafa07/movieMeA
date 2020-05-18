package com.example.android.moviemea.ui.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.moviemea.R;
import com.example.android.moviemea.databinding.FragmentUserReviewDialogBinding;
import com.example.android.moviemea.models.remote.Review;


public class UserReviewDialogFragment extends DialogFragment {

    private static final String ARG_USER_REVIEW = "user_review";

    private FragmentUserReviewDialogBinding mFragmentUserReviewDialogBinding;
    private Review mUserReview;

    public UserReviewDialogFragment() {
        // Required empty public constructor
    }

    public static UserReviewDialogFragment newInstance(Review userReview) {
        UserReviewDialogFragment fragment = new UserReviewDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_USER_REVIEW, userReview);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserReview = getArguments().getParcelable(ARG_USER_REVIEW);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentUserReviewDialogBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_review_dialog,
                container, false);

        mFragmentUserReviewDialogBinding.setUserReview(mUserReview);
        mFragmentUserReviewDialogBinding.userReviewFragmentContentTextView.setMovementMethod(new ScrollingMovementMethod());
        mFragmentUserReviewDialogBinding.userReviewCloseButton.setOnClickListener(view -> {
            dismiss();
        });

        return mFragmentUserReviewDialogBinding.getRoot();
    }
}
