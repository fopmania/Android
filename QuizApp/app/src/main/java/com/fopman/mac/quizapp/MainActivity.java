package com.fopman.mac.quizapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

enum eCategory{
        Null,
        Type1,
        Type2
        }

public class MainActivity extends AppCompatActivity {

    public static Context context = null;
    public static eCategory SelectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_main);
    }


    public void onClickCategory1(View view) {
        SelectedCategory = eCategory.Type1;
        Intent it = new Intent(MainActivity.this, QuizActivity.class);
        startActivity(it);
    }

    public void onClickCategory2(View view) {
        SelectedCategory = eCategory.Type2;
        Intent it = new Intent(MainActivity.this, QuizActivity.class);
        startActivity(it);
    }
}
