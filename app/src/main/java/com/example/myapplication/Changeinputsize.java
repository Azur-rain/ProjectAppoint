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
    private static final float HINT_SIZE_SP = 12f;
    private static final float TYPED_TEXT_SIZE_SP = 16f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load layout file
        setContentView(R.layout.login);

        // 1. Setup Email Field
        AppCompatEditText editTextE = findViewById(R.id.editTextE);
        setupHintAndTextWatcher(editTextE, "Email");

        // 2. Forget password field
        AppCompatEditText editTextEbar = findViewById(R.id.editTextEbar);
        setupHintAndTextWatcher(editTextEbar, "Email");

        // 3. Setup Password Field
        AppCompatEditText editTextPW = findViewById(R.id.editTextPW);
        setupHintAndTextWatcher(editTextPW, "Password");

        // 4. Setup First Name Field
        AppCompatEditText etfn = findViewById(R.id.etfn);
        setupHintAndTextWatcher(etfn, "First Name");

        // 5. Setup Last Name Field
        AppCompatEditText etln = findViewById(R.id.etln);
        setupHintAndTextWatcher(etln, "Last Name");

        // 6. Setup Email Field
        AppCompatEditText etmail = findViewById(R.id.etmail);
        setupHintAndTextWatcher(etmail, "Email");

        // 7. Setup Gender Field
        AppCompatEditText etgen = findViewById(R.id.etgen);
        setupHintAndTextWatcher(etgen, "Gender");

        // 8. Setup DOB Field
        AppCompatEditText etdob = findViewById(R.id.etdob);
        setupHintAndTextWatcher(etdob, "Date of Birth");

        // 9. Setup Pas Field
        AppCompatEditText etpas = findViewById(R.id.etpas);
        setupHintAndTextWatcher(etpas, "Password");

        // 10. Setup Conpa Field
        AppCompatEditText etconpa = findViewById(R.id.etconpa);
        setupHintAndTextWatcher(etconpa, "Confirm Password");


    }

    /**
     * Sets the styled hint (12sp, 50% black) and attaches the TextWatcher
     * for dynamic sizing (16sp when typing).
     */
    private void setupHintAndTextWatcher(final AppCompatEditText editText, String hintText) {
        // --- Hint Styling: 12sp size and 50% Black color ---
        SpannableString ss = new SpannableString(hintText);

        // Set hint size to 12sp (false = SP, true = DP)
        ss.setSpan(new AbsoluteSizeSpan((int) HINT_SIZE_SP, false), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Set 50% opacity black color (#80000000)
        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#80000000")), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        editText.setHint(ss);

        // Set hint and base text color/size
        editText.setTextColor(Color.parseColor("#FF000000")); // Always full black text
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, HINT_SIZE_SP);

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
                    editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, HINT_SIZE_SP);
                }
            }
        });
    }
}