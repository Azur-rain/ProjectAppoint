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
        setContentView(R.layout.mainregis);

        // Inflate all steps
        layoutRegis1 = ((ViewStub) findViewById(R.id.stubregis1)).inflate();
        layoutRegis2 = ((ViewStub) findViewById(R.id.stubregis2)).inflate();
        layoutRegis2.setVisibility(View.GONE);
        layoutRegis3 = ((ViewStub) findViewById(R.id.stubregis3)).inflate();
        layoutRegis3.setVisibility(View.GONE);

        // --- STEP 1 ---
        etFirstName = layoutRegis1.findViewById(R.id.etfn);
        etLastName = layoutRegis1.findViewById(R.id.etln);
        etEmail = layoutRegis1.findViewById(R.id.etmail);
        tvfnError = layoutRegis1.findViewById(R.id.tvfnError2);
        tvlnError = layoutRegis1.findViewById(R.id.tvlnError22);
        tvemaError = layoutRegis1.findViewById(R.id.tvemaError3);
        btnNext1 = layoutRegis1.findViewById(R.id.btntonext);
        btnBack1 = layoutRegis1.findViewById(R.id.btnLoginback);

        btnBack1.setOnClickListener(v -> finish());

        btnNext1.setOnClickListener(v -> {
            boolean valid = true;
            tvfnError.setVisibility(TextUtils.isEmpty(etFirstName.getText()) ? View.VISIBLE : View.INVISIBLE);
            tvlnError.setVisibility(TextUtils.isEmpty(etLastName.getText()) ? View.VISIBLE : View.INVISIBLE);
            tvemaError.setVisibility(TextUtils.isEmpty(etEmail.getText()) ? View.VISIBLE : View.INVISIBLE);

            if (tvfnError.getVisibility() == View.INVISIBLE &&
                    tvlnError.getVisibility() == View.INVISIBLE &&
                    tvemaError.getVisibility() == View.INVISIBLE) {
                showStep2();
            }
        });

        // --- STEP 2 ---
        etGender = layoutRegis2.findViewById(R.id.etgen);
        etDob = layoutRegis2.findViewById(R.id.etdob);
        tvGenderError = layoutRegis2.findViewById(R.id.tvgenError33);
        tvDobError = layoutRegis2.findViewById(R.id.tvlnError4);
        btnNext2 = layoutRegis2.findViewById(R.id.btntonext1);
        btnBack2 = layoutRegis2.findViewById(R.id.btnback1);

        btnBack2.setOnClickListener(v -> {
            layoutRegis2.setVisibility(View.GONE);
            layoutRegis1.setVisibility(View.VISIBLE);
        });

        btnNext2.setOnClickListener(v -> {
            boolean valid = true;
            tvGenderError.setVisibility(TextUtils.isEmpty(etGender.getText()) ? View.VISIBLE : View.INVISIBLE);
            tvDobError.setVisibility(TextUtils.isEmpty(etDob.getText()) ? View.VISIBLE : View.INVISIBLE);

            if (tvGenderError.getVisibility() == View.INVISIBLE && tvDobError.getVisibility() == View.INVISIBLE) {
                showStep3();
            }
        });

        // --- STEP 3 ---
        etPassword = layoutRegis3.findViewById(R.id.etpas);
        etConfirmPassword = layoutRegis3.findViewById(R.id.etconpa);
        tvPasswordError = layoutRegis3.findViewById(R.id.tvgenError444);
        tvMin = layoutRegis3.findViewById(R.id.tvmin);
        tvMax = layoutRegis3.findViewById(R.id.tvmax);
        tvConfirmError = layoutRegis3.findViewById(R.id.tvlnError51);
        cbAccept = layoutRegis3.findViewById(R.id.cbAccept);
        btnCreateAccount = layoutRegis3.findViewById(R.id.btnmakeacc);
        btnBack3 = layoutRegis3.findViewById(R.id.btnback2);

        btnCreateAccount.setEnabled(false); // Disabled by default

        cbAccept.setOnCheckedChangeListener((buttonView, isChecked) -> btnCreateAccount.setEnabled(isChecked));

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
                } else if (password.length() <= 12) {
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

        btnCreateAccount.setOnClickListener(v -> {
            boolean valid = true;
            String password = etPassword.getText().toString();
            String confirm = etConfirmPassword.getText().toString();

            tvPasswordError.setVisibility(View.VISIBLE);
            tvConfirmError.setVisibility(View.INVISIBLE);

            if (TextUtils.isEmpty(password)) {
                tvPasswordError.setText("*Password is required");
                tvPasswordError.setTextColor(Color.RED);
                valid = false;
            } else if (password.length() < 10) {
                tvPasswordError.setText("*Password is weak");
                tvPasswordError.setTextColor(Color.RED);
                valid = false;
            }

            if (TextUtils.isEmpty(confirm)) {
                tvConfirmError.setText("*Please confirm your password");
                tvConfirmError.setVisibility(View.VISIBLE);
                valid = false;
            } else if (!password.equals(confirm)) {
                if (password.length() >= 10) {
                    tvConfirmError.setText("*Password do not match");
                    tvConfirmError.setVisibility(View.VISIBLE);
                    valid = false;
                }
            }

            if (!cbAccept.isChecked()) valid = false;

            if (valid && password.length() >= 10 && password.equals(confirm)) {
                layoutRegis3.setVisibility(View.GONE);
                // Redirect back to LoginActivity
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnBack3.setOnClickListener(v -> {
            layoutRegis3.setVisibility(View.GONE);
            layoutRegis2.setVisibility(View.VISIBLE);
        });

        // Terms & Privacy dialogs remain the same
        TextView tvTerms = layoutRegis3.findViewById(R.id.tv1);
        TextView tvPrivacy = layoutRegis3.findViewById(R.id.tv3);

        tvTerms.setOnClickListener(v -> showDialog(R.layout.termdialog, R.id.btnClose));
        tvPrivacy.setOnClickListener(v -> showDialog(R.layout.policydialog, R.id.btnClosetwo));
    }

    private void showStep2() {
        layoutRegis1.setVisibility(View.GONE);
        layoutRegis2.setVisibility(View.VISIBLE);
    }

    private void showStep3() {
        layoutRegis2.setVisibility(View.GONE);
        layoutRegis3.setVisibility(View.VISIBLE);
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
