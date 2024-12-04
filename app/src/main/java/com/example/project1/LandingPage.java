package com.example.project1;

import android.content.Intent;
import android.os.Bundle;
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
import java.util.ArrayList;

public class LandingPage extends AppCompatActivity{
    private TextView outcome;
    private Button playAgain;
    private TextView timeElapsed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        outcome = findViewById(R.id.outcome);
        playAgain = findViewById(R.id.playAgain);
        timeElapsed = findViewById(R.id.timeElapse);

        int timeVal = getIntent().getIntExtra("TIMER_VALUE",0);
        timeElapsed.setText("Used time " + timeVal + " seconds.");

        String outcomeVal = getIntent().getStringExtra("MESSAGE_KEY");
        if (outcomeVal == null) {
            outcomeVal = "No message available";
        }
        outcome.setText(outcomeVal);
        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent(LandingPage.this, MainActivity.class);
                startActivity(inte);
            }
        });
    }
}
