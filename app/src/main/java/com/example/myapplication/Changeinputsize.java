package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

public class Changeinputsize extends AppCompatActivity {

    // Define standard sizes for reuse
    private static final float HINT_SIZE_DP = 12f;
    private static final float TYPED_TEXT_SIZE_SP = 16f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // --- FIX: Loading the correct layout file: R.layout.login ---
        setContentView(R.layout.login);

        // 1. Setup Email Field
        // These IDs (editTextE, editTextPW) must be present in your login.xml file.
        AppCompatEditText editTextE = findViewById(R.id.editTextE);
        setupHintAndTextWatcher(editTextE, "Email");

            // Forget password field
        AppCompatEditText editTextEbar = findViewById(R.id.editTextEbar);
        setupHintAndTextWatcher(editTextEbar, "Email");

        // 2. Setup Password Field
        AppCompatEditText editTextPW = findViewById(R.id.editTextPW);
        setupHintAndTextWatcher(editTextPW, "Password");
    }

    /**
     * Sets the styled hint (12dp, 50% black) and attaches the TextWatcher
     * for dynamic sizing (16sp when typing).
     */
    private void setupHintAndTextWatcher(final AppCompatEditText editText, String hintText) {
        // --- Hint Styling: 12dp size and 50% Black color ---
        SpannableString ss = new SpannableString(hintText);
        // Set hint size to 12dp (The 'true' parameter indicates the size unit is DP)
        ss.setSpan(new AbsoluteSizeSpan((int) HINT_SIZE_DP, true), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Set 50% opacity black color (#80000000)
        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#80000000")), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        editText.setHint(ss);

        // Set the base text size to 12sp initially
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, HINT_SIZE_DP);

        // --- TextWatcher for Dynamic Sizing ---
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                // Check if the typed text is present
                if (s.length() > 0) {
                    // Typed text is present: Set size to 16sp
                    editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, TYPED_TEXT_SIZE_SP);
                } else {
                    // Text is empty: Revert to 12sp
                    editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, HINT_SIZE_DP);
                }
            }
        });
    }
}
