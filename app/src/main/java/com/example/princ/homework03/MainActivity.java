package com.example.princ.homework03;
/*
  Author: Sujanth Babu Guntupalli
*/
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GetTriviaTask.ITrivia {

    ArrayList<Question> qList=new ArrayList<>();
    ImageView triviaIV;
    TextView loadingTV,readyTV;
    ProgressBar progressBar;
    Button startTriviaButton,exitButton;
    TriviaActivity t=new TriviaActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("state", "onCreate: "+"Main Activity started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.TriviaQuiz);

        triviaIV=(ImageView) findViewById(R.id.triviaIV);
        loadingTV=(TextView) findViewById(R.id.loadingTV);
        readyTV=(TextView) findViewById(R.id.triviaReadyTV);
        progressBar=(ProgressBar) findViewById(R.id.progressBar);
        startTriviaButton=(Button) findViewById(R.id.startTriviaButton);
        exitButton=(Button) findViewById(R.id.exitButton);

        triviaIV.setVisibility(View.INVISIBLE);
        readyTV.setVisibility(View.INVISIBLE);
        startTriviaButton.setEnabled(false);

        if(isConnected()) {
            new GetTriviaTask(MainActivity.this).execute("http://dev.theappsdr.com/apis/trivia_json/trivia_text.php");
        }else{
            Toast.makeText(this, "Internet Disconnected", Toast.LENGTH_SHORT).show();
        }

        startTriviaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Intent intent=new Intent(MainActivity.this,TriviaActivity.class);
                 intent.putExtra("questionsList",qList);
                 startActivity(intent);
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.finishAffinity(MainActivity.this);
            }
        });

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
    public void handleQuestions(ArrayList<Question> qList1) {
        qList=qList1;
        progressBar.setVisibility(View.GONE);
        loadingTV.setVisibility(View.GONE);
        readyTV.setVisibility(View.VISIBLE);
        triviaIV.setVisibility(View.VISIBLE);
        startTriviaButton.setEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("state", "onDestroy: "+"Main Activity ended");
    }
}
