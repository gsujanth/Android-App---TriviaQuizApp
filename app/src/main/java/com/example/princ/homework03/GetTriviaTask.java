package com.example.princ.homework03;
/*
  Author: Sujanth Babu Guntupalli
*/

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class GetTriviaTask extends AsyncTask<String,Void,ArrayList<Question>>{

    ITrivia iTrivia;

    public GetTriviaTask(ITrivia iTrivia) {
        this.iTrivia = iTrivia;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected ArrayList<Question> doInBackground(String... strings) {

        HttpURLConnection con = null;
        BufferedReader reader=null;
        String questionNo,answerIndex="";
        String question,questionImageURL="";
        ArrayList<Question> questionList=new ArrayList<>();

        for(int i=0;i<100;i++){
            for(int j=0;j<100000;j++){

            }
        }

        try {

            URL url = new URL(strings[0]);
            con = (HttpURLConnection) url.openConnection();
            con.connect();
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    String[] triviaQuestion = line.split(";");
                    questionNo = triviaQuestion[0];
                    question = triviaQuestion[1];
                    questionImageURL=triviaQuestion[2];
                    answerIndex = triviaQuestion[triviaQuestion.length - 1];
                    ArrayList<String> questionOptions=new ArrayList<>();
                    for (int i = 3; i < triviaQuestion.length - 1; i++) {
                        questionOptions.add(triviaQuestion[i]);
                    }
                    Question q = new Question(questionNo, question, questionImageURL, questionOptions, answerIndex);
                    questionList.add(q);

                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        return questionList;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(ArrayList<Question> arrayList) {
        iTrivia.handleQuestions(arrayList);
    }

    public static interface ITrivia{
            public void handleQuestions(ArrayList<Question> qList);
    }
}
