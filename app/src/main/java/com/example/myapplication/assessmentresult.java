package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class assessmentresult extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assessmentresult);

        // Receive totalScore from Intent
        Intent intent = getIntent();
        int totalScore = intent.getIntExtra("totalScore", -1);

        // Get references to the table TextViews
        TextView scaleScoreText = findViewById(R.id.scalescore);
        TextView diagnosisText = findViewById(R.id.diagnosis);

        // Display the total score
        scaleScoreText.setText(String.valueOf(totalScore));

        // Determine diagnosis based on PHQ-9 scoring
        String diagnosis;
        if (totalScore >= 0 && totalScore <= 4) {
            diagnosis = "Minimal Symptoms";
        } else if (totalScore <= 9) {
            diagnosis = "Mild Symptoms";
        } else if (totalScore <= 14) {
            diagnosis = "Moderate Symptoms";
        } else if (totalScore <= 19) {
            diagnosis = "Moderately Severe Symptoms";
        } else {
            diagnosis = "Severe Symptoms";
        }

        diagnosisText.setText(diagnosis);
    }

    // Back to Home/Dashboard
    public void onHomeClicked(android.view.View view) {
        finish(); // simply closes the result page and goes back to the previous activity
    }

    // Book Appointment button placeholder
    public void onBookAppointmentClicked(android.view.View view) {
        // TODO: Start appointment booking activity
    }
}
