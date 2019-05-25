package com.muilat.android.wazo.data;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.preference.PreferenceManager;

import com.muilat.android.wazo.R;
import com.muilat.android.wazo.utils.Utils;

public class Word implements Parcelable {

    private int mID;
    private int mCategoryID;
    public String mEnglish;
    public String mHausa;
    public String mYoruba;
    private String mIsFavourite;
    private String displayName;

    public Word(Cursor data) {
        mID = WazoDbHelper.getColumnInt(data, WordContract.WordEntry._ID);
        mCategoryID = WazoDbHelper.getColumnInt(data, WordContract.WordEntry.COLUMN_CATEGORY_ID);
        mEnglish = WazoDbHelper.getColumnString(data, WordContract.WordEntry.COLUMN_ENGLISH);
        mHausa = WazoDbHelper.getColumnString(data, WordContract.WordEntry.COLUMN_HAUSA);
        mYoruba = WazoDbHelper.getColumnString(data, WordContract.WordEntry.COLUMN_YORUBA);
        mIsFavourite = WazoDbHelper.getColumnString(data, WordContract.WordEntry.COLUMN_IS_FAVOURITE);

    }

    public Word(Word word){
        mID = word.mID;
        mCategoryID = word.mCategoryID;
        mEnglish = word.mEnglish;
        mHausa = word.mHausa;
        mYoruba = word.mYoruba;
        mIsFavourite = word.mIsFavourite;
    }

    protected Word(Parcel in) {
        mID = in.readInt();
        mCategoryID = in.readInt();
        mEnglish = in.readString();
        mHausa = in.readString();
        mYoruba = in.readString();
    }

    public static final Creator<Word> CREATOR = new Creator<Word>() {
        @Override
        public Word createFromParcel(Parcel in) {
            return new Word(in);
        }

        @Override
        public Word[] newArray(int size) {
            return new Word[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public int getID() {
        return mID;
    }

    public int getCategoryID() {
        return mCategoryID;
    }

    public String getEnglish() {
        return mEnglish;
    }

    public String getHausa() {
        return mHausa;
    }

    public String getYoruba() {
        return mYoruba;
    }

    public boolean isFavourite(){
        if (mIsFavourite.equals("0"))

            return false;
        else
            return true;
    }

    public void setIsFavourite(String mIsFavourite) {
        this.mIsFavourite = mIsFavourite;
    }


    public String getDisplayName(Context context){
        displayName = Utils.getDisplayName(context, this);
        return displayName;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mID);
        parcel.writeInt(mCategoryID);
        parcel.writeString(mEnglish);
        parcel.writeString(mHausa);
        parcel.writeString(mYoruba);
    }
}
