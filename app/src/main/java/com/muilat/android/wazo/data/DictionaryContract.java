package com.muilat.android.wazo.data;


import android.net.Uri;
import android.provider.BaseColumns;

import static android.provider.BaseColumns._ID;
import static com.muilat.android.wazo.data.DictionaryContract.DictionaryEntry.COLUMN_CATEGORY_ID;
import static com.muilat.android.wazo.data.DictionaryContract.DictionaryEntry.COLUMN_ENGLISH;
import static com.muilat.android.wazo.data.DictionaryContract.DictionaryEntry.COLUMN_HAUSA;
import static com.muilat.android.wazo.data.DictionaryContract.DictionaryEntry.COLUMN_IS_FAVOURITE;
import static com.muilat.android.wazo.data.DictionaryContract.DictionaryEntry.COLUMN_YORUBA;

public class DictionaryContract {
    public static final String AUTHORITY = "com.muilat.android.wazo";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_DICTIONARIES = "dictionaries";

    public static final Uri CONTENT_URI =
            BASE_CONTENT_URI.buildUpon().appendPath(PATH_DICTIONARIES).build();


    public static final class DictionaryEntry implements BaseColumns {
        public static final String TABLE_NAME = "dictionaries";

        public static final String COLUMN_ENGLISH = "english_name";
        public static final String COLUMN_HAUSA = "hausa_name";
        public static final String COLUMN_YORUBA = "yoruba_name";
        public static final String COLUMN_CATEGORY_ID = "category_id";
        public static final String COLUMN_IS_FAVOURITE = "is_favourite";
    }

    final static String CREATE_DICTIONARIES_TABLE = "CREATE TABLE "  + DictionaryContract.DictionaryEntry.TABLE_NAME + " (" +
            _ID+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            COLUMN_CATEGORY_ID+ " INTEGER NOT NULL, " +
            COLUMN_ENGLISH + " TEXT NOT NULL,"+
            COLUMN_HAUSA + " TEXT NOT NULL,"+
            COLUMN_IS_FAVOURITE + " INTEGER NOT NULL DEFAULT 0, "+
            COLUMN_YORUBA + " TEXT NOT NULL);";
}