package com.android.weni.pahlawan.pahlawan.ui.quiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.weni.pahlawan.pahlawan.R;

public class ResultQuizActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mRepeatQuizButton, mCloseQuizButton;
    private RatingBar mBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_quiz);

        mRepeatQuizButton = (Button) findViewById(R.id.repeat_quiz_button);
        mCloseQuizButton = (Button) findViewById(R.id.close_quiz_button);
        mBar = (RatingBar) findViewById(R.id.ratingBar1);
        mBar.setNumStars(3);
        mBar.setStepSize(0.5f);
        //get text view
        TextView t = (TextView) findViewById(R.id.textResult2);
        //get score
        Bundle b = getIntent().getExtras();
        int score = b.getInt("score");
        //display score
        mBar.setRating(score);
        switch (score) {
            case 1:
                t.setText("1");
                break;
            case 2:
                t.setText("2");
                break;
//            case 3:
            case 3:
                t.setText("3");
                break;
//            case 4:t.setText("Who are you? A student in JP???");
//                break;
        }

        mRepeatQuizButton.setOnClickListener(this);
        mCloseQuizButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.repeat_quiz_button) {
            startActivity(new Intent(this, QuestionActivity.class));
            finish();
        } else if (id == R.id.close_quiz_button) {
            startActivity(new Intent(this, QuizActivity.class));
            finish();
        }
    }
}
