package com.muilat.android.wazo;

import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easyandroidanimations.library.BounceAnimation;
import com.easyandroidanimations.library.FlipHorizontalToAnimation;
import com.muilat.android.wazo.data.Word;
import com.muilat.android.wazo.data.WordContract;
import com.muilat.android.wazo.utils.Utils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;

public class FlashCardActivity   extends AppCompatActivity  implements
        LoaderManager.LoaderCallbacks<Cursor>  {

    private static final String TAG = FlashCardActivity.class.getSimpleName();

    private static final int QUIZ_LOADER_ID = 19023;

    static int i;

    ArrayList<Word> remainingQuizArrayList = new ArrayList<>();
    ArrayList<Word> quizArrayList = new ArrayList<>();
    static Word wordQuestion;

    int searchBy;


    TextView textBehind, textTarget;
    TextView eng_tv, yor_tv, has_tv;
    private int lang;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_card);

        textBehind = findViewById(R.id.text_behind);
        textTarget = findViewById(R.id.text_target);
        eng_tv = findViewById(R.id.lang_eng);
        has_tv = findViewById(R.id.lang_has);
        yor_tv = findViewById(R.id.lang_yor);

        lang = Utils.getLangPref(this);
        switch (lang){
            case Utils.ENGLISH_YORUBA:
                searchBy = lang;

                eng_tv.setBackgroundColor(getResources().getColor(R.color.colorControlActivated));
                break;
            case Utils.HAUSA_YORUBA:
                searchBy = lang;

                has_tv.setBackgroundColor(getResources().getColor(R.color.colorControlActivated));
                break;
            default://no use
                searchBy = 0;
        }

        getSupportLoaderManager().restartLoader(QUIZ_LOADER_ID, null, this);


        textBehind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textTarget.setVisibility(View.VISIBLE);
                new FlipHorizontalToAnimation(textBehind)
                        .setFlipToView(textTarget)
                        .setInterpolator(new LinearInterpolator())
                        .animate();
                textBehind.setVisibility(View.GONE);

//                textTarget.setText(wordQuestion.getYoruba());

            }

        });

        textTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildQuestion();
            }

        });

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {

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
                try {
                    return getContentResolver().query(WordContract.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage()+" Failed to asynchronously load quiz.");
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
        while (data.moveToNext()){
            remainingQuizArrayList.add(new Word(data));
            quizArrayList.add(new Word(data));
        }
        data.close();

        Log.e(TAG, "No of cursor: "+data.getCount());

        buildQuestion();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
//        mWordAdapter.swapCursor(null);
    }


    private void buildQuestion() {


        if(remainingQuizArrayList.size() == 0){
            remainingQuizArrayList = new ArrayList<>(quizArrayList);
        }

        textTarget.setVisibility(View.GONE);
        textBehind.setVisibility(View.VISIBLE);
        new BounceAnimation(textBehind).animate();



        Random random = new Random();
        i = random.nextInt(remainingQuizArrayList.size()); // i will be the correct answer

        //question word
        wordQuestion = remainingQuizArrayList.get(i);

        //remove i from remaining to minimize search
        remainingQuizArrayList.remove(remainingQuizArrayList.get(i));


        switch (searchBy){
            case Utils.ENGLISH_YORUBA:
                textBehind.setText(wordQuestion.getEnglish());
                textTarget.setText(wordQuestion.getYoruba());
                break;
            case Utils.HAUSA_YORUBA:
                textBehind.setText(wordQuestion.getHausa());
                textTarget.setText(wordQuestion.getYoruba());
                break;
            default://0
                textBehind.setText(wordQuestion.getYoruba());
                if(lang == Utils.ENGLISH_YORUBA)
                    textTarget.setText(wordQuestion.getEnglish());
                else if(lang == Utils.HAUSA_YORUBA)
                    textTarget.setText(wordQuestion.getHausa());




        }
        textBehind.setText(wordQuestion.getDisplayName(this));
//        textTarget.setContentDescription(wordQuestion.getYoruba());

    }

    public void langClick(View view) {

        eng_tv.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        yor_tv.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        has_tv.setBackgroundColor(getResources().getColor(R.color.colorAccent));

        view.setBackgroundColor(getResources().getColor(R.color.colorControlActivated));

        if(view == eng_tv){
            searchBy = Utils.ENGLISH_YORUBA;

        }
        else if(view == yor_tv){
            searchBy = 0;

        }
        else if(view == has_tv){
            searchBy = Utils.HAUSA_YORUBA;

        }

        buildQuestion();
    }

}