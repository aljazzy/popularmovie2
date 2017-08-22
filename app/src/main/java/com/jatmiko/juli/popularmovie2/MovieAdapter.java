package com.jatmiko.juli.popularmovie2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jatmiko.juli.popularmovie2.api.RetrofitApi;
import com.jatmiko.juli.popularmovie2.model.Movie;
import com.jatmiko.juli.popularmovie2.utility.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Miko on 10/08/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private ArrayList<Movie> movis;

    public MovieAdapter() {
        movis = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.adapter_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Constant.Function.setImageResource(holder.itemView.getContext(), RetrofitApi.BASE_URL_IMAGE + movis.get(position).getPosterPath(), holder.movie_poster);
    }

    private void detailMovie(Context context, int position) {
        Intent i = new Intent(context, DetailMovieActivity.class);
        i.putExtra(Constant.Data.MOVIE_INTENT, MainApp.getInstance().getGson().toJson(movis.get(position)));
        context.startActivity(i);
    }

    @Override
    public int getItemCount() {
        return movis.size();
    }

    public List<Movie> getData() {
        return this.movis;
    }

    public void setData(List<Movie> movies) {
        this.movis.clear();
        this.movis.addAll(movies);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.adapter_movie_poster)
        ImageView movie_poster;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            movie_poster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    detailMovie(itemView.getContext(), getAdapterPosition());
                }
            });

        }
    }


}
