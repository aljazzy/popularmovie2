package com.jatmiko.juli.popularmovie2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.jatmiko.juli.popularmovie2.api.RetrofitApi;
import com.jatmiko.juli.popularmovie2.event.MovieErrorEvent;
import com.jatmiko.juli.popularmovie2.event.MovieEvent;
import com.jatmiko.juli.popularmovie2.event.MvReviewErrorEvent;
import com.jatmiko.juli.popularmovie2.event.MvReviewEvent;
import com.jatmiko.juli.popularmovie2.event.MvTrailerErrorEvent;
import com.jatmiko.juli.popularmovie2.event.MvTrailerEvent;
import com.jatmiko.juli.popularmovie2.model.Movie;
import com.jatmiko.juli.popularmovie2.model.MovieResponse;
import com.jatmiko.juli.popularmovie2.model.ReviewResponse;
import com.jatmiko.juli.popularmovie2.model.VideoResponse;
import com.jatmiko.juli.popularmovie2.utility.Constant;
import com.jatmiko.juli.popularmovie2.utility.database.Contract;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Miko on 10/08/2017.
 */

public class MovieController {
    private EventBus eventBus = MainApp.getInstance().getEventBus();
    private Constant.Data.MOVIE_LIST_TITLE movieListTitle;

    private void getMovies(int type, int page) {
        Call<MovieResponse> movieResponseCall = MainApp.getInstance().getApiService().getMovies(Constant.Data.MOVIE_LIST_TYPE[type], RetrofitApi.API_KEY, RetrofitApi.LANG_SOURCE, page, RetrofitApi.MOVIES_REGION);
        movieResponseCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.code() == 200) {
                    eventBus.post(new MovieEvent(response.message(), response.body().getResults(), movieListTitle));
                } else {
                    eventBus.post(new MovieErrorEvent(response.message()));
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                eventBus.post(new MovieErrorEvent(t.getMessage()));

            }
        });
    }


    public void getPopularMovies(int page) {
        movieListTitle = Constant.Data.MOVIE_LIST_TITLE.POPULAR;
        getMovies(0, page);
    }

    public void getTopRatedMovies(int page) {
        movieListTitle = Constant.Data.MOVIE_LIST_TITLE.TOP_RATED;
        getMovies(1, page);
    }


    public void getMovieTrailers(String mvId) {
        Call<VideoResponse> videoResponseCall = MainApp.getInstance().getApiService().getTrailers(mvId, RetrofitApi.API_KEY, RetrofitApi.LANG_SOURCE);
        videoResponseCall.enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                if (response.code() == 200) {
                    eventBus.post(new MvTrailerEvent(response.message(), response.body()));
                } else {
                    eventBus.post(new MvTrailerErrorEvent(response.message()));
                }
            }

            @Override
            public void onFailure(Call<VideoResponse> call, Throwable t) {
                eventBus.post(new MvTrailerErrorEvent(t.getMessage()));
            }
        });
    }

    private void getFavoriteMovies(Context context) {
        movieListTitle = Constant.Data.MOVIE_LIST_TITLE.FAVORITE;
        Cursor cursor = context.getContentResolver().query(Contract.MovieEntry.CONTENT_URI, null, null, null, null);

        List<Movie> mMovieFavorites = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Movie movie = new Movie();
                movie.setId(cursor.getString(cursor.getColumnIndex(Contract.MovieEntry.COLUMN_ID)));
                movie.setTitle(cursor.getString(cursor.getColumnIndex(Contract.MovieEntry.COLUMN_TITLE)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(Contract.MovieEntry.COLUMN_OVERVIEW)));
                movie.setVoteAverage(cursor.getDouble(cursor.getColumnIndex(Contract.MovieEntry.COLUMN_VOTE_AVERAGE)));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(Contract.MovieEntry.COLUMN_RELEASE_DATE)));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndex(Contract.MovieEntry.COLUMN_POSTER_PATH)));
                mMovieFavorites.add(movie);
            }
            cursor.close();
        }

        eventBus.post(new MovieEvent(Constant.Data.FAVORITE_SUCCESS_MESSAGE, mMovieFavorites, movieListTitle));
    }

    public boolean addFavoriteMovie(Context context, Movie movie) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(Contract.MovieEntry.COLUMN_ID, movie.getId());
        contentValues.put(Contract.MovieEntry.COLUMN_TITLE, movie.getTitle());
        contentValues.put(Contract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        contentValues.put(Contract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        contentValues.put(Contract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        contentValues.put(Contract.MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());

        Uri uri = context.getContentResolver().insert(Contract.MovieEntry.CONTENT_URI, contentValues);

        getFavoriteMovies(context);

        return (uri != null);
    }


    public boolean removeFavoriteMovie(Context context, Movie movie) {
        Cursor cursor = context.getContentResolver().query(Contract.MovieEntry.CONTENT_URI, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                if (cursor.getString(cursor.getColumnIndex(Contract.MovieEntry.COLUMN_ID)).equals(movie.getId())) {
                    String id = cursor.getString(cursor.getColumnIndex(Contract.MovieEntry._ID));
                    Uri uri = Contract.MovieEntry.CONTENT_URI;
                    uri = uri.buildUpon().appendPath(id).build();
                    int itemDeleted = context.getContentResolver().delete(uri, null, null);

                    getFavoriteMovies(context);
                    return (itemDeleted != 0);
                }
            }
            cursor.close();
        }
        return false;
    }

    public void getMovieReviews(String id, int reviewPage) {
        Call<ReviewResponse> rvResponseCall = MainApp.getInstance().getApiService().getReviews(id, RetrofitApi.API_KEY, RetrofitApi.LANG_SOURCE, reviewPage);
        rvResponseCall.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                if (response.code() == 200) {
                    eventBus.post(new MvReviewEvent(response.message(), response.body()));
                } else {
                    eventBus.post(new MvTrailerErrorEvent(response.message()));
                }
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                eventBus.post(new MvReviewErrorEvent(t.getMessage()));
            }
        });
    }

    public void getFavoritMovies(Context context) {
        movieListTitle = Constant.Data.MOVIE_LIST_TITLE.FAVORITE;
        Cursor cursor = context.getContentResolver().query(Contract.MovieEntry.CONTENT_URI, null, null, null, null);

        List<Movie> mvFavourites = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Movie movie = new Movie();
                movie.setId(cursor.getString(cursor.getColumnIndex(Contract.MovieEntry.COLUMN_ID)));
                movie.setTitle(cursor.getString(cursor.getColumnIndex(Contract.MovieEntry.COLUMN_TITLE)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(Contract.MovieEntry.COLUMN_OVERVIEW)));
                movie.setVoteAverage(cursor.getDouble(cursor.getColumnIndex(Contract.MovieEntry.COLUMN_VOTE_AVERAGE)));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(Contract.MovieEntry.COLUMN_RELEASE_DATE)));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndex(Contract.MovieEntry.COLUMN_POSTER_PATH)));
                mvFavourites.add(movie);
            }
            cursor.close();
        }

        eventBus.post(new MovieEvent(Constant.Data.FAVORITE_SUCCESS_MESSAGE, mvFavourites, movieListTitle));
    }

    public boolean isFavoritMovie(Context c, Movie m) {
        Cursor cursor = c.getContentResolver().query(Contract.MovieEntry.CONTENT_URI, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                if (cursor.getString(cursor.getColumnIndex(Contract.MovieEntry.COLUMN_ID)).equals(m.getId())) {
                    return true;
                }
            }
            cursor.close();
            ;
        }
        return false;
    }
}
