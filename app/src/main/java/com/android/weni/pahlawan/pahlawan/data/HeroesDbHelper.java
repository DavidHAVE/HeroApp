package com.android.weni.pahlawan.pahlawan.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by David on 15/11/2017.
 */

public class HeroesDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = HeroesDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "heroes.db";

    private static final int DATABASE_VERSION = 1;

    public HeroesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_HEROES_TABLE = "CREATE TABLE " + HeroesContract.HeroesEntry.TABLE_NAME + " ("
                + HeroesContract.HeroesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + HeroesContract.HeroesEntry.COLUMN_HEROES_NAME + " INTEGER NOT NULL, "
                + HeroesContract.HeroesEntry.COLUMN_HEROES_DESCRIPTION + " TEXT NOT NULL, "
                + HeroesContract.HeroesEntry.COLUMN_HEROES_IMAGE + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_HEROES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String SQL_DROP_HEROES = "DROP TABLE IF EXISTS " + HeroesContract.HeroesEntry.TABLE_NAME;
        db.execSQL(SQL_DROP_HEROES);
        onCreate(db);
    }
}
