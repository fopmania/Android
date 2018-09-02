package com.fopman.mac.quizapp;

import android.content.Context;
import android.content.res.AssetManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


/**
 * Created by mac on 2016-12-19.
 */

public class JSONParser {
    static InputStream iStream = null;
    static JSONArray    jArray = null;
    static String       fName = "";

    public JSONParser(){
    }

    public static String AssetJSONFile (String filename, Context context){
        AssetManager manager = context.getAssets();

        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;

        try{
            br = new BufferedReader(new InputStreamReader(manager.open(filename)));
            String temp;
            while((temp = br.readLine()) != null){
                sb.append(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try{
                br.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        return sb.toString();
    }


    public ArrayList<Question> readQuizFromAsset(String fn, Context context){
        ArrayList<Question> arrQ = new ArrayList<>();

        try{
            String jsonLocation = AssetJSONFile(fn, context);
            JSONObject jo = new JSONObject(jsonLocation);
            JSONArray ja = jo.getJSONArray("quiz");
            for(int i = 0; i < ja.length(); i++){
                JSONObject obj = ja.getJSONObject(i);

                String question = obj.getString("question");
                String image = obj.getString("image");
                String answer = obj.getString("answer");
                String point = obj.getString("point");
                JSONArray opt = obj.getJSONArray("options");
                String opt1 = opt.getString(0);
                String opt2 = opt.getString(1);
                String opt3 = opt.getString(2);
                String opt4 = opt.getString(3);

                Question Que = new Question(Float.parseFloat(point), question, answer, image, opt1, opt2, opt3, opt4);
                arrQ.add(Que);
            }
        } catch (JSONException e ) {
            e.printStackTrace();
        }

        return arrQ;
    }





}
