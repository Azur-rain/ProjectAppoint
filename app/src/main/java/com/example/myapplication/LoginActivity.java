package com.example.myapplication;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // ----------------- Views -----------------
        AppCompatEditText editTextE = findViewById(R.id.eTEmail);
        AppCompatEditText editTextPW = findViewById(R.id.etPassword);
        TextView tvErrorEmail = findViewById(R.id.tvEmailError);
        TextView tvErrorPassword = findViewById(R.id.tvPasswordError);
        AppCompatButton buttonLogin = findViewById(R.id.btnLogin);
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
            boolean validateSuccess = true;

            // ----------------- Empty Field Validations -----------------
            if (email.isEmpty()) {
                TextStylingUtils.showAndFadeOut(tvErrorEmail, 4000);
                validateSuccess = false;
                editTextE.requestFocus();
            }

            if (password.isEmpty()) {
                TextStylingUtils.showAndFadeOut(tvErrorPassword, 4000);
                validateSuccess = false;
                editTextPW.requestFocus();
            }

            // ----------------- Temporary Hardcoded Login -----------------
            if (validateSuccess) {

                String TEMP_EMAIL = "test";
                String TEMP_PASSWORD = "123";

                if (email.equals(TEMP_EMAIL) && password.equals(TEMP_PASSWORD)) {

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

                } else {


                    if (!email.equals(TEMP_EMAIL)) {
                        TextStylingUtils.showAndFadeOut(tvErrorEmail, 4000);
                        editTextE.requestFocus();
                    }


                    if (!password.equals(TEMP_PASSWORD)) {
                        TextStylingUtils.showAndFadeOut(tvErrorPassword, 4000);
                        editTextPW.requestFocus();
                    }
                }
            }
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
