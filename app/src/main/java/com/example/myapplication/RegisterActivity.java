package com.example.myapplication;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class RegisterActivity extends AppCompatActivity {

    private EditText etFirstName, etLastName, etEmail;
    private TextView tvfnError, tvlnError, tvemaError;
    private AppCompatButton btnNext1, btnBack1;

    private EditText etGender, etDob;
    private TextView tvGenderError, tvDobError;
    private AppCompatButton btnNext2, btnBack2;

    private EditText etPassword, etConfirmPassword;
    private TextView tvPasswordError, tvMin, tvMax, tvConfirmError;
    private CheckBox cbAccept;
    private AppCompatButton btnCreateAccount, btnBack3;

    private View layoutRegis1, layoutRegis2, layoutRegis3;

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
        // Terms & Privacy
        TextView tvTerms = layoutStep3.findViewById(R.id.tv1);
        TextView tvPrivacy = layoutStep3.findViewById(R.id.tv3);
        tvTerms.setOnClickListener(v -> showDialog(R.layout.termdialog, R.id.btnClose));
        tvPrivacy.setOnClickListener(v -> showDialog(R.layout.policydialog, R.id.btnClosetwo));

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
        // Password strength check
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = s.toString();
                if (password.isEmpty()) {
                    tvPasswordError.setText("*Password is required");
                    tvPasswordError.setTextColor(Color.RED);
                } else if (password.length() < 10) {
                    tvPasswordError.setText("*Password is weak");
                    tvPasswordError.setTextColor(Color.RED);
                } else if (password.length() <= 13) {
                    tvPasswordError.setText("*Password is medium");
                    tvPasswordError.setTextColor(Color.parseColor("#00FF0A"));
                } else {
                    tvPasswordError.setText("*Password is strong");
                    tvPasswordError.setTextColor(Color.parseColor("#00FF0A"));
                }
                tvPasswordError.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        // Checkbox enables Create Account button
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (currentStep == 3) {
                btnNext.setEnabled(isChecked);
            }
        });

        // Setup Gender Spinner with styled hint and dropdown
        setupGenderSpinner();

        // Date picker for DOB
        etDateOfBirth.setOnClickListener(v -> showDatePicker());

        btnNext.setOnClickListener(v -> {
            handleNext(true);
            updateButtonState();
        });

        btnBack.setOnClickListener(v -> {
            handleNext(false);
            updateButtonState();
        });

        showCurrentStep();
        updateButtonText();
        updateButtonState();
    }

    private void setupGenderSpinner() {
        String[] genders = {"Gender", "Male", "Female"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, genders) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0; // Disable hint
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextSize(12);
                tv.setTextColor(position == 0 ? Color.GRAY : Color.BLACK); // hint gray, selection black
                return tv;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextSize(12);
                tv.setTextColor(Color.BLACK); // dropdown items black
                tv.setBackgroundColor(Color.WHITE); // dropdown background white
                return tv;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGender.setAdapter(adapter);
        spGender.setSelection(0); // show hint by default
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String dob = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
                    etDateOfBirth.setText(dob);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void handleNext(boolean isNext) {
        if (isNext) {
            boolean valid = false;

            switch (currentStep) {
                case 1:
                    valid = validateStep1();
                    break;
                case 2:
                    valid = validateStep2();
                    break;
                case 3:
                    valid = validateStep3();
                    if (valid) {
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                        return;
                    }
                    break;
            }

            if (!valid) return;
            if (currentStep < MAX_STEPS) currentStep++;
        } else {
            if (currentStep > 1) currentStep--;
            else {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return;
            }
        }

        showCurrentStep();
        updateButtonText();
    }

    private boolean validateStep1() {
        boolean valid = true;

        if (TextUtils.isEmpty(etFirst.getText())) {
            tvFirstError.setVisibility(View.VISIBLE);
            valid = false;
        } else tvFirstError.setVisibility(View.INVISIBLE);

        if (TextUtils.isEmpty(etLast.getText())) {
            tvLastError.setVisibility(View.VISIBLE);
            valid = false;
        } else tvLastError.setVisibility(View.INVISIBLE);

        String email = Objects.requireNonNull(etEmail.getText()).toString().trim();
        if (email.isEmpty()) {
            tvEmailError.setText("*Email is missing");
            tvEmailError.setVisibility(View.VISIBLE);
            valid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tvEmailError.setText("*Invalid email format");
            tvEmailError.setVisibility(View.VISIBLE);
            valid = false;
        } else tvEmailError.setVisibility(View.INVISIBLE);

        return valid;
    }

    private boolean validateStep2() {
        boolean valid = true;

        if (spGender.getSelectedItemPosition() == 0) {
            tvGenderError.setVisibility(View.VISIBLE);
            valid = false;
        } else tvGenderError.setVisibility(View.INVISIBLE);

        if (TextUtils.isEmpty(etDateOfBirth.getText())) {
            tvDobError.setVisibility(View.VISIBLE);
            valid = false;
        } else tvDobError.setVisibility(View.INVISIBLE);

        return valid;
    }

    private boolean validateStep3() {
        boolean valid = true;

        String password = Objects.requireNonNull(etPassword.getText()).toString().trim();
        String confirm = Objects.requireNonNull(etConPassword.getText()).toString().trim();

        // Password validation
        if (TextUtils.isEmpty(password) || password.length() < 10) {
            tvPasswordError.setVisibility(View.VISIBLE);
            valid = false;
        } else {
            tvPasswordError.setVisibility(View.INVISIBLE);
        }

        // Confirm password validation
        if (TextUtils.isEmpty(confirm)) {
            tvConPasswordError.setText("*Please confirm your password");
            tvConPasswordError.setVisibility(View.VISIBLE);
            valid = false;
        } else if (!password.equals(confirm)) {
            if (password.length() >= 10) {
                tvConPasswordError.setText("*Password do not match");
                tvConPasswordError.setVisibility(View.VISIBLE);
                valid = false;
            }
        } else {
            tvConPasswordError.setVisibility(View.INVISIBLE);
        }

        // Checkbox must be checked
        if (!checkBox.isChecked()) valid = false;

        return valid;
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
