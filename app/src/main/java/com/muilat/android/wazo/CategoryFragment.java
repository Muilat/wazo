package com.muilat.android.wazo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jorgecastilloprz.pagedheadlistview.PagedHeadListView;
import com.jorgecastilloprz.pagedheadlistview.utils.PageTransformerTypes;
import com.muilat.android.wazo.adapter.CategoryAdapter;
import com.muilat.android.wazo.adapter.CategoryListAdapter;
import com.muilat.android.wazo.adapter.WordAdapter;
import com.muilat.android.wazo.data.Categories;
import com.muilat.android.wazo.data.CategoryContract;
import com.muilat.android.wazo.data.WazoDbHelper;
import com.muilat.android.wazo.data.WordContract;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;

import java.util.ArrayList;


public class CategoryFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>  {

    private static final int CATEGORY_LOADER_ID = 1111;
    private static final String TAG = CategoryFragment.class.getName();

    ArrayList<Categories> mCategories = new ArrayList<>();

    PagedHeadListView mPagedHeadListView;
    CategoryListAdapter mCategoryListAdapter;


    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);



        RelativeLayout rootView = view.findViewById(R.id.rootView);
        mPagedHeadListView = new PagedHeadListView(getActivity());
        mPagedHeadListView.setHeaderHeight(250);
//        mPagedHeadListView.setOnHeaderPageChangeListener(ViewPager.OnPageChangeListener onP);
        mPagedHeadListView.addFragmentToHeader(new PageHeadListViewFragment());
        mPagedHeadListView.setIndicatorBgColor(getResources().getColor(R.color.colorPrimaryDark));
        mPagedHeadListView.setIndicatorColor(getResources().getColor(R.color.colorPrimary));
        mPagedHeadListView.disableVerticalTouchOnHeader();
        mPagedHeadListView.setHeaderOffScreenPageLimit(4);
        mPagedHeadListView.setHeaderPageTransformer(PageTransformerTypes.FLIP);




        rootView.addView(mPagedHeadListView);


        mPagedHeadListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Categories category = (Categories)adapterView.getItemAtPosition(i);
                Intent intent =new Intent(getActivity(), CategoryViewActivity.class);
//                i = i - 1;
//                Log.e(TAG, "categoryd_id "+ category.getId());
//                intent.putExtra(CategoryViewActivity.DEFAULT_TAB,i);
                intent.putExtra(CategoryViewActivity.EXTRA_CATEGORY_ID,category.getId());
                intent.putExtra(CategoryViewActivity.EXTRA_CATEGORY_TITLE,category.getDisplayName(getActivity()));

                startActivity(intent);
            }
        });

        //Ensure a loader is initialized and active
        getActivity().getSupportLoaderManager().initLoader(CATEGORY_LOADER_ID, null, this);


        return view;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(getActivity()) {

            // Initialize a Cursor, this will hold all the Word data
            Cursor mWordData = null;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mWordData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mWordData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {

                try {
                    return getActivity().getContentResolver().query(CategoryContract.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);

                } catch (Exception e) {
                    Log.e(TAG, e.getMessage()+" Failed to asynchronously load words.");
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(Cursor data) {
                mWordData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        while(data.moveToNext())
            mCategories.add(new Categories(data));

        mCategoryListAdapter = new CategoryListAdapter(getActivity(), mCategories);
        mPagedHeadListView.setAdapter(mCategoryListAdapter);
        Log.e(TAG, "No of cursor: "+data.getCount());


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCategories.clear();
    }

    @Override
    public void onResume() {
        super.onResume();

        // re-queries for all Categories
//        getActivity().getSupportLoaderManager().restartLoader(CATEGORY_LOADER_ID, null, this);
    }

    @Override
    public void onPause() {
        super.onPause();
    }


}
