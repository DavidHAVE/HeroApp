package com.android.weni.pahlawan.pahlawan.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by David on 15/11/2017.
 */

public class HeroesProvider extends ContentProvider {

    public static final String LOG_TAG = HeroesProvider.class.getSimpleName();
    /**
     * URI matcher code for the content URI for the pets table
     */
    private static final int HEROES = 100;
    /**
     * URI matcher code for the content URI for a single pet in the pets table
     */
    private static final int HEROES_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(HeroesContract.CONTENT_AUTHORITY, HeroesContract.PATH_HEROES, HEROES);

        sUriMatcher.addURI(HeroesContract.CONTENT_AUTHORITY, HeroesContract.PATH_HEROES + "/#", HEROES_ID);
    }

    private HeroesDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new HeroesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor = null;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case HEROES:
                cursor = database.query(HeroesContract.HeroesEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case HEROES_ID:
                selection = HeroesContract.HeroesEntry._ID + "=?";
                selectionArgs = new String[]{
                        String.valueOf(ContentUris.parseId(uri))
                };

                cursor = database.query(HeroesContract.HeroesEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknows " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case HEROES:
                return HeroesContract.HeroesEntry.CONTENT_LIST_TYPE;
            case HEROES_ID:
                return HeroesContract.HeroesEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case HEROES:
                return insertHeroes(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertHeroes(Uri uri, ContentValues contentValues) {
        String name = contentValues.getAsString(HeroesContract.HeroesEntry.COLUMN_HEROES_NAME);

        if (name == null) {
            throw new IllegalArgumentException("Pet requires a name");
        }

        // TODO: Insert a new pet into the pets database table with the given ContentValues
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(HeroesContract.HeroesEntry.TABLE_NAME, null, contentValues);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, id);
    }

    //insertHelperMethod

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        if (uri == HeroesContract.HeroesEntry.CONTENT_URI) {
            database.beginTransaction();
            SQLiteStatement stmt = database.compileStatement(
                    "INSERT INTO " + HeroesContract.HeroesEntry.TABLE_NAME + "(" + HeroesContract.HeroesEntry.COLUMN_HEROES_NAME + ", "
                            + HeroesContract.HeroesEntry.COLUMN_HEROES_DESCRIPTION + ", " + HeroesContract.HeroesEntry.COLUMN_HEROES_IMAGE + ") VALUES " +
                            "(?, ?, ?);");

            int length = values.length;
            for (int i = 0; i < length; i++) {
                stmt.bindString(1, values[i].getAsString(HeroesContract.HeroesEntry.COLUMN_HEROES_NAME));
                stmt.bindString(2, values[i].getAsString(HeroesContract.HeroesEntry.COLUMN_HEROES_DESCRIPTION));
                stmt.bindString(3, values[i].getAsString(HeroesContract.HeroesEntry.COLUMN_HEROES_IMAGE));
            }
            database.setTransactionSuccessful();
            database.endTransaction();
        } else {

        }
//        getContext().getContentResolver().notifyChange(uri, null);
        return super.bulkInsert(uri, values);
    }


    /**
     * Insert a pet into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsDeleted;
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case HEROES:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(HeroesContract.HeroesEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            case HEROES_ID:
                // Delete a single row given by the ID in the URI
                selection = HeroesContract.HeroesEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(HeroesContract.HeroesEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
