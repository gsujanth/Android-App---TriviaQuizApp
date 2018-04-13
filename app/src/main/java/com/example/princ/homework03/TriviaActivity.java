package com.example.princ.homework03;
/*
  Author: Sujanth Babu Guntupalli
*/

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TriviaActivity extends AppCompatActivity implements GetImageTask.IImage {

    Button nextButton, quitButton;
    TextView qNoTV, questionTV, timerTV, loadImageTV;
    ImageView iv;
    ProgressBar imgProgressBar;
    RadioGroup radioGroup;
    RadioGroup.LayoutParams rgLPs;
    ArrayList<Question> triviaQuestions = new ArrayList<>();
    int questionId = 0;
    public static final int REQ_CODE=1001;
    CountDownTimer cdt;
    int correctCounter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("state", "onCreate: "+"Trivia Activity started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);
        setTitle(R.string.TriviaQuiz);

        nextButton = (Button) findViewById(R.id.nextButton);
        quitButton = (Button) findViewById(R.id.quitButton);
        qNoTV = (TextView) findViewById(R.id.qnoTV);
        questionTV = (TextView) findViewById(R.id.questionTV);
        timerTV = (TextView) findViewById(R.id.timerTV);
        loadImageTV = (TextView) findViewById(R.id.loadImageTV);
        iv = (ImageView) findViewById(R.id.imageView);
        imgProgressBar = (ProgressBar) findViewById(R.id.imageProgressBar);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        cdt=new CountDownTimer(120000, 1000) {

            public void onTick(long millisUntilFinished) {
                timerTV.setText(String.format("Time Left: %d seconds", millisUntilFinished / 1000));
            }

            public void onFinish() {
                if(radioGroup.getCheckedRadioButtonId()==Integer.parseInt(triviaQuestions.get(questionId).getAnswerIndex())){
                    correctCounter+=1;
                }
                timerTV.setText("done!");
                Intent intent = new Intent(TriviaActivity.this, StatsActivity.class);
                Log.d("count", "onTimerFinish: "+correctCounter);
                intent.putExtra("correctCount",correctCounter);
                startActivityForResult(intent,REQ_CODE);
            }
        };

        cdt.start();

        if (getIntent() != null && getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey("questionsList")) {
                triviaQuestions.clear();
                radioGroup.clearCheck();
                radioGroup.removeAllViews();
                triviaQuestions = (ArrayList<Question>) getIntent().getExtras().getSerializable("questionsList");
                if (isConnected()) {
                    new GetImageTask(TriviaActivity.this).execute(triviaQuestions.get(questionId).getImageURL());
                } else {
                    Toast.makeText(this, "Internet Disconnected", Toast.LENGTH_SHORT).show();
                }

                qNoTV.setText(String.format("Q%d", Integer.parseInt(triviaQuestions.get(questionId).getQuestionNo()) + 1));
                questionTV.setText(triviaQuestions.get(questionId).getQuestion());
                for (int i = 0; i < triviaQuestions.get(questionId).getQuestionOptions().size(); i++) {
                    RadioButton radioButton = new RadioButton(this);
                    radioButton.setText(triviaQuestions.get(questionId).getQuestionOptions().get(i));
                    radioButton.setId(i);
                    rgLPs = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 75);
                    radioGroup.addView(radioButton, rgLPs);
                }
            }
        }

        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cdt.cancel();
                Intent intent = new Intent(TriviaActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(radioGroup.getCheckedRadioButtonId()==Integer.parseInt(triviaQuestions.get(questionId).getAnswerIndex())){
                    correctCounter+=1;
                }
                questionId += 1;
                Log.d("count", "onNextClick: "+correctCounter);
                loadImageTV.setVisibility(View.VISIBLE);
                imgProgressBar.setVisibility(View.VISIBLE);
                radioGroup.clearCheck();
                radioGroup.removeAllViews();
                iv.setImageBitmap(null);
                if (questionId < triviaQuestions.size()) {
                        if (isConnected()) {
                            new GetImageTask(TriviaActivity.this).execute(triviaQuestions.get(questionId).getImageURL());
                        } else {
                            Toast.makeText(TriviaActivity.this, "Internet Disconnected", Toast.LENGTH_SHORT).show();
                        }
                    qNoTV.setText(String.format("Q%d", Integer.parseInt(triviaQuestions.get(questionId).getQuestionNo()) + 1));
                    questionTV.setText(triviaQuestions.get(questionId).getQuestion());
                    for (int i = 0; i < triviaQuestions.get(questionId).getQuestionOptions().size(); i++) {
                        RadioButton radioButton = new RadioButton(TriviaActivity.this);
                        radioButton.setText(triviaQuestions.get(questionId).getQuestionOptions().get(i));
                        radioButton.setId(i);
                        rgLPs = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 75);
                        radioGroup.addView(radioButton, rgLPs);
                    }
                } else {
                    loadImageTV.setVisibility(View.GONE);
                    imgProgressBar.setVisibility(View.GONE);
                    cdt.cancel();
                    Intent intent = new Intent(TriviaActivity.this, StatsActivity.class);
                    Log.d("count", "onLastNextClick: "+correctCounter);
                    intent.putExtra("correctCount",correctCounter);
                    startActivityForResult(intent,REQ_CODE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        questionId=0;
        correctCounter=0;
        radioGroup.clearCheck();
        radioGroup.removeAllViews();
        cdt.start();
        if(requestCode==REQ_CODE) {
            if (resultCode == RESULT_OK ) {
                if (isConnected()) {
                    new GetImageTask(TriviaActivity.this).execute(triviaQuestions.get(questionId).getImageURL());
                } else {
                    Toast.makeText(this, "Internet Disconnected", Toast.LENGTH_SHORT).show();
                }

                qNoTV.setText(String.format("Q%d", Integer.parseInt(triviaQuestions.get(questionId).getQuestionNo()) + 1));
                questionTV.setText(triviaQuestions.get(questionId).getQuestion());
                for (int i = 0; i < triviaQuestions.get(questionId).getQuestionOptions().size(); i++) {
                    RadioButton radioButton = new RadioButton(this);
                    radioButton.setText(triviaQuestions.get(questionId).getQuestionOptions().get(i));
                    radioButton.setId(i);
                    rgLPs = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 75);
                    radioGroup.addView(radioButton, rgLPs);
                }
            } else {
                Toast.makeText(this, "Click Again to Exit", Toast.LENGTH_LONG).show();
            }
        }
    }
    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }

    @Override
    public void handleBitmap(Bitmap bitmap1) {
        loadImageTV.setVisibility(View.GONE);
        imgProgressBar.setVisibility(View.GONE);
        iv.setImageBitmap(bitmap1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("state", "onDestroy: "+"Trivia Activity ended");
    }
}
