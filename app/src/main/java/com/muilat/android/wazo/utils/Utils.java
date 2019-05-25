package com.muilat.android.wazo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.muilat.android.wazo.R;
import com.muilat.android.wazo.data.Word;

public class Utils {

    public static final String PREF_LANG = "pref-language";
    public static final int ENGLISH_YORUBA = 1;
    public static final int HAUSA_YORUBA = 2;



    public static String getDisplayName(Context context, Word word){
        String displayName;

        int pref = getLangPref(context);
        switch (pref){
            case ENGLISH_YORUBA:
                return word.mEnglish;
            case HAUSA_YORUBA:
                return word.mHausa;
            default:
                return "";

        }

    }


    public static int getLangPref(Context context){

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String lang_pref = sharedPreferences.getString(context.getString(R.string.pref_language_key),
                context.getResources().getString(R.string.pref_lang_hau_yor_value));

        if(lang_pref.equals(context.getResources().getString(R.string.pref_lang_hau_yor_value))){
            return HAUSA_YORUBA;

        }else {
            return ENGLISH_YORUBA;
        }

    }


}
