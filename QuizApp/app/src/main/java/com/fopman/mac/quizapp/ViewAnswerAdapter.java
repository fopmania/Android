package com.fopman.mac.quizapp;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.InterpolatorRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by mac on 2016-12-22.
 */

class ViewAnswerAdapter extends ArrayAdapter<Question> {

    int[] myAnswer;

    public TextView TextViewById(View vw, int id){
        return (TextView)vw.findViewById(id);
    }

    public ViewAnswerAdapter(Context context, int resource, List<Question> objects, int[] myAnswers) {
        super(context, resource, objects);
        this.myAnswer = myAnswers;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater li = LayoutInflater.from(getContext());
        View vw = li.inflate(R.layout.list_answer, parent, false);

        Question q = getItem(position);
        StringBuilder sb = new StringBuilder(q.getQuestion());
        sb.append( "\n1. "+q.getOption1()+"\n2. "+q.getOption2()+"\n3. "+q.getOption3()+"\n4. "+q.getOption4());
        TextViewById(vw, R.id.tvQuestion).setText(sb);

        TextViewById(vw, R.id.tvCorrect).setText("Correct Answer : " + q.getAnswer());
        TextViewById(vw, R.id.tvMyAnswer).setText("My Answer : " + myAnswer[position]);
        if(Integer.parseInt(q.getAnswer()) == myAnswer[position] ) {
            TextViewById(vw, R.id.tvMyAnswer).setTextColor(Color.rgb(0,0,255));
        }else{
            TextViewById(vw, R.id.tvMyAnswer).setTextColor(Color.rgb(255,0,0));
        }


        return vw;
    }
}
