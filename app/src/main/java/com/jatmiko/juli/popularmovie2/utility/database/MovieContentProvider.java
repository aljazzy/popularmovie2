package com.jatmiko.juli.popularmovie2.utility.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.jatmiko.juli.popularmovie2.utility.database.Contract.MovieEntry.TABLE_NAME;

public class MovieContentProvider extends ContentProvider {
    public static final int movis = 100;
    public static final int movi_with_id = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DbHelper mDbHelper;

    private static final UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(Contract.AUTHORITY, Contract.PATH_MOVIE, movis);
        uriMatcher.addURI(Contract.AUTHORITY, Contract.PATH_MOVIE + "/#", movi_with_id);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mDbHelper = new DbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);

        Cursor returnCursor;
        switch (match) {
            case movis:
                returnCursor = db.query(TABLE_NAME, strings, s, strings1, null, null, s1);
                break;
            case movi_with_id:
                String mvId = uri.getPathSegments().get(1);
                String mSelect = "-id?";
                String[] s2 = new String[]{mvId};
                returnCursor = db.query(TABLE_NAME, strings, mSelect, s2, null, null, s1);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri : " + uri);
        }
        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not Implemented!");

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case movis:
                long id = db.insert(TABLE_NAME, null, contentValues);

                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(Contract.MovieEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed insert Row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri :" + uri);
        }
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int taskDeleted;

        switch (match) {
            case movi_with_id:
                String id = uri.getPathSegments().get(1);
                taskDeleted = db.delete(TABLE_NAME,
                        "_id=?",
                        new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (taskDeleted != 0) {
            if (getContext() != null) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
        }

        return taskDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        throw new UnsupportedOperationException("Not Implemented!");
    }
}