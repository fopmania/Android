package com.fopman.mac.quizapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fopman.mac.quizapp.JSONParser;
import com.fopman.mac.quizapp.Question;
import com.fopman.mac.quizapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 2016-12-19.
 */

public class DBAdapter extends SQLiteOpenHelper {
    private SQLiteDatabase myDB;

    private String fileName = "";
    private static final int    DB_VER = 1;
    private static final String DB_NAME = "JJQuiz";

    private String TABLE_QUESTION = "";

    private static final String KEY_ID = "id";
    private static final String KEY_QUESION = "question";
    private static final String KEY_ANSWER = "answer"; //correct option
    private static final String KEY_POINT = "point"; //correct option
    private static final String KEY_IMAGE = "image"; //correct option
    private static final String KEY_OPT1= "opt1";
    private static final String KEY_OPT2= "opt2";
    private static final String KEY_OPT3= "opt3";
    private static final String KEY_OPT4= "opt4";

    private Context context;


    public DBAdapter(Context context, String fileName){
        super(context, DB_NAME, null, DB_VER);
        this.context = context;
        this.fileName = fileName;
        String [] arrStr = fileName.split("\\.");
        TABLE_QUESTION = arrStr[0];
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        myDB = sqLiteDatabase;
        if(isTableExists(TABLE_QUESTION) == false){
            makeTable();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        myDB.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION);

        onCreate(sqLiteDatabase);
    }

    public boolean isTableExists(String tableName) {
        return isTableExists(tableName, false);
    }

    public boolean isTableExists(String tableName, boolean openDb) {
        if(openDb) {
            if(myDB == null || !myDB.isOpen()) {
                myDB = getReadableDatabase();
            }

            if(!myDB.isReadOnly()) {
                myDB.close();
                myDB = getReadableDatabase();
            }
        }
        else{
            if(myDB == null)    return false;
        }

        Cursor cursor = myDB.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    void makeTable(){
        String sql = "CREATE TABLE IF NOT EXISTS "+TABLE_QUESTION+" (" + KEY_ID+
                " INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_QUESION+" TEXT, "
                + KEY_ANSWER+" TEXT, "+ KEY_POINT+" FLOAT, "+ KEY_IMAGE+" TEXT, "+KEY_OPT1 +" TEXT, "
                +KEY_OPT2 +" TEXT, "+KEY_OPT3 +" TEXT, "+KEY_OPT4+" TEXT)";
        myDB.execSQL(sql);
        addQuestions();
    }


    public List<Question> getAllQuestions() {
        myDB = this.getWritableDatabase();

        if(isTableExists(TABLE_QUESTION) == false){
            makeTable();
        }

        List<Question> quesList = new ArrayList<Question>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_QUESTION;
        myDB=this.getReadableDatabase();

        Cursor cursor = myDB.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Question quest = new Question();
                quest.setId(cursor.getInt(0));
                quest.setQuestion(cursor.getString(1));
                quest.setAnswer(cursor.getString(2));
                quest.setPoint(cursor.getFloat(3));
                quest.setImage(cursor.getString(4));
                quest.setOption1(cursor.getString(5));
                quest.setOption2(cursor.getString(6));
                quest.setOption3(cursor.getString(7));
                quest.setOption4(cursor.getString(8));

                quesList.add(quest);

            } while (cursor.moveToNext());
        }
        // return quest list
        return quesList;
    }


    public int rowCount()
    {
        int row=0;
        String selectQuery = "SELECT  * FROM " + TABLE_QUESTION;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        row=cursor.getCount();
        return row;
    }

    public void addQuestion(Question quest) {

        ContentValues values = new ContentValues();
        values.put(KEY_QUESION, quest.getQuestion());
        values.put(KEY_ANSWER, quest.getAnswer());
        values.put(KEY_POINT, quest.getPoint());
        values.put(KEY_IMAGE, quest.getImage());
        values.put(KEY_OPT1, quest.getOption1());
        values.put(KEY_OPT2, quest.getOption2());
        values.put(KEY_OPT3, quest.getOption3());
        values.put(KEY_OPT4, quest.getOption4());

        myDB.insert(TABLE_QUESTION, null, values);
    }

    private void addQuestions()
    {
        JSONParser jp = new JSONParser();
        ArrayList<Question> arrQ = jp.readQuizFromAsset(fileName, context);
        for(int i = 0; i < arrQ.size(); i++){
            this.addQuestion(arrQ.get(i));
        }
    }

}
