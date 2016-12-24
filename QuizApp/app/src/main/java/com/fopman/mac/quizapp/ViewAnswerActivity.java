package com.fopman.mac.quizapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class ViewAnswerActivity extends AppCompatActivity {

    ViewAnswerAdapter aalistAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_answer);

        aalistAnswer = new ViewAnswerAdapter(this, R.layout.list_answer, QuizActivity.listQuestion, QuizActivity.arrMyAnswer);
        ((ListView)findViewById(R.id.lvAnswer)).setAdapter(aalistAnswer);


    }
}
