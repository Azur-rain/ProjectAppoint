package com.example.projectappoint;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import java.util.Calendar;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private int currentStep = 1;
    private static final int MAX_STEPS = 3;

    private View layoutStep1, layoutStep3, layoutStep2;

    private AppCompatButton btnNext, btnBack;

    AppCompatEditText etFirst, etLast, etEmail, etDateOfBirth, etPassword, etConPassword;
    Spinner spGender;

    String firstName, lastName, email, gender, dateOfBirth, password, passwordConfirm;

    TextView tveFirst, tveLast, tveEmail, tveGender, tveDateOfBirth, tvePassword, tveConPassword, tveConsent;

    CheckBox cbConsent;

    private FirebaseService firestoreRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regis);

        firestoreRepo = FirebaseService.getInstance();

        btnNext = findViewById(R.id.button_next);
        btnBack = findViewById(R.id.button_back);

        layoutStep1 = findViewById(R.id.regis_step_1);
        layoutStep2 = findViewById(R.id.regis_step_2);
        layoutStep3 = findViewById(R.id.regis_step_3);

        etFirst = findViewById(R.id.et_firstName);
        etLast = findViewById(R.id.et_lastName);
        etEmail = findViewById(R.id.et_email);
        etDateOfBirth = findViewById(R.id.et_date_of_birth);
        etPassword = findViewById(R.id.et_password);
        etConPassword = findViewById(R.id.et_con_password);

        TextStylingUtils.setupHintAndTextWatcher(etFirst, "First Name");
        TextStylingUtils.setupHintAndTextWatcher(etLast, "Last Name");
        TextStylingUtils.setupHintAndTextWatcher(etEmail, "Email");
        TextStylingUtils.setupHintAndTextWatcher(etDateOfBirth, "Date of Birth");
        TextStylingUtils.setupHintAndTextWatcher(etPassword, "Password");
        TextStylingUtils.setupHintAndTextWatcher(etConPassword, "Confirm Password");

        spGender = findViewById(R.id.sp_gender);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.gender_options,
                R.layout.custom_spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spGender.setAdapter(adapter);

        etDateOfBirth.setOnClickListener(v -> showDatePickerDialog());

        tveFirst = findViewById(R.id.tv_firstName_Error);
        tveLast = findViewById(R.id.tv_lastName_Error);
        tveEmail = findViewById(R.id.tv_email_Error);
        tveGender = findViewById(R.id.tv_gender_Error);
        tveDateOfBirth = findViewById(R.id.tv_date_of_birth_Error);
        tvePassword = findViewById(R.id.tv_password_Error);
        tveConPassword = findViewById(R.id.tv_con_password_Error);
        tveConsent = findViewById(R.id.tv_consent_Error);

        cbConsent = findViewById(R.id.cb_consent);


        showCurrentStep();
        UpdateButtonText();

        btnNext.setOnClickListener(v -> handleNextButtonClick(true));
        btnBack.setOnClickListener(v -> handleNextButtonClick(false));
    }

    private void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String selectedDate = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year1;
                    etDateOfBirth.setText(selectedDate);
                },
                year, month, day);
        datePickerDialog.show();
    }

    private void handleNextButtonClick(boolean isNext) {

        boolean isValidated = false;

        if (isNext) {

            switch (currentStep) {
                case 1:
                    firstName = Objects.requireNonNull(etFirst.getText()).toString().trim();
                    boolean isFirstValid = validateFields(firstName, tveFirst); // Store separately

                    lastName = Objects.requireNonNull(etLast.getText()).toString().trim();
                    boolean isLastValid = validateFields(lastName, tveLast); // Store separately
                    isValidated = isFirstValid && isLastValid;

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
                    gender = spGender.getSelectedItem().toString();
                    boolean isGenderValid = false;
                    if (gender.equals("Select Gender")) {
                        tveGender.setText("*Gender is missing");
                        TextStylingUtils.showAndFadeOut(tveGender, 4000);
                    } else isGenderValid = true;
                    dateOfBirth = Objects.requireNonNull(etDateOfBirth.getText()).toString().trim();
                    boolean isDateOfBirthValid = validateFields(dateOfBirth, tveDateOfBirth);
                    isValidated = isGenderValid && isDateOfBirthValid;
                    break;
                case 3:
                    password = Objects.requireNonNull(etPassword.getText()).toString().trim();
                    isValidated = validateFields(password, tvePassword);

                    if (password.length() < 10 || password.length() > 16) {
                        tvePassword.setText("*Password must be between 10 and 16 characters");
                        isValidated = false;
                        TextStylingUtils.showAndFadeOut(tvePassword, 4000);
                    }

                    passwordConfirm = Objects.requireNonNull(etConPassword.getText()).toString().trim();
                    if (!password.equals(passwordConfirm) && isValidated) {
                        tveConPassword.setText("*Password does not match");
                        TextStylingUtils.showAndFadeOut(tveConPassword, 4000);
                        isValidated = false;
                    } else isValidated = true;
                    break;
            }
            if (!isValidated) {
                return;
            }

            if (currentStep < MAX_STEPS) {
                currentStep++;
            } else if (currentStep == MAX_STEPS) {
                submitRegistration();
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

    private void submitRegistration() {
        // 1. Check Consent first
        if (!cbConsent.isChecked()) {
            tveConsent.setText("*Accept the Privacy Policy and Terms of Service before proceeding.");
            TextStylingUtils.showAndFadeOut(tveConsent, 4000);
            return;
        }

        // 2. Hash the password
        String hashedPassword = SecurityUtils.hashPassword(password);

        // 3. Create the UserData object
        UserData newUser = new UserData(
                firstName,
                lastName,
                email,
                gender,
                dateOfBirth,
                hashedPassword
        );

        // 4. Update UI to indicate loading (prevent double clicks)
        btnNext.setEnabled(false);
        btnNext.setText("REGISTERING...");

        // 5. Pass to Firebase Service with the REQUIRED Callback
        firestoreRepo.addUser(newUser, new FirebaseService.UserAddCallback() {
            @Override
            public void onSuccess(String documentId) {
                // --- SUCCESS ---
                // Only navigate AFTER the database confirms the save
                Log.d("RegisterActivity", "User saved with ID: " + documentId);

                Intent intent = new Intent(RegisterActivity.this, PatientDashboard.class);
                // Clear the back stack so they can't press "Back" to return to registration
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Exception e) {
                btnNext.setEnabled(true);
                btnNext.setText("REGISTER");

                // Show error message
                tveConsent.setText("Registration Failed: " + e.getMessage());
                TextStylingUtils.showAndFadeOut(tveConsent, 4000);
                Log.e("RegisterActivity", "Error saving user", e);
            }
        });
    }

}
