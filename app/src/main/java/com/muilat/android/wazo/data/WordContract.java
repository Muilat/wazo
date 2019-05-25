package com.muilat.android.wazo.data;


import android.net.Uri;
import android.provider.BaseColumns;

import static android.provider.BaseColumns._ID;
import static com.muilat.android.wazo.data.WordContract.WordEntry.COLUMN_CATEGORY_ID;
import static com.muilat.android.wazo.data.WordContract.WordEntry.COLUMN_ENGLISH;
import static com.muilat.android.wazo.data.WordContract.WordEntry.COLUMN_HAUSA;
import static com.muilat.android.wazo.data.WordContract.WordEntry.COLUMN_IS_FAVOURITE;
import static com.muilat.android.wazo.data.WordContract.WordEntry.COLUMN_YORUBA;

public class WordContract {
    public static final String AUTHORITY = "com.muilat.android.wazo";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_WORDS = "words";
    public final static String PATH_FAVOURITES = "favourites";

    public static final Uri CONTENT_URI =
            BASE_CONTENT_URI.buildUpon().appendPath(PATH_WORDS).build();
    public static final Uri FAVOURITE_CONTENT_URI =
            BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVOURITES).build();


//        public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
//                .authority(CONTENT_AUTHORITY)
//                .appendPath(TABLE_NAME)
//                .build();

    public static final class WordEntry implements BaseColumns {
        public static final String TABLE_NAME = "words";

        public static final String COLUMN_ENGLISH = "english_name";
        public static final String COLUMN_HAUSA = "hausa_name";
        public static final String COLUMN_YORUBA = "yoruba_name";
        public static final String COLUMN_CATEGORY_ID = "category_id";
        public static final String COLUMN_IS_FAVOURITE = "is_favourite";
    }

    final static String CREATE_WORDS_TABLE = "CREATE TABLE "  + WordContract.WordEntry.TABLE_NAME + " (" +
            _ID+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            COLUMN_CATEGORY_ID+ " INTEGER NOT NULL, " +
            COLUMN_ENGLISH + " TEXT NOT NULL,"+
            COLUMN_HAUSA + " TEXT NOT NULL,"+
            COLUMN_IS_FAVOURITE + " INTEGER NOT NULL DEFAULT 0, "+
            COLUMN_YORUBA + " TEXT NOT NULL);";
}