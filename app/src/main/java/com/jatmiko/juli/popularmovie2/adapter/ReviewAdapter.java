package com.jatmiko.juli.popularmovie2.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jatmiko.juli.popularmovie2.R;
import com.jatmiko.juli.popularmovie2.model.Review;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Miko on 18/08/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private List<Review> mReview;

    public ReviewAdapter() {
        mReview = new ArrayList<>();
    }

    public List<Review> getData() {
        return this.mReview;
    }

    public void setData(List<Review> movis) {
        this.mReview.clear();
        this.mReview.addAll(movis);
        notifyDataSetChanged();
    }

    @Override
    public ReviewAdapter.ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_review, parent, false);
        return new ReviewViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ReviewViewHolder holder, int position) {
        holder.reviewAuthor.setText(mReview.get(holder.getAdapterPosition()).getAuthor());
        holder.reviewContent.setText(mReview.get(holder.getAdapterPosition()).getContent());
    }

    @Override
    public int getItemCount() {
        return mReview.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.adapter_reviews_author)
        TextView reviewAuthor;

        @BindView(R.id.adapter_reviews_content)
        TextView reviewContent;

        ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
