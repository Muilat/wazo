package com.muilat.android.wazo;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.muilat.android.wazo.adapter.WordAdapter;
import com.muilat.android.wazo.data.WordContract;

import static com.muilat.android.wazo.data.WordContract.CONTENT_URI;


/**
 * A simple {@link Fragment} subclass.
 */
    public class CommonFragment extends Fragment implements  LoaderManager.LoaderCallbacks<Cursor>  {

    private static final String TAG = CommonFragment.class.getSimpleName();
    private static final int WORD_LOADER_ID = 11123;
    int category_id = -1;

    public final static String ARG_CATEGORY_ID = "category_id";

    WordAdapter mWordAdapter;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            Bundle bundle = getArguments();
            category_id = bundle.getInt(ARG_CATEGORY_ID);
        }


        //Ensure a loader is initialized and active
//        getActivity().getSupportLoaderManager().initLoader(CATEGORY_LOADER_ID, null, this);
    }

    public CommonFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_common, container, false);

        RecyclerView recycler =  view.findViewById(R.id.word_recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(layoutManager);

        mWordAdapter = new WordAdapter();
        recycler.setAdapter(mWordAdapter);
        recycler.setHasFixedSize(true);

        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(getActivity()) {

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
                String selection = null;
                String[] selectionArgs = null;
                if(category_id > 0){
                    selection = "category_id=?";
                    selectionArgs = new String[]{String.valueOf((category_id))};
                }

                try {
                    return getActivity().getContentResolver().query(WordContract.CONTENT_URI,
                            null,
                            selection,
                            selectionArgs,
                            null);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage()+" Failed to asynchronously load words.");
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
        Log.e(TAG, "No of cursor: "+data.getCount());


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mWordAdapter.swapCursor(null);
    }

    @Override
    public void onResume() {
        super.onResume();

        // re-queries for all Categories
        getActivity().getSupportLoaderManager().restartLoader(WORD_LOADER_ID, null, this);
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
