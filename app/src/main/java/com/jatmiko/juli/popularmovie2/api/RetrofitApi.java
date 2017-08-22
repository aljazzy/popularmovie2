package com.jatmiko.juli.popularmovie2.api;

import com.jatmiko.juli.popularmovie2.BuildConfig;
import com.jatmiko.juli.popularmovie2.model.MovieResponse;
import com.jatmiko.juli.popularmovie2.model.ReviewResponse;
import com.jatmiko.juli.popularmovie2.model.VideoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Miko on 10/08/2017.
 */

public interface RetrofitApi {
    String BASE_URL = "https://api.themoviedb.org/3/";
    String BASE_URL_IMAGE = "https://image.tmdb.org/t/p/w185";
    String API_KEY = BuildConfig.MOVIE_API_KEY;
    String LANG_SOURCE = "en-US";
    String MOVIES_REGION = "US";
    String BASE_URL_IMAGE_YOUTUBE = "http://img.youtube.com/vi/";
    String BASE_URL_IMAGE_YOUTUBE_THUMBNAIL = "/0.jpg";
    String BASE_URL_VIDEO_YOUTUBE = "https://youtube.com/watch";

    @GET("movie/{type}")
    Call<MovieResponse> getMovies(@Path("type") String type, @Query("api_key") String apiKey, @Query("language") String language, @Query("page") int page, @Query("region") String region);

    @GET("movie/{id}/videos")
    Call<VideoResponse> getTrailers(@Path("id") String movieId, @Query("api_key") String apiKey, @Query("language") String language);

    @GET("movie/{id}/reviews")
    Call<ReviewResponse> getReviews(@Path("id") String movieId, @Query("api_key") String apiKey, @Query("language") String language, @Query("page") int page);

}
