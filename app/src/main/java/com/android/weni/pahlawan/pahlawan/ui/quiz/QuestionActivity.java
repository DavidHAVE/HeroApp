package com.android.weni.pahlawan.pahlawan.ui.quiz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.weni.pahlawan.pahlawan.R;
import com.android.weni.pahlawan.pahlawan.helper.FirebaseHandler;
import com.android.weni.pahlawan.pahlawan.object.Quiz;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener {

    final int MSG_START_TIMER = 0;
    final int MSG_STOP_TIMER = 1;
    final int MSG_UPDATE_TIMER = 2;
    final int MSG_PAUSE_TIMER = 3;
    final int MSG_RESUME_TIMER = 4;
    List<Quiz> quesList;
    int score = 0;
    int qid = 1;
    Quiz currentQ;
    TextView txtQuestion;
    RadioButton rda, rdb, rdc, rdd;
    Button butNext;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    int secs, secsFinal;
    int mins, milliseconds;
    FirebaseAuth mAuth;
    private TextView mTimeTextView, mScoreTextView;
    private boolean isResult = false;
    private Handler customHandler = new Handler();
    private long startTime = 0L;
    private String number, question, point1, point2, point3,
            point4, answer;
    // Firebase instance variables
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser user;
    private String userId;
    private FirebaseHandler firebaseHandler;

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            secs = (int) (updatedTime / 1000);
            mins = secs / 60;
            secsFinal = secs % 60;
//            milliseconds = (int) (updatedTime % 1000);
//            timerValue.setText("" + mins + ":"
//                    + String.format("%02d", secs) + ":"
//                    + String.format("%03d", milliseconds));
            mHandler.sendEmptyMessage(MSG_UPDATE_TIMER);
            customHandler.postDelayed(this, 1000);
        }
    };

    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_START_TIMER:
                    startTime = SystemClock.uptimeMillis();
                    customHandler.postDelayed(updateTimerThread, 0);
                    break;
                case MSG_UPDATE_TIMER:
                    //  String duration = String.format("%02d", secs);
                    //mDurationTextView.setText(duration);
//                    String timer = "" + mins + ":"
//                            + String.format("%02d", secsFinal) + ":"
//                            + String.format("%03d", milliseconds);
                    String timer = "" + mins + ":"
                            + String.format("%02d", secsFinal);
                    mTimeTextView.setText(timer);
                    if (secsFinal == 20 && isResult == false) {
//                        mHandler.sendEmptyMessage(MSG_STOP_TIMER);
                        isResult = true;
                        Intent intent = new Intent(QuestionActivity.this, ResultQuizActivity.class);
                        Bundle b = new Bundle();
                        b.putInt("score", score); //Your score
                        intent.putExtras(b); //Put your score to your next Intent
                        startActivity(intent);
                        finish();
                    }
                    break;
                case MSG_PAUSE_TIMER:
                    timeSwapBuff += timeInMilliseconds;
                    customHandler.removeCallbacks(updateTimerThread);
                    break;
                case MSG_RESUME_TIMER:
                    startTime = SystemClock.uptimeMillis();
                    customHandler.postDelayed(updateTimerThread, 0);
                    break;
                case MSG_STOP_TIMER:
                    timeSwapBuff = 0;
                    customHandler.removeCallbacks(updateTimerThread);
//                    mDurationTextView.setText("00");
                    break;
                default:
                    break;
            }
        }
    };

    private ProgressDialog loadingDialog;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("GalleryActivity", "Handler");
            setQuestionView();
//            saveTips();
            //mFoodAdapter.notifyDataSetChanged();
//            loadingDialog.dismiss();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        if (isNetworkConnected()) {
            //Initialize Firebase Components
            mAuth = FirebaseAuth.getInstance();
            firebaseHandler = new FirebaseHandler();
        }

        mTimeTextView = (TextView) findViewById(R.id.time_text_view);
        mScoreTextView = (TextView) findViewById(R.id.score_text_view);
        txtQuestion = (TextView) findViewById(R.id.textView1);
        rda = (RadioButton) findViewById(R.id.radio0);
        rdb = (RadioButton) findViewById(R.id.radio1);
        rdc = (RadioButton) findViewById(R.id.radio2);
        rdd = (RadioButton) findViewById(R.id.radio3);
        butNext = (Button) findViewById(R.id.button1);

        startTread();
//        attachDatabaseQuestionReadListener();

        butNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.button1) {
            RadioGroup grp = (RadioGroup) findViewById(R.id.radioGroup1);
            RadioButton answerRadioButton = (RadioButton) findViewById(grp.getCheckedRadioButtonId());
            Log.d("yourans", answer + " " + answerRadioButton.getText());
            qid++;
            if (answer.equals(answerRadioButton.getText())) {
                score++;
                Log.d("score", "Your score" + score);
            }
            if (qid < 4) {
//                currentQ = quesList.get(qid);
                startTread();
//                setQuestionView();
            } else {
                Intent intent = new Intent(QuestionActivity.this, ResultQuizActivity.class);
                Bundle b = new Bundle();
                b.putInt("score", score); //Your score
                intent.putExtras(b); //Put your score to your next Intent
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isNetworkConnected()) {
            signInAnonymously();
        }
        mHandler.sendEmptyMessage(MSG_START_TIMER);
    }

    @Override
    protected void onPause() {
        super.onPause();
        detachDatabaseQuizReadListener();
        mHandler.sendEmptyMessage(MSG_PAUSE_TIMER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.sendEmptyMessage(MSG_RESUME_TIMER);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mHandler.sendEmptyMessage(MSG_STOP_TIMER);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null;
    }

    public void startTread() {
        loadingDialog = new ProgressDialog(QuestionActivity.this);
        loadingDialog.setMessage("Please Wait...");
        loadingDialog.show();
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
//                    baseList.clear();
//                    heroesLists.clear();
//                    attachTipDatabaseReadListener();
//                    questionRead();
                    detachDatabaseQuizReadListener();
                    attachDatabaseQuestionReadListener();
                    Log.e("QuestionActivity", "startTread");
//                    Log.e("GalleryActivity", "startTread");
                    //readTip();
                    sleep(1000);
                    handler.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // do your stuff
            }
        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e("GalleryActivity", "signInAnonymously:FAILURE", exception);
                    }
                });
    }

    private void attachDatabaseQuestionReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Quiz quiz = dataSnapshot.getValue(Quiz.class);
//                    String uid = newWeight.getUid();

                    number = quiz.getNumber();
                    question = quiz.getQuestion();
                    point1 = quiz.getPoint1();
                    point2 = quiz.getPoint2();
                    point3 = quiz.getPoint3();
                    point4 = quiz.getPoint4();
                    answer = quiz.getAnswer();

                    setQuestionView();

                    Log.e("QuestionActivity", "number :" + number + ", question  :" + question);

//                    if (newWeight.getNewWeight() != 0 && newWeight.getNewDate() != null) {
////                        int newWeightVar = newWeight.getNewWeight();
////                        newWeightLists.add(newWeightVar);
//
//                        int weight = newWeight.getNewWeight();
//
//                        String newDate = newWeight.getNewDate();
//                        DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
//
//                        try {
//                            date = format.parse(newDate);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                        newDateList.add(date);
//
//                        dateList.add(newDate);
//                        weightList.add(weight);
//                        Log.e("ProgGoalActivity", "dateListssss True ");
//                    }
//
//                    Log.e("ProgGoalActivity", "dateListssss = " + newDateList);
//                    mNewWeightAdapter.add(newWeight);

//                    for (int i = 0; i<newWeightLists.size();i++){
//                        totalWeight = newWeightLists.get(i);
//                    }


//                    //progressWeight
//                    int rentangBeratInt = (int) rentangBerat;
//                    mWeightProgressBar.setMax(rentangBeratInt);
//                    mWeightProgressBar.setProgress(totalWeight);
//
//                    //progressTime
//                    int rentangWaktuInt = (int) rentangWaktu;
//                    mTimeProgressBar.setMax(rentangWaktuInt);
//                    mTimeProgressBar.setProgress(4);
//
//                   Log.e("ProgWeight", "Rentang WaktuInt =" + rentangWaktuInt + " RentangBeratInt = " + rentangBeratInt);

                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Log.e("ProgGoalActivity", "onChanged");
                }

                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Log.e("ProgGoalActivity", "onRemoved");
                }

                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    Log.e("ProgGoalActivity", "onMoved");
                }

                public void onCancelled(DatabaseError databaseError) {
                    Log.e("ProgGoalActivity", "onCancel");
                }
            };
            firebaseHandler.getRefQuiz().orderByChild("number").equalTo(String.valueOf(qid)).addChildEventListener(mChildEventListener);
        }
    }

    private void detachDatabaseQuizReadListener() {
        if (mChildEventListener != null) {
            firebaseHandler.getRefQuiz().removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

    private void setQuestionView() {
        mScoreTextView.setText(String.valueOf(score));
        txtQuestion.setText(question);
        rda.setText(point1);
        rdb.setText(point2);
        rdc.setText(point3);
        rdd.setText(point4);
        loadingDialog.dismiss();
//        qid++;
    }
}
