package com.example.princ.homework03;
/*
  Author: Sujanth Babu Guntupalli
*/

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class StatsActivity extends AppCompatActivity {

    ProgressBar pcProgressBar;
    Button quitButton2,tryAgainButton;
    TextView tryTV,pcTV;
    int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("state", "onCreate: "+"Stats Activity started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        setTitle(R.string.TriviaQuiz);

        pcProgressBar=(ProgressBar) findViewById(R.id.pcProgressBar);
        quitButton2=(Button) findViewById(R.id.quitButton2);
        tryAgainButton=(Button) findViewById(R.id.tryAgainButton);
        tryTV=(TextView) findViewById(R.id.tryTV);
        pcTV=(TextView) findViewById(R.id.pcTV);
        //pcProgressBar.setProgress(45);

        if(getIntent()!=null && getIntent().getExtras()!=null){
            if(getIntent().getExtras().containsKey("correctCount")){
                Integer count1=(Integer) getIntent().getExtras().getSerializable("correctCount");
                count=(count1.intValue()*100)/16;
                pcProgressBar.setProgress(count);
                pcTV.setText(String.format("%d%s",count,"%"));
                if(count==100){
                        tryTV.setText("Good Job!");
                }
            }
        }

        quitButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(StatsActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("state", "onDestroy: "+"Stats Activity ended");
    }
}
