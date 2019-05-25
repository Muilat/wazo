package com.muilat.android.wazo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.filippudak.ProgressPieView.ProgressPieView;
import com.muilat.android.wazo.data.Word;

import java.util.ArrayList;

public class QuizResultActivity  extends AppCompatActivity {
    private static final String TAG = QuizResultActivity.class.getSimpleName();


    public static final String CORRECT_ANSWER = "correct-answer";
    public static final String WRONG_ANSWER = "wrong-answer";

    int mCorrectAnswer, mWrongAnswer;

    TextView wrongAnswerTxt, correctAnswerTxt;
    ProgressPieView progressPieView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        wrongAnswerTxt = findViewById(R.id.wrong);
        correctAnswerTxt = findViewById(R.id.correct);
        progressPieView = findViewById(R.id.progressPieView);
        TextView t=(TextView)findViewById(R.id.textResult);



        if(getIntent() != null){
            mCorrectAnswer = getIntent().getIntExtra(CORRECT_ANSWER, 0);
            mWrongAnswer = getIntent().getIntExtra(WRONG_ANSWER, 0);
        }

        wrongAnswerTxt.setText(mWrongAnswer+"");
        correctAnswerTxt.setText(mCorrectAnswer+"");

        float total = mCorrectAnswer + mWrongAnswer;
        final float percentageCorrect = (mCorrectAnswer/total) * 100;

        progressPieView.setOnProgressListener(new ProgressPieView.OnProgressListener() {
            @Override
            public void onProgressChanged(int progress, int max) {
                if (!progressPieView.isTextShowing()) {
                    progressPieView.setShowText(true);
                    progressPieView.setShowImage(false);
                }
            }

            @Override
            public void onProgressCompleted() {
                if (!progressPieView.isImageShowing()) {
                    progressPieView.setShowImage(true);
                }
                progressPieView.setShowText(false);
                progressPieView.setImageResource(R.drawable.ic_favorite_24dp);
            }
        });

        if (percentageCorrect>=80 && percentageCorrect<=100){
            t.setText("Score is Excellent !");
        }else if(percentageCorrect>=70 && percentageCorrect<=79){
            t.setText("Score is Very Good!");
        }else if(percentageCorrect>=60 && percentageCorrect<=69){
            t.setText("Score is Good");
        }else if(percentageCorrect>=50 && percentageCorrect<=59){
            t.setText("Score is Average!");
        }else if(percentageCorrect>=33 && percentageCorrect<=49){
            t.setText("Score is  Below Average!");
        }else{
            t.setText("Score is Poor! You need to practice more!");
        }



        Log.e(TAG, percentageCorrect+"%");

        progressPieView.setProgress(0);

        progressPieView.animateProgressFill(Math.round(percentageCorrect));
        progressPieView.setShowText(true);


        Button btnShare=(Button)findViewById(R.id.btnShare);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textToShare = "I scored "+percentageCorrect+"% in Wazo Quiz, Checkout what youwill score";
                textToShare += "\nInstall from http://play.google.com/store/apps/details?id=" + getPackageName();

                Intent shareIntent = ShareCompat.IntentBuilder.from(QuizResultActivity.this)
                        .setText(textToShare)
                        .setChooserTitle("Share Developer with")
                        .setSubject(getString(R.string.app_name))
                        .setType("text/plain")
                        .createChooserIntent();

                if(shareIntent.resolveActivity(getPackageManager()) != null){
                    startActivity(shareIntent);
                }
            }
        });

        Button btnExit=(Button)findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showInterstitialAd();
                finish();
            }
        });

        Button btnplayAgain=(Button)findViewById(R.id.btnPlayAgain);
        btnplayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showInterstitialAd();
                Intent quizIntent=new Intent(QuizResultActivity.this,QuizActivity.class);

                startActivity(quizIntent);

            }
        });

    }


}
