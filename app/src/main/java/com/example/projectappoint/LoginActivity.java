package com.example.projectappoint;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private FirebaseService firestoreRepo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        firestoreRepo = FirebaseService.getInstance();

        // ----------------- Views -----------------
        AppCompatEditText editTextE = findViewById(R.id.et_email);
        AppCompatEditText editTextPW = findViewById(R.id.et_password);
        TextView tvErrorEmail = findViewById(R.id.tv_email_Error);
        TextView tvErrorPassword = findViewById(R.id.tv_password_Error);
        AppCompatButton buttonLogin = findViewById(R.id.btn_login);
        TextView tvForgotPassword = findViewById(R.id.tvForgotPasswordClickable);
        TextView tvRegisterClickable = findViewById(R.id.tvRegisterClickable);
        CheckBox cbRemember = findViewById(R.id.cbRemember);

        // ----------------- SharedPreferences -----------------
        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
        boolean rememberMe = sharedPreferences.getBoolean("remember_me", false);
        cbRemember.setChecked(rememberMe);

        if (rememberMe) {
            editTextE.setText(sharedPreferences.getString("saved_email", ""));
            editTextPW.setText(sharedPreferences.getString("saved_password", ""));
        }

        // ----------------- Hint & TextWatcher -----------------
        TextStylingUtils.setupHintAndTextWatcher(editTextE, "Email");
        TextStylingUtils.setupHintAndTextWatcher(editTextPW, "Password");

        // ----------------- Login button -----------------
        buttonLogin.setOnClickListener(v -> {

            String email = Objects.requireNonNull(editTextE.getText()).toString().trim();
            String password = Objects.requireNonNull(editTextPW.getText()).toString().trim();

            // ----------------- Empty Field Validations -----------------
            if (email.isEmpty()) {
                TextStylingUtils.showAndFadeOut(tvErrorEmail, 4000);
                editTextE.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                TextStylingUtils.showAndFadeOut(tvErrorPassword, 4000);
                editTextPW.requestFocus();
                return;
            }

            firestoreRepo.loginUser(email, password, new FirebaseService.LoginCallback() {
                @Override
                public void onSuccess() {
                    // Remember Me Logic
                    if (cbRemember.isChecked()) {
                        sharedPreferences.edit()
                                .putString("saved_email", email)
                                .putString("saved_password", password)
                                .putBoolean("remember_me", true)
                                .apply();
                    } else {
                        sharedPreferences.edit()
                                .remove("saved_email")
                                .remove("saved_password")
                                .putBoolean("remember_me", false)
                                .apply();
                    }

                    // Navigate to Dashboard
                    Intent intent = new Intent(LoginActivity.this, PatientDashboard.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(Exception e) {
                    if (e.getMessage().equals("User not found")) {
                        tvErrorEmail.setText("*Email not registered.");
                        TextStylingUtils.showAndFadeOut(tvErrorEmail, 4000);
                        editTextE.requestFocus();
                    } else if (e.getMessage().equals("Invalid password")) {
                        tvErrorPassword.setText("*Incorrect password.");
                        TextStylingUtils.showAndFadeOut(tvErrorPassword, 4000);
                        editTextPW.requestFocus();
                    }
                }
            });
        });

        // ----------------- Forgot Password link -----------------
        tvForgotPassword.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class))
        );

        // ----------------- Register link -----------------
        tvRegisterClickable.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class))
        );
    }
}
