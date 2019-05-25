package com.muilat.android.wazo;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.muilat.android.wazo.data.Word;
import com.muilat.android.wazo.data.WordContract;
import com.muilat.android.wazo.views.AnswerView;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;

public class QuizActivity extends AppCompatActivity implements
        AnswerView.OnAnswerSelectedListener,
        LoaderManager.LoaderCallbacks<Cursor>  {
    private static final String TAG = QuizActivity.class.getSimpleName();

    private static final int QUIZ_LOADER_ID = 10023;

    //Number of quiz answers
    public static final int ANSWER_COUNT = 4;
    static int i;
    int correctAnswer = 0;
    int wrongAnswer = 0;
//    static Set<Word> quizSet = new LinkedHashSet<>();


    ArrayList<Word> quizArrayList = new ArrayList<>();
    ArrayList<Word> remainingQuizArrayList = new ArrayList<>();
    static Word wordQuestion;

    ProgressBar progressBar;
    private TextView mQuestionText;
    private TextView mCorrectText, mCorrectAnswer;
    private AnswerView mAnswerSelect;
    LinearLayout livesHolder;

    private int lives = 5;
    private Button mNextQuestion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        mQuestionText = findViewById(R.id.text_question);
        mCorrectText = findViewById(R.id.text_correct);
        mAnswerSelect = findViewById(R.id.answer_select);
        mCorrectAnswer = findViewById(R.id.correct_answer);
        mNextQuestion = findViewById(R.id.next_question);
        livesHolder = findViewById(R.id.lives_holder);


        progressBar = findViewById(R.id.progressBar);

        mAnswerSelect.setOnAnswerSelectedListener(this);

        progressBar.setVisibility(View.VISIBLE);


        getSupportLoaderManager().restartLoader(QUIZ_LOADER_ID, null, QuizActivity.this);

        for (int l = 0; l < lives; l++){
            ImageView live = new ImageView(this);
            live.setImageResource(R.drawable.ic_favorite_24dp);
            livesHolder.addView(live);
        }



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
            quizArrayList.add(new Word(data));
            remainingQuizArrayList.add(new Word(data));
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

        progressBar.setVisibility(View.VISIBLE);
        mAnswerSelect.setEnabled(true);


        if(remainingQuizArrayList.size() == 0){
            showResult();
            finish();
            return;
        }



        Random random = new Random();
        i = random.nextInt(remainingQuizArrayList.size()); // i will be the correct answer

        //question word
        wordQuestion = remainingQuizArrayList.get(i);

        //remove i from remaining to minimize search
        remainingQuizArrayList.remove(remainingQuizArrayList.get(i));


        List<Word> answers = new ArrayList<>();


        List<Word> remainingAnswers = new ArrayList<>();

        remainingAnswers.addAll(quizArrayList);

        Word rightAnswer = remainingAnswers.get(i);
        remainingAnswers.remove(rightAnswer);

        LinkedHashSet<Integer> answerSet = new LinkedHashSet<>();
        //generate indexes for options/answers
//        while (answerSet.size() < ANSWER_COUNT){
        for(int k = 0; k < ANSWER_COUNT; k++){
            int a = random.nextInt(remainingAnswers.size());
            Word w = remainingAnswers.get(a);
                answers.add(w);
                remainingAnswers.remove(w);

        }



//        if(!answers.contains(rightAnswer)){
            //correct answer option is not yet added
            //randomly put correct answer in answers;
            int correctAnswerIndex = random.nextInt(ANSWER_COUNT);
//            Log.e(TAG, "correct answer index : "+correctAnswerIndex);
            answers.remove(correctAnswerIndex);//remove answer in index and replace with corrwect answer
            answers.add(correctAnswerIndex, rightAnswer);

//        }


        String question = getString(R.string.question_text, wordQuestion.getDisplayName(this));
        mQuestionText.setText(question);

        //Load answer strings
        ArrayList<String> options = new ArrayList<>();
        for (Word item : answers) {
            options.add(item.getYoruba());
        }

        mAnswerSelect.loadAnswers(options, wordQuestion.getYoruba());
        progressBar.setVisibility(View.GONE);

    }

    /* Answer Selection Callbacks */

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCorrectAnswerSelected() {
        correctAnswer++;
        updateResultText();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onWrongAnswerSelected() {
        wrongAnswer++;
        updateResultText();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void updateResultText() {

//        mCorrectText.setTextColor(mAnswerSelect.isCorrectAnswerSelected() ?
//                getColor(R.color.colorCorrect) : getColor( R.color.colorWrong)
//        );
//        mCorrectText.setText(mAnswerSelect.isCorrectAnswerSelected() ?
//                R.string.answer_correct : R.string.answer_wrong
//        );

        if(mAnswerSelect.isCorrectAnswerSelected()){
            mCorrectText.setTextColor(getColor(R.color.colorCorrect));
            mCorrectText.setText(R.string.answer_correct);
        }
        else{
            mCorrectText.setTextColor(getColor(R.color.colorWrong));
            mCorrectText.setText(getString(R.string.answer_wrong));
            mCorrectAnswer.setText(wordQuestion.getYoruba());
            mCorrectAnswer.setTextColor(getColor(R.color.colorCorrect));

            lives--;

            livesHolder.removeViewAt(lives);



            if(lives==0){
                showResult();

                finish();
            }

        }

        mAnswerSelect.setEnabled(false);
        mNextQuestion.setEnabled(true);

    }

    private void showResult() {
        Intent resultIntent = new Intent(this, QuizResultActivity.class);
        resultIntent.putExtra(QuizResultActivity.CORRECT_ANSWER,  correctAnswer);
        resultIntent.putExtra(QuizResultActivity.WRONG_ANSWER,  wrongAnswer);

        startActivity(resultIntent);
    }

    public void nextQuestion(View view) {
//        counter++;
//        mCorrectText.setText("");
//        if(counter > quizArrayList.size()){
//            Toast.makeText(this, "End o", Toast.LENGTH_SHORT).show();
//            view.setVisibility(View.GONE);
//            return;
//
//        }
        mCorrectAnswer.setText("");
        mCorrectText.setText("");
        view.setEnabled(false);
        buildQuestion();

    }
}