package com.fopman.mac.quizapp;

/**
 * Created by mac on 2016-12-19.
 */

public class Question {
    private int id;
    private float Point;
    private String Question;
    private String Answer;
    private String Image;
    private String Option1;
    private String Option2;
    private String Option3;
    private String Option4;

    public Question(){
        id=0;
        Point = 1.0f;
        Question="";
        Answer="";
        Image="";
        Option1="";
        Option2="";
        Option3="";
        Option4="";
    }

    public Question(float point, String question, String answer, String image, String op1, String op2, String op3,String op4) {
        Point = point;
        Question = question;
        Answer = answer;
        Image = image;
        Option1 = op1;
        Option2 = op2;
        Option3 = op3;
        Option4 = op4;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getPoint() {
        return this.Point;
    }

    public void setPoint(float point) {
        this.Point = point;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String Question) {
        this.Question = Question;
    }

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String Answer) {
        this.Answer = Answer;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String Image) {
        this.Image = Image;
    }

    public String getOption1() {
        return Option1;
    }

    public void setOption1(String option1) {
        Option1 = option1;
    }

    public String getOption2() {
        return Option2;
    }

    public void setOption2(String option2) {
        Option2 = option2;
    }

    public String getOption3() {
        return Option3;
    }

    public void setOption3(String option3) {
        Option3 = option3;
    }

    public String getOption4() {
        return Option4;
    }

    public void setOption4(String option4) {
        Option4 = option4;
    }
}
