package com.muilat.android.wazo.data;

import android.net.Uri;
import android.provider.BaseColumns;

import static android.provider.BaseColumns._ID;
import static com.muilat.android.wazo.data.CategoryContract.CategoryEntry.COLUMN_HAUSA;
import static com.muilat.android.wazo.data.CategoryContract.CategoryEntry.COLUMN_IMAGE_URL;
import static com.muilat.android.wazo.data.CategoryContract.CategoryEntry.COLUMN_NAME;
import static com.muilat.android.wazo.data.CategoryContract.CategoryEntry.TABLE_NAME;

public class CategoryContract {
    
        public static final String AUTHORITY = "com.muilat.android.wazo";
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
        public static final String PATH_CATEGORIES = "categories";
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CATEGORIES).build();


        public static final class CategoryEntry implements BaseColumns {
            public static final String TABLE_NAME = "categories";

            public static final String COLUMN_NAME = "name";
            public static final String COLUMN_HAUSA = "hausa";
            public static final String COLUMN_IMAGE_URL = "image_url";
        }

        final static String CREATE_CATEGORIES_TABLE = "CREATE TABLE "  + TABLE_NAME + " (" +
                _ID+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                COLUMN_HAUSA + " TEXT NOT NULL,"+
                COLUMN_IMAGE_URL + " TEXT NOT NULL,"+
                COLUMN_NAME + " TEXT NOT NULL);";
}