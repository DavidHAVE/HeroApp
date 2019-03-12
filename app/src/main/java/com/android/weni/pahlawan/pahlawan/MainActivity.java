package com.android.weni.pahlawan.pahlawan;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.weni.pahlawan.pahlawan.ui.AboutActivity;
import com.android.weni.pahlawan.pahlawan.ui.HelpActivity;
import com.android.weni.pahlawan.pahlawan.ui.gallery.GalleryActivity;
import com.android.weni.pahlawan.pahlawan.ui.puzzle.PuzzleMenuActivity;
import com.android.weni.pahlawan.pahlawan.ui.quiz.QuizActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button mGaleryButton, mPuzzleButton, mQuizButton, mExitButton, mHelpButton, mAboutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGaleryButton = (Button) findViewById(R.id.galery_button);
        mPuzzleButton = (Button) findViewById(R.id.puzzle_button);
        mQuizButton = (Button) findViewById(R.id.quiz_button);
        mExitButton = (Button) findViewById(R.id.exit_button);
        mHelpButton = (Button) findViewById(R.id.help_button);
        mAboutButton = (Button) findViewById(R.id.about_button);

        mGaleryButton.setOnClickListener(this);
        mPuzzleButton.setOnClickListener(this);
        mQuizButton.setOnClickListener(this);
        mExitButton.setOnClickListener(this);
        mHelpButton.setOnClickListener(this);
        mAboutButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.galery_button) {
            Intent i = new Intent(this, GalleryActivity.class);
            startActivity(i);
        } else if (id == R.id.puzzle_button) {
            Intent i = new Intent(this, PuzzleMenuActivity.class);
            startActivity(i);
        } else if (id == R.id.quiz_button) {
            Intent i = new Intent(this, QuizActivity.class);
            startActivity(i);
        } else if (id == R.id.exit_button) {
            closeDialog();
        } else if (id == R.id.help_button) {
            Intent i = new Intent(this, HelpActivity.class);
            startActivity(i);
        } else if (id == R.id.about_button) {
            Intent i = new Intent(this, AboutActivity.class);
            startActivity(i);
        }
    }

    @Override
    public void onBackPressed() {
        closeDialog();
    }

    private void closeDialog() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Keluar Aplikasi")
                .setMessage("Apakah mau keluar Aplikasi?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("Tidak", null)
                .show();
    }
}
