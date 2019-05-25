package com.muilat.android.wazo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.muilat.android.wazo.adapter.WordAdapter;
import com.muilat.android.wazo.data.WazoDbHelper;
import com.muilat.android.wazo.data.WordContract.WordEntry;
import com.muilat.android.wazo.utils.Utils;

public class SearchActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>  {

    private static final String TAG = "SearchActivity";
    private static final int SEARCH_LOADER_ID = 1090;

    WordAdapter mSearchAdapter;

    TextView eng_tv, yor_tv, has_tv;

    String queryText = "SELECT * FROM words WHERE _id = -1";//yes wrong query dont want anything returned

    String whereColumn;
    int searchBy;

    private SQLiteDatabase mDb;
    private WazoDbHelper dbHelper;

    private SearchView searchView;
    private String searchIext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        eng_tv = findViewById(R.id.lang_eng);
        has_tv = findViewById(R.id.lang_has);
        yor_tv = findViewById(R.id.lang_yor);

        RecyclerView recycler =  findViewById(R.id.search_recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recycler.setLayoutManager(layoutManager);

        recycler.setItemAnimator(new DefaultItemAnimator()); //Animator for recycler view



        mSearchAdapter = new WordAdapter();
        recycler.setAdapter(mSearchAdapter);
        recycler.setHasFixedSize(true);

        searchBy = Utils.getLangPref(this);
        switch (searchBy){
            case Utils.ENGLISH_YORUBA:
                whereColumn = WordEntry.COLUMN_ENGLISH;
                eng_tv.setBackgroundColor(getResources().getColor(R.color.colorControlActivated));
                break;
            case Utils.HAUSA_YORUBA:
                whereColumn = WordEntry.COLUMN_HAUSA;
                has_tv.setBackgroundColor(getResources().getColor(R.color.colorControlActivated));
                break;
                default:
                    whereColumn = WordEntry.COLUMN_YORUBA;


        }

        dbHelper = new WazoDbHelper(this);
        mDb= dbHelper.getReadableDatabase();
        getSupportLoaderManager().initLoader(SEARCH_LOADER_ID, null, SearchActivity.this);



        searchView = findViewById(R.id.searchView);
        searchView.setIconified(false);
        searchView.setIconifiedByDefault(true);
        searchView.requestFocusFromTouch();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if(newText.equals("")){

                    mSearchAdapter.swapCursor(null);
                }else{
                    searchIext = newText;
                    queryText = "SELECT * FROM words WHERE "+whereColumn+" LIKE '%"+newText+"%'";

                    getSupportLoaderManager().restartLoader(SEARCH_LOADER_ID, null, SearchActivity.this);

//                    return true;
                }

                return false;
            }
        });



    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {

            // Initialize a Cursor, this will hold all the Category data
//            Cursor mCategoriesData = null;
            Cursor mWordsData;

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


                try {
                    Cursor cursor = mDb.rawQuery(queryText, null);

                    return cursor;
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage()+" Failed to asynchronously load Lessons.");
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
        Log.e(TAG, "No of words: "+data.getCount());


        mSearchAdapter.swapCursor(data, searchBy);

//        setLessonView(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor>loader) {
        mSearchAdapter.swapCursor(null);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // When the home button is pressed, take the user back to the previous Activity
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    public void closeSearch(View view) {
        finish();
    }

    public void langClick(View view) {
        view = (TextView)view;

        eng_tv.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        yor_tv.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        has_tv.setBackgroundColor(getResources().getColor(R.color.colorAccent));

        view.setBackgroundColor(getResources().getColor(R.color.colorControlActivated));

        if(view == eng_tv){
            whereColumn = WordEntry.COLUMN_ENGLISH;
            searchBy = Utils.ENGLISH_YORUBA;

        }
        else if(view == yor_tv){
            whereColumn = WordEntry.COLUMN_YORUBA;
            searchBy = 0;

        }
        else if(view == has_tv){
            whereColumn = WordEntry.COLUMN_HAUSA;
            searchBy = Utils.HAUSA_YORUBA;

        }
        queryText = "SELECT * FROM words WHERE "+whereColumn+" LIKE '%"+searchIext+"%'";

        getSupportLoaderManager().restartLoader(SEARCH_LOADER_ID, null, SearchActivity.this);

    }
}
