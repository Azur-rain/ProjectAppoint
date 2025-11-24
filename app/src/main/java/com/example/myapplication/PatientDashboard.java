package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class PatientDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patientdashboard);


        View root = findViewById(R.id.rootDashboard);


        root.setOnApplyWindowInsetsListener((v, insets) -> {
            int bottomInset = insets.getSystemWindowInsetBottom();


            v.setPadding(
                    v.getPaddingLeft(),
                    v.getPaddingTop(),
                    v.getPaddingRight(),
                    bottomInset
            );

            return insets.consumeSystemWindowInsets();
        });

        // Set up About Us button click listener
        View btnAbout = findViewById(R.id.btnAbout);
        if (btnAbout != null) {
            btnAbout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PatientDashboard.this, AboutUsActivity.class);
                    startActivity(intent);
                }
            });
        }
        View btnAssessment = findViewById(R.id.btnDash);
        if (btnAssessment != null) {
            btnAssessment.setOnClickListener(v -> {
                Intent intent = new Intent(PatientDashboard.this, assessment.class);
                startActivity(intent);
            });
        }
    }


    // Handle image clicks (referenced in XML layout)
    public void onImageClick(View view) {
        // Get the parent LinearLayout to handle the click
        View parent = (View) view.getParent();
        if (parent != null) {
            parent.performClick();
        }
    }
}
