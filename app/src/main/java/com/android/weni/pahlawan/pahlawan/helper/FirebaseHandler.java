package com.android.weni.pahlawan.pahlawan.helper;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by David on 15/11/2017.
 */

public class FirebaseHandler {

//    private static boolean isPersistenceEnabled = false;
    private Context context;
    private String userKey;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mHeroesDatabaseReference;
    private DatabaseReference mHeroesDatabaseReferenceUser;
    private DatabaseReference mQuizDatabaseReference;
    private DatabaseReference mQuizDatabaseReferenceUser;

    public FirebaseHandler() {
        this.context = context;
        this.userKey = userKey;
//        if (!isPersistenceEnabled) {
//            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
//            isPersistenceEnabled = true;
//        }
        mHeroesDatabaseReference = FirebaseDatabase.getInstance().getReference().child("heroes");
        mQuizDatabaseReference = FirebaseDatabase.getInstance().getReference().child("quiz");
    }

    public DatabaseReference getRefHeroes() {
        mHeroesDatabaseReference = FirebaseDatabase.getInstance().getReference().child("heroes");
        return mHeroesDatabaseReference;
    }

    public DatabaseReference getRefHeroesUser() {
        mHeroesDatabaseReferenceUser = FirebaseDatabase.getInstance().getReference().child("heroes");
        return mHeroesDatabaseReference.child(userKey);
    }

    public DatabaseReference getRefQuiz() {
        mQuizDatabaseReference = FirebaseDatabase.getInstance().getReference().child("quiz");
        return mQuizDatabaseReference;
    }

    public DatabaseReference getRefQuizUser() {
        mQuizDatabaseReferenceUser = FirebaseDatabase.getInstance().getReference().child("quiz");
        return mQuizDatabaseReference.child(userKey);
    }
}
