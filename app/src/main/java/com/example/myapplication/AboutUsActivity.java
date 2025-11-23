package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class AboutUsActivity extends AppCompatActivity {

    private static final String TAG = "AboutUsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try {
            setContentView(R.layout.aboutus);

            // Handle close button click - goes back to PatientDashboard
            ImageButton btnClose = findViewById(R.id.btnClose);
            if (btnClose != null) {
                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Navigate back to PatientDashboard
                        Intent intent = new Intent(AboutUsActivity.this, PatientDashboard.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finish(); // Close this activity
                    }
                });
            } else {
                Log.e(TAG, "btnClose button not found in layout");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate: " + e.getMessage(), e);
            e.printStackTrace();
            finish(); // Close activity if there's an error
        }
    }
}

