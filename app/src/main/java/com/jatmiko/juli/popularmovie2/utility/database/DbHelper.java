package com.jatmiko.juli.popularmovie2.utility.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Miko on 18/08/2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movie.db";
    private static final int VERSION = 1;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE " + Contract.MovieEntry.TABLE_NAME + " (" +
                Contract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Contract.MovieEntry.COLUMN_ID + " TEXT NOT NULL, " +
                Contract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                Contract.MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                Contract.MovieEntry.COLUMN_VOTE_AVERAGE + " DOUBLE NOT NULL, " +
                Contract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL," +
                Contract.MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL);";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(" DROP TABLE IF EXIST " + Contract.MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
