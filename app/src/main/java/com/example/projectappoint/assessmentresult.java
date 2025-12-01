package com.example.projectappoint;

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
        finish(); // closes the result page and goes back to the previous activity
    }

    // Book Appointment button
    public void onBookAppointmentClicked(android.view.View view) {
        Intent intent = new Intent(this, pt_Appoint_MainActivity.class);
        // Clear the back stack so pressing back won't return to assessmentresult
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // optional, closes the current activity
    }
}
