package com.jatmiko.juli.popularmovie2.utility.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Miko on 18/08/2017.
 */

public class Contract {
    public static final String AUTHORITY = "com.jatmiko.juli.popularmovie2";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_MOVIE = "movie";

    public static class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();
        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_POSTER_PATH = "poster_path";
    }
}
