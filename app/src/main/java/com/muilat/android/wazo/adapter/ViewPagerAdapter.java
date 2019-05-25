package com.muilat.android.wazo.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.preference.PreferenceManager;

import com.muilat.android.wazo.CategoryFragment;
import com.muilat.android.wazo.CommonFragment;
import com.muilat.android.wazo.R;
import com.muilat.android.wazo.data.Categories;

import java.util.ArrayList;

/**
 * Provides the appropriate {@link Fragment} for a view pager.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Categories> mCategories = new ArrayList<>();
//    FragmentManager fragmentManager;

    Context mContext;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
//        fragmentManager = fm;
    }

    //me testing
    public void swapData(ArrayList<Categories> categories, Context context){
        mCategories = categories;
        mContext = context;

        notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return new CategoryFragment();/*mCategories.get(position);*/
    }

    @Override
    public int getCount() {
        if(mCategories.isEmpty()){
            return 0;
        }

        return mCategories.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
//        switch (position){
//            case 0:
//                return "Categories";
//            case 1:
//                return "Common";
//            default:
//                return null;
//        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String user_lang_pref = sharedPreferences.getString(mContext.getString(R.string.pref_language_key),
                mContext.getResources().getString(R.string.pref_lang_hau_yor_value));

        if(user_lang_pref.equals(mContext.getResources().getString(R.string.pref_lang_hau_yor_value))){
            return mCategories.get(position).getHausa();

        }else {
            return mCategories.get(position).getName();
        }
    }


}