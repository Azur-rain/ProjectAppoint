package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

import java.util.Calendar;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private int currentStep = 1;
    private static final int MAX_STEPS = 3;

    private View layoutStep1, layoutStep2, layoutStep3;

    private AppCompatButton btnNext, btnBack;
    private CheckBox checkBox;

    private AppCompatEditText etFirst, etLast, etEmail, etDateOfBirth, etPassword, etConPassword;
    private Spinner spGender;
    private TextView tvFirstError, tvLastError, tvEmailError, tvGenderError, tvDobError, tvPasswordError, tvConPasswordError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regis);

        btnNext = findViewById(R.id.button_next);
        btnBack = findViewById(R.id.button_back);

        layoutStep1 = findViewById(R.id.regis_step_1);
        layoutStep2 = findViewById(R.id.regis_step_2);
        layoutStep3 = findViewById(R.id.regis_step_3);

        // Step 1 fields
        etFirst = layoutStep1.findViewById(R.id.et_firstName);
        etLast = layoutStep1.findViewById(R.id.et_lastName);
        etEmail = layoutStep1.findViewById(R.id.et_email);
        tvFirstError = layoutStep1.findViewById(R.id.tv_firstName_Error);
        tvLastError = layoutStep1.findViewById(R.id.tv_lastName_Error);
        tvEmailError = layoutStep1.findViewById(R.id.tv_email_Error);

        // Step 2 fields
        spGender = layoutStep2.findViewById(R.id.et_gender);
        etDateOfBirth = layoutStep2.findViewById(R.id.et_date_of_birth);
        tvGenderError = layoutStep2.findViewById(R.id.tv_gender_Error);
        tvDobError = layoutStep2.findViewById(R.id.tv_date_of_birth_Error);

        // Step 3 fields
        etPassword = layoutStep3.findViewById(R.id.et_password);
        etConPassword = layoutStep3.findViewById(R.id.et_con_password);
        tvPasswordError = layoutStep3.findViewById(R.id.tv_password_Error);
        tvConPasswordError = layoutStep3.findViewById(R.id.tv_con_password_Error);
        checkBox = layoutStep3.findViewById(R.id.checkBox);

        // Terms & Privacy
        TextView tvTerms = layoutStep3.findViewById(R.id.tv1);
        TextView tvPrivacy = layoutStep3.findViewById(R.id.tv3);
        tvTerms.setOnClickListener(v -> showDialog(R.layout.termdialog, R.id.btnClose));
        tvPrivacy.setOnClickListener(v -> showDialog(R.layout.policydialog, R.id.btnClosetwo));

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

    private void showCurrentStep() {
        layoutStep1.setVisibility(View.GONE);
        layoutStep2.setVisibility(View.GONE);
        layoutStep3.setVisibility(View.GONE);

        switch (currentStep) {
            case 1: layoutStep1.setVisibility(View.VISIBLE); break;
            case 2: layoutStep2.setVisibility(View.VISIBLE); break;
            case 3: layoutStep3.setVisibility(View.VISIBLE); break;
        }
    }

    private void updateButtonText() {
        if (currentStep == 3) btnNext.setText("Create Account");
        else btnNext.setText("NEXT");

        if (currentStep == 1) btnBack.setText("BACK TO LOGIN");
        else btnBack.setText("BACK");
    }

    private void updateButtonState() {
        if (currentStep == 3) {
            btnNext.setEnabled(checkBox.isChecked());
        } else {
            btnNext.setEnabled(true);
        }
        btnNext.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.button_enabled_state));
    }

    private void showDialog(int layoutResId, int btnCloseId) {
        View dialogView = getLayoutInflater().inflate(layoutResId, null);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        AppCompatButton btnClose = dialogView.findViewById(btnCloseId);
        btnClose.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}
