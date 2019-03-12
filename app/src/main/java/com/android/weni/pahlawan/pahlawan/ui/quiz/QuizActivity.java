package com.android.weni.pahlawan.pahlawan.ui.quiz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.weni.pahlawan.pahlawan.R;
import com.android.weni.pahlawan.pahlawan.helper.FirebaseHandler;
import com.android.weni.pahlawan.pahlawan.ui.gallery.GalleryActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mReadyQuizButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        mReadyQuizButton = (Button) findViewById(R.id.readyQuizButton);

        mReadyQuizButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.readyQuizButton) {
            Intent intent = new Intent(this, QuestionActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
