package com.android.weni.pahlawan.pahlawan.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by David on 15/11/2017.
 */

public class HeroesContract {

    public static final String CONTENT_AUTHORITY = "com.android.weni.pahlawan.pahlawan";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_HEROES = "heroes";

    public static class HeroesEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_HEROES);


        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of pets.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HEROES;
        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HEROES;

        public final static String TABLE_NAME = "heroes";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_HEROES_NAME = "name";
        public static final String COLUMN_HEROES_DESCRIPTION = "description";
        public static final String COLUMN_HEROES_IMAGE = "imageUrl";
    }
}
