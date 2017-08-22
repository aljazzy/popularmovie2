package com.jatmiko.juli.popularmovie2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jatmiko.juli.popularmovie2.adapter.ReviewAdapter;
import com.jatmiko.juli.popularmovie2.adapter.TrailerAdapter;
import com.jatmiko.juli.popularmovie2.api.RetrofitApi;
import com.jatmiko.juli.popularmovie2.event.MvReviewErrorEvent;
import com.jatmiko.juli.popularmovie2.event.MvReviewEvent;
import com.jatmiko.juli.popularmovie2.event.MvTrailerErrorEvent;
import com.jatmiko.juli.popularmovie2.event.MvTrailerEvent;
import com.jatmiko.juli.popularmovie2.model.Movie;
import com.jatmiko.juli.popularmovie2.utility.Constant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailMovieActivity extends AppCompatActivity {
    @BindView(R.id.collapse_toolbar)
    CollapsingToolbarLayout mCollapse;

    @BindView(R.id.detail_poster)
    ImageView mDetailPoster;

//    @BindView(R.id.detail_title)
//    TextView mDetailTitle;

    @BindView(R.id.detail_overview)
    TextView mDetailOverview;

    @BindView(R.id.detail_rating)
    TextView mDetailRating;

    @BindView(R.id.detail_release)
    TextView mDetailRelease;

    @BindView(R.id.detail_overview_title)
    TextView mDetailOverviewTitle;

    @BindView(R.id.detail_release_title)
    TextView mDetailReleaseTitle;

    @BindView(R.id.rv_review_title)
    RecyclerView mReviewTitle;

    @BindView(R.id.rv_trailer)
    RecyclerView mReviewTrailer;

//    @BindView(R.id.toolbar)
//    Toolbar toolbar;

    private MovieController movieController;

    private EventBus eventBus;
    private int reviewPage = 1;

    private Movie movieDetail;

    private String TAG = this.getClass().getSimpleName();

    private ReviewAdapter reviewAdapter;

    private TrailerAdapter trailerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        movieDetail = MainApp.getInstance().getGson().fromJson(this.getIntent().getStringExtra(Constant.Data.MOVIE_INTENT), Movie.class);

        initView();

        setTitle(movieDetail.getTitle());

        Constant.Function.setImageResource(this, RetrofitApi.BASE_URL_IMAGE + movieDetail.getPosterPath(), mDetailPoster);

        mCollapse.setTitle(movieDetail.getTitle());

        //mDetailTitle.setText(movieDetail.getTitle());

        if (movieDetail.getOverview() != null && !movieDetail.getOverview().equals("")) {
            mDetailOverview.setText(movieDetail.getOverview());
            mDetailOverviewTitle.setVisibility(View.VISIBLE);
        } else {
            mDetailOverview.setText("");
            mDetailOverviewTitle.setVisibility(View.GONE);
        }

        String ratingStr = String.valueOf(movieDetail.getVoteAverage()) + "of 10";
        mDetailRating.setText(ratingStr);

        try {
            mDetailRelease.setText(new SimpleDateFormat(Constant.Data.MOVIE_RELEASE_DATE_FORMAT_AFTER,
                    Locale.getDefault()).format(new SimpleDateFormat(Constant.Data.MOVIE_RELEASE_DATE_FORMAT_BEFORE,
                    Locale.getDefault()).parse(movieDetail.getReleaseDate())));
            mDetailReleaseTitle.setVisibility(View.VISIBLE);
        } catch (ParseException e) {
            e.printStackTrace();
            mDetailRelease.setText("");
            mDetailReleaseTitle.setVisibility(View.GONE);
        }
        RecyclerView.LayoutManager rvLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mReviewTitle.setLayoutManager(rvLayoutManager);
        mReviewTitle.setHasFixedSize(true);

        reviewAdapter = new ReviewAdapter();
        mReviewTitle.setAdapter(reviewAdapter);

        ViewCompat.setNestedScrollingEnabled(mReviewTitle, false);

        RecyclerView.LayoutManager trailerLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mReviewTrailer.setLayoutManager(trailerLayoutManager);
        mReviewTrailer.setHasFixedSize(true);

        trailerAdapter = new TrailerAdapter();
        mReviewTrailer.setAdapter(trailerAdapter);

        ViewCompat.setNestedScrollingEnabled(mReviewTrailer, false);

        movieController = new MovieController();
        movieController.getMovieReviews(movieDetail.getId(), reviewPage);
    }

    @Override
    protected void onResume() {
        super.onResume();
        eventBus = MainApp.getInstance().getEventBus();
        eventBus.register(this);
    }

    @Override
    protected void onPause() {
        eventBus.unregister(this);
        super.onPause();
    }

    private void initView() {
        ButterKnife.bind(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMovieReviewList(MvReviewEvent event) {
        reviewAdapter.setData(event.getBody().getResults());
        movieController.getMovieTrailers(movieDetail.getId());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMovieReviewListError(MvReviewErrorEvent event) {
        Log.e(getString(R.string.log_error_data), event.getMessage());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMovieTrailerList(MvTrailerEvent event) {
        trailerAdapter.setData(event.getBody().getResults());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMovieTrailerListError(MvTrailerErrorEvent event) {
        Log.e(getString(R.string.log_error_data), event.getMessage());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        MenuItem item = menu.findItem(R.id.favorit);

        boolean isFavorit = movieController.isFavoritMovie(this, movieDetail);
        item.setIcon(ContextCompat.getDrawable(this, R.mipmap.fav_off));
        if (isFavorit) {
            item.setIcon(ContextCompat.getDrawable(this, R.mipmap.fav_on));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.home) {
            super.onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.favorit) {
            if (item.getIcon().getConstantState() == (ContextCompat.getDrawable(this, R.mipmap.fav_off).getConstantState())) {
                boolean success = movieController.addFavoriteMovie(this, movieDetail);
                if (success) {
                    Snackbar.make(mDetailPoster, "Done Favorited Movie", Snackbar.LENGTH_LONG).show();
                    item.setIcon(ContextCompat.getDrawable(this, R.mipmap.fav_on));
                } else {
                    Snackbar.make(mDetailPoster, "Failed", Snackbar.LENGTH_LONG).show();
                }
            } else {
                boolean notSuccess = movieController.removeFavoriteMovie(this, movieDetail);
                if (notSuccess) {
                    Snackbar.make(mDetailPoster, "Unfavorit", Snackbar.LENGTH_LONG).show();
                    item.setIcon(ContextCompat.getDrawable(this, R.mipmap.fav_off));
                } else {
                    Snackbar.make(mDetailPoster, "Failed", Snackbar.LENGTH_LONG).show();
                }
            }
            return true;
        } else if (item.getItemId() == R.id.share) {
            Intent i = new Intent();
            i.setAction(Intent.ACTION_SEND);
            i.putExtra(Intent.EXTRA_TEXT, getShareContent());
            i.setType("text/plain");
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String getShareContent() {
        String content = "Popular Movie" + "\n";
        content += movieDetail.getTitle() + " ";
        content += Constant.Function.getYoutubeUrl(trailerAdapter.getData().get(0).getVideoKey()) + "\n";
        content += " Watch Now";
        return content;
    }
}
