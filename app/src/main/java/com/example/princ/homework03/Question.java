package com.example.princ.homework03;
/*
  Author: Sujanth Babu Guntupalli
*/

import java.io.Serializable;
import java.util.ArrayList;

public class Question implements Serializable{

    private String questionNo,question,imageURL,answerIndex;
    private ArrayList<String> questionOptions=new ArrayList<>();

    public Question(String questionNo, String question, String imageURL, ArrayList<String> questionOptions, String answerIndex) {
        this.questionNo = questionNo;
        this.question = question;
        this.imageURL = imageURL;
        this.questionOptions = questionOptions;
        this.answerIndex = answerIndex;
    }

    public String getQuestionNo() {
        return questionNo;
    }

    public void setQuestionNo(String questionNo) {
        this.questionNo = questionNo;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public ArrayList<String> getQuestionOptions() {
        return questionOptions;
    }

    public void setQuestionOptions(ArrayList<String> questionOptions) {
        this.questionOptions = questionOptions;
    }

    public String getAnswerIndex() {
        return answerIndex;
    }

    public void setAnswerIndex(String answerIndex) {
        this.answerIndex = answerIndex;
    }
}
