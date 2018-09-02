package com.fopman.mac.quizapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {
    float totalPoint = 0.0f;
    float myPoint = 0.0f;
    int questionNo = 0;
    int correctNo = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent it = getIntent();
        totalPoint = it.getFloatExtra("totalPoint", 0.0f);
        myPoint = it.getFloatExtra("myPoint", 0.0f);
        questionNo = it.getIntExtra("questionNo", 0);
        correctNo = it.getIntExtra("correctNo", 0);

        ((TextView)findViewById(R.id.lblAnswerInfo)).setText("Total : " + (int)questionNo + "\t Correct :" + (int)correctNo);

        TextView tvMsg = (TextView)findViewById(R.id.lblResultMessage);

        MediaPlayer mp = MediaPlayer.create(this, R.raw.cheering);


        String msg = "Please	try	again!";
        int color = Color.rgb(255,0,0);

        if(correctNo >= 5){
            msg =  "You are a genius!";
            color = Color.rgb(0,0, 255);
        }
        else if(correctNo >= 4){
            msg =  "Excellent  work!";
            color = Color.rgb(0, 55, 200);
        }
        else if(correctNo >= 3){
            msg =  "Good   job!";
            color = Color.rgb(0, 150, 55);
        }
        else{
            mp = MediaPlayer.create(this, R.raw.booing);
        }

        mp.start();
        tvMsg.setTextColor(color);
        tvMsg.setText(msg);

        RatingBar ratingBar = ((RatingBar) findViewById(R.id.ratingBarAnswer));
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        ratingBar.setRating(correctNo*5.0f/questionNo);
        stars.getDrawable(2).setColorFilter(color, PorterDuff.Mode.SRC_ATOP);


    }

    public void onClickHome(View view) {
        Intent it = new Intent(ResultActivity.this, MainActivity.class );
        startActivity(it);
    }

    public void onClickAnswer(View view) {
        Intent it = new Intent(ResultActivity.this, ViewAnswerActivity.class );
        startActivity(it);
    }
}
