package com.android.weni.pahlawan.pahlawan.ui.puzzle;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Toast;

import com.android.weni.pahlawan.pahlawan.R;
import com.android.weni.pahlawan.pahlawan.adapter.CustomPuzzleAdapter;
import com.android.weni.pahlawan.pahlawan.helper.Constants;
import com.android.weni.pahlawan.pahlawan.helper.GestureDetectGridView;

import java.util.ArrayList;
import java.util.Random;

public class PuzzlePlayActivity extends AppCompatActivity {

    public static final String up = "up";
    public static final String down = "down";
    public static final String left = "left";
    public static final String right = "right";
    private static final int COLUMNS = 3;
    private static final int DIMENSIONS = COLUMNS * COLUMNS;
    private static GestureDetectGridView mGridView;
    private static int mColumnWidth, mColumnHeight;
    private static String[] tileList;

    private static String imageName;

    private static void display(Context context) {
        ArrayList<Button> buttons = new ArrayList<>();
        Button button;

        if (imageName.equals("Soekarno")) {
            for (int i = 0; i < tileList.length; i++) {
                button = new Button(context);

                if (tileList[i].equals("0"))
                    button.setBackgroundResource(R.drawable.s1);
                else if (tileList[i].equals("1"))
                    button.setBackgroundResource(R.drawable.s2);
                else if (tileList[i].equals("2"))
                    button.setBackgroundResource(R.drawable.s3);
                else if (tileList[i].equals("3"))
                    button.setBackgroundResource(R.drawable.s4);
                else if (tileList[i].equals("4"))
                    button.setBackgroundResource(R.drawable.s5);
                else if (tileList[i].equals("5"))
                    button.setBackgroundResource(R.drawable.s6);
                else if (tileList[i].equals("6"))
                    button.setBackgroundResource(R.drawable.s7);
                else if (tileList[i].equals("7"))
                    button.setBackgroundResource(R.drawable.s8);
                else if (tileList[i].equals("8"))
                    button.setBackgroundResource(R.drawable.s9);

                buttons.add(button);
            }
        } else if (imageName.equals("Diponegoro")) {
            for (int i = 0; i < tileList.length; i++) {
                button = new Button(context);

                if (tileList[i].equals("0"))
                    button.setBackgroundResource(R.drawable.d1);
                else if (tileList[i].equals("1"))
                    button.setBackgroundResource(R.drawable.d2);
                else if (tileList[i].equals("2"))
                    button.setBackgroundResource(R.drawable.d3);
                else if (tileList[i].equals("3"))
                    button.setBackgroundResource(R.drawable.d4);
                else if (tileList[i].equals("4"))
                    button.setBackgroundResource(R.drawable.d5);
                else if (tileList[i].equals("5"))
                    button.setBackgroundResource(R.drawable.d6);
                else if (tileList[i].equals("6"))
                    button.setBackgroundResource(R.drawable.d7);
                else if (tileList[i].equals("7"))
                    button.setBackgroundResource(R.drawable.d8);
                else if (tileList[i].equals("8"))
                    button.setBackgroundResource(R.drawable.d9);

                buttons.add(button);
            }
        } else if (imageName.equals("Kartini")) {
            for (int i = 0; i < tileList.length; i++) {
                button = new Button(context);

                if (tileList[i].equals("0"))
                    button.setBackgroundResource(R.drawable.k1);
                else if (tileList[i].equals("1"))
                    button.setBackgroundResource(R.drawable.k2);
                else if (tileList[i].equals("2"))
                    button.setBackgroundResource(R.drawable.k3);
                else if (tileList[i].equals("3"))
                    button.setBackgroundResource(R.drawable.k4);
                else if (tileList[i].equals("4"))
                    button.setBackgroundResource(R.drawable.k5);
                else if (tileList[i].equals("5"))
                    button.setBackgroundResource(R.drawable.k6);
                else if (tileList[i].equals("6"))
                    button.setBackgroundResource(R.drawable.k7);
                else if (tileList[i].equals("7"))
                    button.setBackgroundResource(R.drawable.k8);
                else if (tileList[i].equals("8"))
                    button.setBackgroundResource(R.drawable.k9);

                buttons.add(button);
            }
        }

        mGridView.setAdapter(new CustomPuzzleAdapter(buttons, mColumnWidth, mColumnHeight));
    }

    private static boolean isSolved() {
        boolean solved = false;

        for (int i = 0; i < tileList.length; i++) {
            if (tileList[i].equals(String.valueOf(i))) {
                solved = true;
            } else {
                solved = false;
                break;
            }
        }

        return solved;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle_soekarno);

        Bundle data = getIntent().getExtras();
        if (data != null) {
            imageName = data.getString(Constants.IMAGE_NAME);
        }

        init();

        scramble();

        setDimensions();
    }

    private void init() {
        mGridView = (GestureDetectGridView) findViewById(R.id.grid);
        mGridView.setNumColumns(COLUMNS);

        tileList = new String[DIMENSIONS];
        for (int i = 0; i < DIMENSIONS; i++) {
            tileList[i] = String.valueOf(i);
            Log.e("PuzzlePlayActivity", "tileList :"+tileList[i]);
        }
    }

    private void scramble() {
        int index;
        String temp;
        Random random = new Random();

        for (int i = tileList.length - 1; i > 0; i--) {
            index = random.nextInt(i + 1);
            temp = tileList[index];
            tileList[index] = tileList[i];
            tileList[i] = temp;
            Log.e("PuzzleActivity", "index :"+index+", temp:"+temp+", tileList:"+tileList[index]+", tileListp[i]:"+tileList[i]);
        }
    }

    private void setDimensions() {
        ViewTreeObserver vto = mGridView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mGridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int displayWidth = mGridView.getMeasuredWidth();
                int displayHeight = mGridView.getMeasuredHeight();

                int statusbarHeight = getStatusBarHeight(getApplicationContext());
                int requiredHeight = displayHeight - statusbarHeight;

                mColumnWidth = displayWidth / COLUMNS;
                mColumnHeight = requiredHeight / COLUMNS;

                display(getApplicationContext());
            }
        });
    }

    private int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");

        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }

        return result;
    }

    private void swap(Context context, int currentPosition, int swap) {
        String newPosition = tileList[currentPosition + swap];
        tileList[currentPosition + swap] = tileList[currentPosition];
        tileList[currentPosition] = newPosition;
        display(context);

        if (isSolved()) {

            Toast.makeText(context, "YOU WIN!", Toast.LENGTH_SHORT).show();
//          alertDialog();
            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
            builder1.setMessage("Berhasil Menyelesaikan Puzzle.");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Ya",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();

        }
    }

    private void alertDialog() {

    }

    public void moveTiles(Context context, String direction, int position) {

        // Upper-left-corner tile
        if (position == 0) {

            if (direction.equals(right)) swap(context, position, 1);
            else if (direction.equals(down)) swap(context, position, COLUMNS);
            else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

            // Upper-center tiles
        } else if (position > 0 && position < COLUMNS - 1) {
            if (direction.equals(left)) swap(context, position, -1);
            else if (direction.equals(down)) swap(context, position, COLUMNS);
            else if (direction.equals(right)) swap(context, position, 1);
            else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

            // Upper-right-corner tile
        } else if (position == COLUMNS - 1) {
            if (direction.equals(left)) swap(context, position, -1);
            else if (direction.equals(down)) swap(context, position, COLUMNS);
            else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

            // Left-side tiles
        } else if (position > COLUMNS - 1 && position < DIMENSIONS - COLUMNS &&
                position % COLUMNS == 0) {
            if (direction.equals(up)) swap(context, position, -COLUMNS);
            else if (direction.equals(right)) swap(context, position, 1);
            else if (direction.equals(down)) swap(context, position, COLUMNS);
            else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

            // Right-side AND bottom-right-corner tiles
        } else if (position == COLUMNS * 2 - 1 || position == COLUMNS * 3 - 1) {
            if (direction.equals(up)) swap(context, position, -COLUMNS);
            else if (direction.equals(left)) swap(context, position, -1);
            else if (direction.equals(down)) {

                // Tolerates only the right-side tiles to swap downwards as opposed to the bottom-
                // right-corner tile.
                if (position <= DIMENSIONS - COLUMNS - 1) swap(context, position,
                        COLUMNS);
                else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();
            } else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

            // Bottom-left corner tile
        } else if (position == DIMENSIONS - COLUMNS) {
            if (direction.equals(up)) swap(context, position, -COLUMNS);
            else if (direction.equals(right)) swap(context, position, 1);
            else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

            // Bottom-center tiles
        } else if (position < DIMENSIONS - 1 && position > DIMENSIONS - COLUMNS) {
            if (direction.equals(up)) swap(context, position, -COLUMNS);
            else if (direction.equals(left)) swap(context, position, -1);
            else if (direction.equals(right)) swap(context, position, 1);
            else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

            // Center tiles
        } else {
            if (direction.equals(up)) swap(context, position, -COLUMNS);
            else if (direction.equals(left)) swap(context, position, -1);
            else if (direction.equals(right)) swap(context, position, 1);
            else swap(context, position, COLUMNS);
        }
    }
}
