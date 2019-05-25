package com.muilat.android.wazo.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.preference.PreferenceManager;

import com.muilat.android.wazo.R;

public class Categories {
    private int mID;
    private String mName;
    private String mHausa;
    private String mImageUrl;
    private String displayName;

    public Categories(int mID, String mName, String hausa, String mImageUrl) {
        this.mID = mID;
        this.mName = mName;
        this.mHausa = hausa;
        this.mImageUrl = mImageUrl;
    }

    public Categories(Cursor data) {
        mID = WazoDbHelper.getColumnInt(data, CategoryContract.CategoryEntry._ID);
        mName = WazoDbHelper.getColumnString(data, CategoryContract.CategoryEntry.COLUMN_NAME);
        mHausa = WazoDbHelper.getColumnString(data, CategoryContract.CategoryEntry.COLUMN_HAUSA);
        mImageUrl = WazoDbHelper.getColumnString(data, CategoryContract.CategoryEntry.COLUMN_IMAGE_URL);
    }

    public int getId() {
        return mID;
    }

    public String getName() {
        return mName;
    }

    public String getHausa() {
        return mHausa;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getDisplayName(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String lang_pref = sharedPreferences.getString(context.getString(R.string.pref_language_key),
                context.getResources().getString(R.string.pref_lang_hau_yor_value));

        if(lang_pref.equals(context.getResources().getString(R.string.pref_lang_hau_yor_value))){

            displayName = mHausa;

        }else {
            displayName = mName;
        }

        return displayName;
    }

    @Override
    public String toString() {
        return this.displayName;// What to display in the Spinner list.
    }

}
