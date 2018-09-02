package com.fopman.mac.hrandroid;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mac on 2016-12-15.
 */

class CustomAdapter extends ArrayAdapter<Employee> {
    public CustomAdapter(Context context,int resource , ArrayList<Employee> arrE) {
        super(context, resource, arrE);
    }

    public TextView TextViewById(View vw, int id){
        return (TextView)vw.findViewById(id);
    }




    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater li = LayoutInflater.from(getContext());
        View vw = li.inflate(R.layout.list_item, parent, false);

        ImageView img = (ImageView)vw.findViewById(R.id.imgType);

        Employee em = getItem(position);
        TextViewById(vw, R.id.liName).setText(em.getName());
        TextViewById(vw, R.id.liAge).setText(Integer.toString(em.getAge()));
        if(em instanceof FullTime){
            FullTime ft = (FullTime)em;
//            TextViewById(vw, R.id.liVal1).setText(Integer.toString(ft.getSalary()));
//            TextViewById(vw, R.id.liVal2).setText(Integer.toString(ft.getBonus()));
            img.setImageResource(R.drawable.fulltime24);
        }else if(em instanceof PartTime){
            PartTime pt = (PartTime)em;
//            TextViewById(vw, R.id.liVal1).setText(Integer.toString(pt.getHours()));
//            TextViewById(vw, R.id.liVal2).setText(Integer.toString(pt.getRate ()));
            img.setImageResource(R.drawable.parttime24);
        }else if(em instanceof Intern){
            Intern it = (Intern)em;
//            TextViewById(vw, R.id.liVal1).setText(it.getSchoolName());
//            TextViewById(vw, R.id.liVal2).setText("");
            img.setImageResource(R.drawable.intern24);
        }
        img.setScaleType(ImageView.ScaleType.FIT_CENTER);
        img.setScaleX(0.7f);
        img.setScaleY(0.7f);

        if(em.getVehicle() == null)
        {
            TextViewById(vw, R.id.liPlate).setText("No Vehicle");
            TextViewById(vw, R.id.liMaker).setText("");
        }
        else{
            TextViewById(vw, R.id.liPlate).setText(em.getVehicle().getPlate());
            TextViewById(vw, R.id.liMaker).setText(em.getVehicle().getMake());
        }

        return vw;
    }
}
