package com.example.projectappoint;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);

        // ----------------- Views -----------------
        AppCompatEditText editTextEmail = findViewById(R.id.editTextEbar);
        AppCompatButton btnReset = findViewById(R.id.btnReset);
        AppCompatButton btnBack = findViewById(R.id.btnLoginback);
        TextView tvLoginLink = findViewById(R.id.tvRegisterClickable);

        // ----------------- Reset Password button -----------------
        btnReset.setOnClickListener(v -> {
            // TODO: Add actual password reset logic (e.g., API call)
            editTextEmail.setText(""); // optional: clear field
            // e.g., Toast.makeText(this, "Reset email sent!", Toast.LENGTH_SHORT).show();
        });

        // ----------------- Back button -----------------
        btnBack.setOnClickListener(v -> finish()); // simply closes this activity

        // ----------------- Login clickable link -----------------
        tvLoginLink.setOnClickListener(v -> {
            startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
            finish(); // optional: close this activity
        });
    }
}
