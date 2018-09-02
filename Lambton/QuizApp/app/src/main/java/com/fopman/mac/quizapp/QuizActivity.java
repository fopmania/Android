package com.fopman.mac.quizapp;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.BoolRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fopman.mac.quizapp.db.DBAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {
    private static final int MAX_QUESTION = 5;

    public static List<Question> listQuestion = new ArrayList<>();
    public static int [] arrMyAnswer = new int[MAX_QUESTION];

    private int questionCnt = 0;
    private int correctCnt = 0;

    private int answerNo = 0;
    Question    curQuestion;

    private float myPoint = 0.0f;
    private float totalPoint = 0.0f;


    private TextView lblQuestion,lblTitle;
    private RadioButton rb1, rb2, rb3,rb4;
    private ImageView   imgView;
    private Button btNext;
    private RadioGroup rgAnswer;

    private DBAdapter dba = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        init();
    }



    void init(){
        questionCnt = 0;
        lblTitle = (TextView)findViewById(R.id.lblQuizTitle);
        lblQuestion = (TextView)findViewById(R.id.lblQuestion);
        imgView = (ImageView) findViewById(R.id.imgQuestion);
        rb1 = (RadioButton)findViewById(R.id.rbAnswer1);
        rb2 = (RadioButton)findViewById(R.id.rbAnswer2);
        rb3 = (RadioButton)findViewById(R.id.rbAnswer3);
        rb4 = (RadioButton)findViewById(R.id.rbAnswer4);
        btNext = (Button)findViewById(R.id.btNext);
        rgAnswer = (RadioGroup)findViewById(R.id.rgSelectedAnswer);

        //  Data Base setting
        if(MainActivity.SelectedCategory == eCategory.Type1){
            dba = new DBAdapter(this, "history.json");
        }
        else if(MainActivity.SelectedCategory == eCategory.Type2){
            dba = new DBAdapter(this, "sports.json");
        }
        if(dba != null){
            BuildQuestions();
            curQuestion = listQuestion.get(questionCnt);
            setQuestionsView();
        }
    }

    void BuildQuestions(){

        List<Integer> qnum = new ArrayList<>();
        List<Question> listQ = dba.getAllQuestions();

        for(int i = 0; i < listQ.size(); i++){
            qnum.add(i);
        }
        Collections.shuffle(qnum);

        listQuestion.clear();

        for(int i = 0; i < MAX_QUESTION; i++){
            listQuestion.add(listQ.get(qnum.get(i)));
        }
    }

    public Bitmap getBitmapFromAssets(String fileName) {
        AssetManager assetManager = getAssets();

        InputStream is = null;
        try {
            is = assetManager.open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(is);

        return bitmap;
    }
    private void setQuestionsView()
    {
        answerNo = questionCnt+1;

        lblTitle.setText("Question "+answerNo+"/"+MAX_QUESTION);

        lblQuestion.setText(curQuestion.getQuestion());
        imgView.setImageBitmap(getBitmapFromAssets("images/" + curQuestion.getImage()));
        rb1.setText(curQuestion.getOption1());
        rb2.setText(curQuestion.getOption2());
        rb3.setText(curQuestion.getOption3());
        rb4.setText(curQuestion.getOption4());
//        rb1.setChecked(false);
//        rb2.setChecked(false);
//        rb3.setChecked(false);
//        rb4.setChecked(false);

        rgAnswer.clearCheck();

    }

    private int getMyAnswer(){
        int cid = rgAnswer.getCheckedRadioButtonId();
        switch(cid){
            case R.id.rbAnswer1:
                return 1;
            case R.id.rbAnswer2:
                return 2;
            case R.id.rbAnswer3:
                return 3;
            case R.id.rbAnswer4:
                return 4;
        }
        return 0;
    }

    public void onClickNext(View view) {
        if(rgAnswer.getCheckedRadioButtonId() == -1)    return;

        arrMyAnswer[questionCnt] = getMyAnswer();
        if (arrMyAnswer[questionCnt] == Integer.parseInt(curQuestion.getAnswer())) {
            myPoint += curQuestion.getPoint();
            correctCnt++;
        }
        totalPoint += curQuestion.getPoint();

        questionCnt++;

        if (questionCnt <  MAX_QUESTION) {
            curQuestion = listQuestion.get(questionCnt);
            setQuestionsView();
        } else {
            Intent it = new Intent(QuizActivity.this, ResultActivity.class);
            it.putExtra("totalPoint", totalPoint);
            it.putExtra("myPoint", myPoint);
            it.putExtra("questionNo", questionCnt);
            it.putExtra("correctNo", correctCnt);

            startActivity(it);
            finish();
        }
    }
}
