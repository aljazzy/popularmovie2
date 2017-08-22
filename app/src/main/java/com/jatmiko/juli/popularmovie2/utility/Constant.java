package com.jatmiko.juli.popularmovie2.utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jatmiko.juli.popularmovie2.api.RetrofitApi;

/**
 * Created by Miko on 10/08/2017.
 */

public class Constant {
    public static class Data {
        public static final String[] MOVIE_LIST_TYPE = new String[]{"popular", "top_rated"};
        public static final String MOVIE_INTENT = "movie";
        public static final String MOVIE_RELEASE_DATE_FORMAT_AFTER = "MMMM dd, yyyy";
        public static final String MOVIE_RELEASE_DATE_FORMAT_BEFORE = "yyyy-MM-dd";
        public static final int MOVIE_IMAGE_LIST_WIDTH = 200;
        public static final String EXTRA_MOVIE_LIST = "movie-list";
        public static final String EXTRA_PAGE = "page";
        public static final String EXTRA_TITLE = "title";
        public static final String FAVORITE_SUCCESS_MESSAGE = "Success Favorited";

        public enum MOVIE_LIST_TITLE {
            POPULAR, TOP_RATED, FAVORITE;
        }
    }

    public static class Function {

        public static int getColumnsCount(Activity activity) {
            Display display = activity.getWindowManager().getDefaultDisplay();
            DisplayMetrics outMetrics = new DisplayMetrics();
            display.getMetrics(outMetrics);

            float density = activity.getResources().getDisplayMetrics().density;
            float dpWidth = outMetrics.widthPixels / density;
            return Math.round(dpWidth / Data.MOVIE_IMAGE_LIST_WIDTH);
        }


        public static void setImageResource(Context context, String urlImage, ImageView imageView) {
            Glide.with(context)
                    .load(urlImage)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE).dontAnimate().centerCrop())
                    .into(imageView);
        }

        public static void showingVideos(Context context, String videoUrl) {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
            context.startActivity(i);
        }

        public static String getYoutubeUrl(String key) {
            return RetrofitApi.BASE_URL_VIDEO_YOUTUBE + "?v=" + key;
        }

        public static String getYoutubeThumbnailUrl(String key) {
            return RetrofitApi.BASE_URL_IMAGE_YOUTUBE + key + RetrofitApi.BASE_URL_IMAGE_YOUTUBE_THUMBNAIL;
        }
    }
}
