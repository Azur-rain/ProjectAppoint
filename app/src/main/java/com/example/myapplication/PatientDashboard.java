package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class PatientDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patientdashboard);

        View root = findViewById(R.id.rootDashboard);

        // Adjust padding for system insets (navigation bar)
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

        // Assessment Test button in dashboard
        View btnAssessment = findViewById(R.id.btnDash);
        if (btnAssessment != null) {
            btnAssessment.setOnClickListener(v ->
                    startActivity(new Intent(PatientDashboard.this, assessment.class))
            );
        }

        // Book Appointment button
        View btnAvail = findViewById(R.id.btnAvail);
        btnAvail.setOnClickListener(v -> {
            // Always fetch the latest flag
            SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
            boolean assessmentCompleted = prefs.getBoolean("assessmentCompleted", false);

            if (!assessmentCompleted) {
                // Assessment not completed → show popup
                showAssessmentPrereqPopup();
            } else {
                // Assessment completed → go to booking
                startActivity(new Intent(PatientDashboard.this, pt_Appoint_MainActivity.class));
            }
        });

    }

    private void showAssessmentPrereqPopup() {
        View popupView = getLayoutInflater().inflate(R.layout.assessmentprerequisite, null, false);

        AlertDialog popupDialog = new AlertDialog.Builder(PatientDashboard.this)
                .setView(popupView)
                .setCancelable(true)
                .create();

        popupDialog.show();

        // Adjust popup width for better display
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(popupDialog.getWindow().getAttributes());
        lp.width = (int)(getResources().getDisplayMetrics().widthPixels * 0.85);
        popupDialog.getWindow().setAttributes(lp);

        // Take Test button
        AppCompatButton btnTakeTest = popupView.findViewById(R.id.btn_take_test);
        btnTakeTest.setOnClickListener(bt -> {
            startActivity(new Intent(PatientDashboard.this, assessment.class));
            popupDialog.dismiss();
        });

        // Cancel button
        AppCompatButton btnCancel = popupView.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(bc -> popupDialog.dismiss());
    }

    // Handle image clicks referenced in XML
    public void onImageClick(View view) {
        View parent = (View) view.getParent();
        if (parent != null) {
            parent.performClick();
        }
    }
}
