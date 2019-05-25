package com.muilat.android.wazo;

import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.muilat.android.wazo.adapter.FavouriteAdapter;
import com.muilat.android.wazo.adapter.WordAdapter;
import com.muilat.android.wazo.data.Word;
import com.muilat.android.wazo.data.WordContract;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class FavouriteActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>  {

    private static final int FAVOURITE_LOADER_ID = 775;
    private static final String TAG = FavouriteActivity.class.getSimpleName();
    FavouriteAdapter mFavouriteAdapter;

    ArrayList<Word> mFavouriteArrayList = new ArrayList<>();
    static RecyclerView recycler;
    static ImageView delete_all, closeDialog;
    static LinearLayout emptyView;


    Button confrimDelete;
    Dialog deleteAllDialog;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("Favourites");
        setSupportActionBar(toolbar);


        //adView
//        LinearLayout adViewLinearLayout = findViewById(R.id.adViewLayout);
//        Utils.loadAdView(this, adViewLinearLayout);





        delete_all = findViewById(R.id.delete_all);
        emptyView = findViewById(R.id.emptyView);
        mFavouriteAdapter = new FavouriteAdapter();


        recycler =  findViewById(R.id.favourite_recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recycler.setLayoutManager(layoutManager);

        recycler.setItemAnimator(new DefaultItemAnimator()); //Animator for recycler view
        recycler.setAdapter(mFavouriteAdapter);
        recycler.setHasFixedSize(true);

        getSupportLoaderManager().initLoader(FAVOURITE_LOADER_ID, null, this);


        delete_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SweetAlertDialog alert = new SweetAlertDialog(FavouriteActivity.this, SweetAlertDialog.WARNING_TYPE);
                        alert.setTitleText("Are you sure you want to delete all favourites?")
                        .setContentText("You wont be abe to retrieve")
                        .setConfirmText("Yes, delete it!")
                        .setCancelText("No, cancel!")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                for(Word word: mFavouriteArrayList){
                                    String stringId = Long.toString(word.getID());
                                    Uri uri = WordContract.CONTENT_URI;
                                    uri = uri.buildUpon().appendPath(stringId).build();
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put(WordContract.WordEntry.COLUMN_IS_FAVOURITE, 0);
                                    word.setIsFavourite("0");
                                    getContentResolver().update(uri,contentValues,null,null);

                                }
                                sweetAlertDialog.setTitleText("Done!")
                                        .setConfirmText("OK!")
                                        .setContentText("Favourites deleted!")
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                favouriteEmpty();
                            }
                        })
                        .show();
//                confrimDelete.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {


//                    }
//                });
//
//                deleteAllDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                deleteAllDialog.show();
            }
        });

//        mFavouriteAdapter.

        recycler.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(RecyclerView.ViewHolder holder) {

            }
        });

//        recycler.onChan


    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {

            // Initialize a Cursor, this will hold all the Category data
//            Cursor mCategoriesData = null;
            Cursor mLessonsData;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mLessonsData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mLessonsData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {

                String selection = WordContract.WordEntry.COLUMN_IS_FAVOURITE+" =? ";
                String selectionArgs[] = {new String("1")};

                try {
                    return getContentResolver().query(WordContract.CONTENT_URI,
                            null,
                            selection,
                            selectionArgs,
                            null);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage()+" Failed to asynchronously load favourites.");
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(Cursor data) {
                mLessonsData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        for (int i = 0;  i< data.getCount(); i++) {
            data.moveToPosition(i);
            Word lesson = new Word(data);
            mFavouriteArrayList.add(lesson);

        }
        mFavouriteAdapter.swapCursor(mFavouriteArrayList);
//        Log.e(TAG, "No of cursor: "+data.getCount());

        if(data.getCount()==0){
            favouriteEmpty();
        }

    }


    @Override
    public void onLoaderReset(Loader<Cursor>loader) {
        mFavouriteAdapter.swapCursor(null);
    }



    public static void favouriteEmpty() {
        emptyView.setVisibility(View.VISIBLE);
        delete_all.setVisibility(View.GONE);
        recycler.setVisibility(View.GONE);
    }

    public static void favouriteNotEmpty() {
        emptyView.setVisibility(View.GONE);
        delete_all.setVisibility(View.VISIBLE);
        recycler.setVisibility(View.VISIBLE);
    }

    public void onArrowBackClick(View view){
        finish();

    }


}

