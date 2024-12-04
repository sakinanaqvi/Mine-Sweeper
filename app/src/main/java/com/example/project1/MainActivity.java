package com.example.project1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Handler handler = new Handler();
    private Button flagButton;
    private List<Button> flagged;
    private List<Integer> mines;
    private int counter = 0;
    private boolean isFlagged = false;
    private int pressedNonMines = 0;
    private int totalNonMines = 116;
    private Runnable timerRunnable;
    private int remaining = 4;
    private GridLayout gridLayout;
    private int[][] positions;
    private TextView textTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textTimer = findViewById(R.id.timer);
        positions = new int[12][10]; //assigning the rows and cols
        mines = new ArrayList<>();
        gridLayout = findViewById(R.id.gridLayout);
        flagged = new ArrayList<>();
        flagButton = findViewById(R.id.flagButton);

        startTimer();
        randomSpots();
        makeGrid();

        flagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFlagged = !isFlagged;
                if(flagButton.getText() == "Build") flagButton.setText("Flag");
                else {
                    flagButton.setText("Build");
                }
            }
        });
    }

    private void startTimer() {
        //have it run
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                counter++;
                textTimer.setText("Timer: " + counter);
                handler.postDelayed(this, 1000); // Update every 1 second
            }
        };
        handler.post(timerRunnable);
    }
    private void randomSpots() {
        //assign random spots
        List<Integer> allVals = new ArrayList<>();
        for(int i = 0; i < 120; i++) allVals.add(i);

        //randomize the indices order
        Collections.shuffle(allVals);
        for(int i = 0; i < 3; i++) {
            int curr = allVals.get(i);
            mines.add(allVals.get(i));
            //need to mark the position of the mine
            positions[curr / 10][curr % 10] = 1;

        }
    }
    // Create the 12x10 grid of buttons and assign random values to the buttons
    private void makeGrid() {
        int vals = 0;
        for (int row = 0; row < 12; row++) {
            for (int col = 0; col < 10; col++) {
                Button button = new Button(this);
                final int rows = row, cols = col;
                button.setBackgroundColor(Color.GREEN);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isFlagged == true) {
                            //change the color based on if flag is on or off
                            flagButton(button);
                            button.setEnabled(false);

                            //ensure this button is also counted as pressed
                            pressedNonMines++;
                            int near = countNear(rows, cols);
                            button.setText(String.valueOf(near));
                            button.setBackgroundColor(Color.YELLOW);
                            if (remaining == 0) {
                                isFlagged = false;
                                flagButton.setVisibility(View.GONE);
                            }
                        } else {
                            if (positions[rows][cols] == 1) {
                                Intent inte = new Intent(MainActivity.this, LandingPage.class);
                                inte.putExtra("TIMER_VALUE", counter);
                                inte.putExtra("MESSAGE_KEY", "You lose!");
                                startActivity(inte);
                            } else {
                                if (!button.isEnabled()) {
                                    return;
                                }
                                int near = countNear(rows, cols);
                                button.setText(String.valueOf(near));
                                button.setBackgroundColor(Color.WHITE);
                                button.setEnabled(false);

                                pressedNonMines++;
                                if (pressedNonMines == totalNonMines) {
                                    Intent inte = new Intent(MainActivity.this, LandingPage.class);
                                    inte.putExtra("TIMER_VALUE", counter);
                                    inte.putExtra("MESSAGE_KEY", "You win!");
                                    startActivity(inte);
                                }
                            }
                        }
                    }
                });

                // Set GridLayout parameters to fill buttons properly
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.rowSpec = GridLayout.spec(row);
                params.columnSpec = GridLayout.spec(col);
                params.width = 0;
                params.height = 0;
                params.setMargins(4, 4, 4, 4);  // Set margins to avoid overlap

                // Set button layout to expand evenly in the grid
                params.rowSpec = GridLayout.spec(row, 1f);
                params.columnSpec = GridLayout.spec(col, 1f);
                button.setLayoutParams(params);

                // Add button to the grid layout
                gridLayout.addView(button);
                vals++;
            }
        }
    }

    private int countNear(int r, int c) {
        int counter = 0;
        //check near the rows and cols for the adjacent
        for (int i = r - 1; i <= r + 1; i++) {
            for (int j = c - 1; j <= c + 1; j++) {
                // Ensure that we are within the grid bounds
                if (i >= 0 && i < 12 && j >= 0 && j < 10) {
                    if (positions[i][j] == 1) {  // Check if there's a mine
                        counter++;
                    }
                }
            }
        }
        return counter;
    }
    //used to update flag amount on the site
    private void flagButton(Button button) {
        if (remaining > 0 && !flagged.contains(button)) {
            button.setBackgroundColor(Color.YELLOW);
            flagged.add(button);
            remaining--;

            TextView flagsText = findViewById(R.id.flags);
            flagsText.setText("Flags: " + remaining + " ");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(timerRunnable); // Stop the timer when the activity is destroyed
    }
}
