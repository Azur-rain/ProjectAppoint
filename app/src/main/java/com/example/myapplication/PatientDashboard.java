package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class PatientDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patientdashboard);

        View root = findViewById(R.id.rootDashboard);

        // Adjust padding for system insets (like navigation bar)
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

        // ----------------- Buttons -----------------

        // About Us button
        View btnAbout = findViewById(R.id.btnAbout);
        if (btnAbout != null) {
            btnAbout.setOnClickListener(v ->
                    startActivity(new Intent(PatientDashboard.this, AboutUsActivity.class))
            );
        }

        // Assessment button
        View btnAssessment = findViewById(R.id.btnDash);
        if (btnAssessment != null) {
            btnAssessment.setOnClickListener(v ->
                    startActivity(new Intent(PatientDashboard.this, assessment.class))
            );
        }

        // Book Appointment button (btnAvail) - directly open appointment activity for now
        View btnAvail = findViewById(R.id.btnAvail);
        btnAvail.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
            boolean assessmentCompleted = prefs.getBoolean("assessmentCompleted", false);

            if (assessmentCompleted) {
                // Go directly to booking
                startActivity(new Intent(PatientDashboard.this, pt_Appoint_MainActivity.class));
            } else {
                // Show popup
                View popupView = getLayoutInflater().inflate(R.layout.assessmentprerequisite, null);

                AlertDialog popupDialog = new AlertDialog.Builder(PatientDashboard.this)
                        .setView(popupView)
                        .setCancelable(true)
                        .create();

                popupDialog.show();

                AppCompatButton btnTakeTest = popupView.findViewById(R.id.btn_take_test);
                btnTakeTest.setOnClickListener(bt -> {
                    startActivity(new Intent(PatientDashboard.this, assessment.class));
                    popupDialog.dismiss();
                });

                AppCompatButton btnCancel = popupView.findViewById(R.id.btn_cancel);
                btnCancel.setOnClickListener(bc -> popupDialog.dismiss());
            }
        });
        }



    // Handle image clicks referenced in XML
    public void onImageClick(View view) {
        View parent = (View) view.getParent();
        if (parent != null) {
            parent.performClick();
        }
    }
}
