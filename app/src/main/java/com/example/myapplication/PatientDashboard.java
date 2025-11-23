package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class PatientDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patientdashboard); // make sure this matches your XML filename

        // Get root view
        View root = findViewById(R.id.rootDashboard);

        // Handle insets
        root.setOnApplyWindowInsetsListener((v, insets) -> {
            int bottomInset = insets.getSystemWindowInsetBottom();

            // Apply padding to the bottom so grid and nav bar don't overlap
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
