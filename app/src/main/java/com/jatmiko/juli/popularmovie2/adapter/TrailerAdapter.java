package com.jatmiko.juli.popularmovie2.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jatmiko.juli.popularmovie2.R;
import com.jatmiko.juli.popularmovie2.model.Video;
import com.jatmiko.juli.popularmovie2.utility.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Miko on 18/08/2017.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailersViewHolder> {
    private List<Video> mVideos;

    public TrailerAdapter() {
        mVideos = new ArrayList<>();
    }

    public List<Video> getData() {
        return this.mVideos;
    }

    public void setData(List<Video> videos) {
        this.mVideos.clear();
        this.mVideos.addAll(videos);
        notifyDataSetChanged();
    }

    @Override
    public TrailersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_trailer, parent, false);
        return new TrailersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailersViewHolder holder, int position) {
        Constant.Function.setImageResource(holder.itemView.getContext(), Constant.Function.getYoutubeThumbnailUrl(mVideos.get(holder.getAdapterPosition()).getVideoKey()), holder.mTrailersImage);
        holder.mTrailersName.setText(mVideos.get(holder.getAdapterPosition()).getName());
        String videoDescription = mVideos.get(holder.getAdapterPosition()).getType() + " "
                + mVideos.get(holder.getAdapterPosition()).getLanguageCode() + "-"
                + mVideos.get(holder.getAdapterPosition()).getNationalCode() + " "
                + mVideos.get(holder.getAdapterPosition()).getSite();
        holder.mTrailersDescription.setText(videoDescription);
    }

    @Override
    public int getItemCount() {
        return mVideos.size();
    }

    class TrailersViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.adapter_trailers_layout)
        CardView mTrailersLayout;

        @BindView(R.id.adapter_trailers_image)
        ImageView mTrailersImage;

        @BindView(R.id.adapter_trailers_name)
        TextView mTrailersName;

        @BindView(R.id.adapter_trailers_description)
        TextView mTrailersDescription;

        TrailersViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            mTrailersLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mVideos.get(getAdapterPosition()).getSite().equals(v.getContext().getString(R.string.youtube_video))) {
                        Constant.Function.showingVideos(v.getContext(), Constant.Function.getYoutubeUrl(mVideos.get(getAdapterPosition()).getVideoKey()));
                    } else {
                        Toast.makeText(itemView.getContext(), v.getContext().getString(R.string.video_play_error), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
