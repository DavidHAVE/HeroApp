package com.android.weni.pahlawan.pahlawan.ui.puzzle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.android.weni.pahlawan.pahlawan.R;
import com.android.weni.pahlawan.pahlawan.helper.Constants;

public class PuzzleMenuActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout mTipsDietLayout, mFoodCalorieLayout, mDietMenuLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle_menu);

        mTipsDietLayout = (LinearLayout) findViewById(R.id.tipsDietLayout);
        mFoodCalorieLayout = (LinearLayout) findViewById(R.id.foodCalorieLayout);
        mDietMenuLayout = (LinearLayout) findViewById(R.id.dietMenuLayout);

        mTipsDietLayout.setOnClickListener(this);
        mFoodCalorieLayout.setOnClickListener(this);
        mDietMenuLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mTipsDietLayout) {
            Intent i = new Intent(this, PuzzlePlayActivity.class);
            i.putExtra(Constants.IMAGE_NAME, "Soekarno");
            startActivity(i);
        } else if (v == mFoodCalorieLayout) {
            Intent i = new Intent(this, PuzzlePlayActivity.class);
            i.putExtra(Constants.IMAGE_NAME, "Diponegoro");
            startActivity(i);
        } else {
            Intent i = new Intent(this, PuzzlePlayActivity.class);
            i.putExtra(Constants.IMAGE_NAME, "Kartini");
            startActivity(i);
        }
    }
}
