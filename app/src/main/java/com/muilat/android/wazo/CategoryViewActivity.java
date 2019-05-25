package com.muilat.android.wazo;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.muilat.android.wazo.adapter.WordAdapter;
import com.muilat.android.wazo.data.WazoDbHelper;
import com.muilat.android.wazo.data.Word;
import com.muilat.android.wazo.data.WordContract;

public class CategoryViewActivity extends AppCompatActivity implements  LoaderManager.LoaderCallbacks<Cursor>  {

    private static final String TAG = CategoryViewActivity.class.getSimpleName();
    private static final int WORDS_LOADER_ID = 11123;
    int category_id = -1;

    public final static String EXTRA_CATEGORY_ID = "category_id";
    public final static String EXTRA_CATEGORY_TITLE = "category_title";

    WazoDbHelper dbHelper;

    static WordAdapter mWordAdapter;
    private SQLiteDatabase mDb;
//    private InterstitialAd interstitialAd;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView title = findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);


        RecyclerView recycler =  findViewById(R.id.words_recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);

        mWordAdapter = new WordAdapter();
        recycler.setItemAnimator(new DefaultItemAnimator()); //Animator for recycler view
        recycler.setAdapter(mWordAdapter);
        recycler.setHasFixedSize(true);

        if (getIntent() != null) {
            category_id = getIntent().getIntExtra(EXTRA_CATEGORY_ID, 0);
            title.setText(getIntent().getStringExtra(EXTRA_CATEGORY_TITLE));
        }

        dbHelper = new WazoDbHelper(this);
        mDb= dbHelper.getReadableDatabase();


        //Ensure a loader is initialized and active
        getSupportLoaderManager().initLoader(WORDS_LOADER_ID, null, this);

        //adView
//        LinearLayout adViewLinearLayout = findViewById(R.id.adViewLayout);
//        Utils.loadAdView(this, adViewLinearLayout);
//
//        //interstitialAd
//        interstitialAd = new InterstitialAd(SubCategoryFragment.this);
//        interstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                super.onAdClosed();
//                Log.e(TAG, "ad closed");
//                interstitialAd.loadAd(new AdRequest.Builder().build());
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                super.onAdFailedToLoad(errorCode);
//                Log.e(TAG, "ad failed");
//
//            }
//
//            @Override
//            public void onAdLeftApplication() {
//                super.onAdLeftApplication();
//                Log.e(TAG, "ad left application");
//
//            }
//
//            @Override
//            public void onAdOpened() {
//                super.onAdOpened();
//                Log.e(TAG, "ad opened");
//
//            }
//
//            @Override
//            public void onAdLoaded() {
//                super.onAdLoaded();
//                Log.e(TAG, "ad loaded");
//
//            }
//        });
//
//        //interstitialAd
//        Handler handlerInterstitialAd = new Handler();
//        handlerInterstitialAd.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                interstitialAd.setAdUnitId(OfflineTutorialPreference.getInterstitialId(SubCategoryFragment.this));
//
//                interstitialAd.loadAd(new AdRequest.Builder().build());
//            }
//        },1000);

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(CategoryViewActivity.this) {

            // Initialize a Cursor, this will hold all the words data
            Cursor mWordsData = null;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mWordsData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mWordsData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {
//                String stringId = Long.toString(category_id);
//                Uri uri = WordContract.CONTENT_URI;
//                uri = uri.buildUpon().appendPath(stringId).build();
//
//
                try {
//                    return getContentResolver().query(uri,
//                            null,
//                            null,
//                            null,
//                            null);
                String sql = "SELECT * FROM words WHERE category_id =? ";
                return mDb.rawQuery(sql,new String[] {Long.toString(category_id)});
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage()+" Failed to asynchronously load Categorys.");
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(Cursor data) {
                mWordsData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mWordAdapter.swapCursor(data);
        Log.e(TAG, "No of cursor/words: "+data.getCount());

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mWordAdapter.swapCursor(null);
    }


    @Override
    public void onResume() {
        super.onResume();

        // re-queries for all SubCategories
        //getSupportLoaderManager().restartLoader(SUB_CATEGORY_LOADER_ID, null, this);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void onArrowBackClick(View view){
//        showInterstitialAd();
        finish();
    }

    @Override
    public void onBackPressed() {
//        showInterstitialAd();
        finish();
    }

    public void onSearchClick(View view){
        Intent searchIntent = new Intent(this, SearchActivity.class);
//        searchIntent.putParcelableArrayListExtra(SearchActivity.EXTRA_SEARCHES,mLessons);;
        startActivity(searchIntent);

    }

//    private void showInterstitialAd(){
//        if(interstitialAd != null && interstitialAd.isLoaded()){
//            interstitialAd.show();
//        }
//    }


}
