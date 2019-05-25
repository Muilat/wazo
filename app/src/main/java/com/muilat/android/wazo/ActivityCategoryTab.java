package com.muilat.android.wazo;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.muilat.android.wazo.adapter.ViewPagerAdapter;
import com.muilat.android.wazo.data.Categories;
import com.muilat.android.wazo.data.CategoryContract;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.ArrayList;

public class ActivityCategoryTab extends AppCompatActivity{

    public static final String DEFAULT_TAB = "default-tab";
    ArrayList<Categories> mCategories = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_tab_layout);

//        Cursor cursor = getContentResolver().query(CategoryContract.CONTENT_URI,null,null,null,null);
//
//
//
//        Toolbar toolbar = findViewById(R.id.toolbar);
////    toolbar.setTitle(demo.titleResId);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        ViewPager viewPager = findViewById(R.id.viewpager);
//        SmartTabLayout viewPagerTab = findViewById(R.id.viewpagertab);
//
//
//        FragmentPagerItems pages = new FragmentPagerItems(this);
//        while (cursor.moveToNext()){
//            Categories category = new Categories(cursor);
//            CategoryFragment categoryFragment = new CategoryFragment();
//            Bundle args = new Bundle();
//            args.putInt(CategoryFragment.ARG_CATEGORY_ID, category.getId());
//            categoryFragment.setArguments(args);
//            pages.add(FragmentPagerItem.of(category.getName(), categoryFragment.getClass()));
//        }
//
//        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
//                getSupportFragmentManager(), pages);
//
//        viewPager.setAdapter(adapter);
//        viewPager.setCurrentItem(getIntent().getIntExtra(DEFAULT_TAB,1));
//        viewPagerTab.setViewPager(viewPager);

    }
}
