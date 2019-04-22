package com.nbvarnado.popularmovies.ui.detail.review;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nbvarnado.popularmovies.R;
import com.nbvarnado.popularmovies.data.database.reviews.Review;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    List<Review> mReviews;

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View reviewView = LayoutInflater.from(parent.getContext()).inflate(R.layout.review, parent, false);
        return new ReviewViewHolder(reviewView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = mReviews.get(position);
        holder.mAuthorTextView.setText(review.getAuthor());
        holder.mContentTextView.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        if (mReviews == null) {
            return 0;
        }
        return mReviews.size();
    }

    public void setReviewData(List<Review> reviewData) { mReviews = reviewData; }

    class ReviewViewHolder extends RecyclerView.ViewHolder {

        final TextView mAuthorTextView;
        final TextView mContentTextView;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            mAuthorTextView = itemView.findViewById(R.id.tv_review_author);
            mContentTextView = itemView.findViewById(R.id.tv_review_content);
        }
    }
}
