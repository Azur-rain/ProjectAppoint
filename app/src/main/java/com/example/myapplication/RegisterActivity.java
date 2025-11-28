package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private int currentStep = 1;
    private static final int MAX_STEPS = 3;

    private View layoutStep1, layoutStep3, layoutStep2;

    private AppCompatButton btnNext, btnBack;

    AppCompatEditText etFirst, etLast, etEmail, etGender, etDateOfBirth, etPaaword, etConPassword;

    String firstName, lastName, email, gender, dateOfBirth, password, passwordConfirm;

    TextView tveFirst, tveLast, tveEmail, tveGender, tveDateOfBirth, tvePassword, tveConPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regis);

        btnNext = findViewById(R.id.button_next);
        btnBack = findViewById(R.id.button_back);

        layoutStep1 = findViewById(R.id.regis_step_1);
        layoutStep2 = findViewById(R.id.regis_step_2);
        layoutStep3 = findViewById(R.id.regis_step_3);

        etFirst = findViewById(R.id.et_firstName);
        etLast = findViewById(R.id.et_lastName);
        etEmail = findViewById(R.id.et_email);
        etGender = findViewById(R.id.et_gender);
        etDateOfBirth = findViewById(R.id.et_date_of_birth);
        etPaaword = findViewById(R.id.et_password);
        etConPassword = findViewById(R.id.et_con_password);


        TextStylingUtils.setupHintAndTextWatcher(etFirst, "First Name");
        TextStylingUtils.setupHintAndTextWatcher(etLast, "Last Name");
        TextStylingUtils.setupHintAndTextWatcher(etEmail, "Email");
        TextStylingUtils.setupHintAndTextWatcher(etGender, "Gender");
        TextStylingUtils.setupHintAndTextWatcher(etDateOfBirth, "Date of Birth");
        TextStylingUtils.setupHintAndTextWatcher(etPaaword, "Password");
        TextStylingUtils.setupHintAndTextWatcher(etConPassword, "Confirm Password");

        tveFirst = findViewById(R.id.tv_firstName_Error);
        tveLast = findViewById(R.id.tv_lastName_Error);
        tveEmail = findViewById(R.id.tv_email_Error);
        tveGender = findViewById(R.id.tv_gender_Error);
        tveDateOfBirth = findViewById(R.id.tv_date_of_birth_Error);
        tvePassword = findViewById(R.id.tv_password_Error);
        tveConPassword = findViewById(R.id.tv_con_password_Error);


        showCurrentStep();
        UpdateButtonText();

        btnNext.setOnClickListener(v -> handleNextButtonClick(true));
        btnBack.setOnClickListener(v -> handleNextButtonClick(false));
    }

    private void handleNextButtonClick(boolean isNext) {

        boolean isValidated = false;

        if (isNext) {

            switch (currentStep) {
                case 1:
                    firstName = Objects.requireNonNull(etFirst.getText()).toString().trim();
                    isValidated = validateFields(firstName, tveFirst);
                    lastName = Objects.requireNonNull(etLast.getText()).toString().trim();
                    isValidated = validateFields(lastName, tveLast);
                    email = Objects.requireNonNull(etEmail.getText()).toString().trim();
                    if (email.isEmpty()) {
                        tveEmail.setText("*Email is missing");
                        TextStylingUtils.showAndFadeOut(tveEmail, 4000);
                        isValidated = false;
                    } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        tveEmail.setText("*Invalid email format");
                        TextStylingUtils.showAndFadeOut(tveEmail, 4000);
                        isValidated = false;
                    }
                    break;
                case 2:
                    gender = Objects.requireNonNull(etGender.getText()).toString().trim();
                    isValidated = validateFields(gender, tveGender);
                    dateOfBirth = Objects.requireNonNull(etDateOfBirth.getText()).toString().trim();
                    isValidated = validateFields(dateOfBirth, tveDateOfBirth);
                    break;
                case 3:
                    password = Objects.requireNonNull(etPaaword.getText()).toString().trim();
                    isValidated = validateFields(password, tvePassword);
                    break;
            }

            if (!isValidated) {
                return;
            }

            if (currentStep < MAX_STEPS) {
                currentStep++;
            } else if (currentStep == MAX_STEPS) {
                // submitRegistration();
                return;
            }

        } else {
            if (currentStep > 1) {
                currentStep--;
            } else {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        }

        showCurrentStep();
        UpdateButtonText();
    }

    private boolean validateFields(String field, TextView tv) {
        if (field.isEmpty()) {
            TextStylingUtils.showAndFadeOut(tv, 4000);
            return false;
        } else return true;
    }


    private void UpdateButtonText() {
        if (currentStep == 3) {
            btnNext.setText("REGISTER");
        } else {
            btnNext.setText("NEXT");
        }

        if (currentStep == 1) {
            btnBack.setText("BACK TO LOGIN");
        } else {
            btnBack.setText("BACK");
        }
    }

    private void showCurrentStep() {

        layoutStep1.setVisibility(View.GONE);
        layoutStep2.setVisibility(View.GONE);
        layoutStep3.setVisibility(View.GONE);

        switch (currentStep) {
            case 1:
                layoutStep1.setVisibility(View.VISIBLE);
                break;
            case 2:
                layoutStep2.setVisibility(View.VISIBLE);
                break;
            case 3:
                layoutStep3.setVisibility(View.VISIBLE);
                break;
        }
    }

}