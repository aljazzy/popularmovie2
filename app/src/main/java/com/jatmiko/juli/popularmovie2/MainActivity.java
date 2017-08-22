package com.jatmiko.juli.popularmovie2;

import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jatmiko.juli.popularmovie2.event.MovieErrorEvent;
import com.jatmiko.juli.popularmovie2.event.MovieEvent;
import com.jatmiko.juli.popularmovie2.model.Movie;
import com.jatmiko.juli.popularmovie2.utility.Constant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.rv_main)
    RecyclerView mMain;

    @BindView(R.id.loading_bar)
    ProgressBar loadingBar;

    @BindView(R.id.error_layout)
    LinearLayout errorLayout;

    private EventBus eventBus;
    private MovieAdapter adapter;
    private MovieController controller;
    private int page;
    private Constant.Data.MOVIE_LIST_TITLE movieListTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        controller = new MovieController();

        initView();

        if (savedInstanceState != null) {
            List<Movie> movieList = Arrays.asList(MainApp.getInstance().getGson().fromJson(savedInstanceState.getString(Constant.Data.EXTRA_MOVIE_LIST), Movie[].class));
            adapter.setData(movieList);

            MainActivity.this.setTitle(savedInstanceState.getString(Constant.Data.EXTRA_TITLE));
            page = savedInstanceState.getInt(Constant.Data.EXTRA_PAGE);
            return;
        }

        errorLayout.setVisibility(View.GONE);
        loadingBar.setVisibility(View.VISIBLE);

        page = 1;
        setPopularMovies(page);
    }

    private void initView() {
        ButterKnife.bind(this);

        int columns = Constant.Function.getColumnsCount(this);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, columns);
        mMain.setLayoutManager(layoutManager);
        mMain.setHasFixedSize(true);

        adapter = new MovieAdapter();
        mMain.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, "Exit", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_popular:
                errorLayout.setVisibility(View.GONE);
                adapter.setData(new ArrayList<Movie>());
                loadingBar.setVisibility(View.VISIBLE);
                page = 1;
                setPopularMovies(page);
                break;
            case R.id.action_top_rated:
                errorLayout.setVisibility(View.GONE);
                adapter.setData(new ArrayList<Movie>());
                loadingBar.setVisibility(View.VISIBLE);
                page = 1;
                setTopRatedMovies(page);
                break;
            case R.id.action_favorit:
                errorLayout.setVisibility(View.GONE);
                adapter.setData(new ArrayList<Movie>());
                loadingBar.setVisibility(View.VISIBLE);
                page = 1;

                setFavoriteMovies();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setFavoriteMovies() {
        MainActivity.this.setTitle("Favorit");

        this.movieListTitle = Constant.Data.MOVIE_LIST_TITLE.FAVORITE;
        controller.getFavoritMovies(this);
    }

    private void setTopRatedMovies(int page) {
        MainActivity.this.setTitle(getString(R.string.top_rated));

        this.page = page;
        this.movieListTitle = Constant.Data.MOVIE_LIST_TITLE.TOP_RATED;

        controller.getTopRatedMovies(page);
    }

    private void setPopularMovies(int page) {
        MainActivity.this.setTitle(getString(R.string.popular));

        this.page = page;
        this.movieListTitle = Constant.Data.MOVIE_LIST_TITLE.POPULAR;

        controller.getPopularMovies(page);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMovieList(MovieEvent event) {
        if (event.getMvListTitle().equals(movieListTitle))
            loadingBar.setVisibility(View.GONE);
        adapter.setData(event.getResults());
        mMain.scrollToPosition(0);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMovieListError(MovieErrorEvent event) {
        loadingBar.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);
        Log.e("errorResultData", event.getMessage());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        List<Movie> mvList = adapter.getData();
        String mvListJson = MainApp.getInstance().getGson().toJson(mvList);
        outState.putString(Constant.Data.EXTRA_MOVIE_LIST, mvListJson);
        outState.putInt(Constant.Data.EXTRA_PAGE, page);
        outState.putString(Constant.Data.EXTRA_TITLE, getTitle().toString());

    }
}
