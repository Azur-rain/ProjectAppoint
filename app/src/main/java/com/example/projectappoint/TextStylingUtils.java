package com.example.projectappoint;

import android.graphics.Color;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;

public class TextStylingUtils {

    // Define standard sizes for reuse
    private static final float HINT_SIZE_SP = 14f;
    private static final float TYPED_TEXT_SIZE_SP = 16f;

    public static void setupHintAndTextWatcher(final AppCompatEditText editText, String hintText) {
        // --- Hint Styling: 12sp size and 50% Black color ---
        SpannableString ss = new SpannableString(hintText);

        // Set hint size to 12sp (false = SP, true = DP)
        ss.setSpan(new AbsoluteSizeSpan((int) HINT_SIZE_SP, true), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

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

    public static void showAndFadeOut(View view, long visibleDurationMs) {

        // Instantly show the view
        view.setAlpha(1f);
        view.setVisibility(View.VISIBLE);

        // Wait, then fade out
        view.postDelayed(() -> {
            view.animate()
                    .alpha(0f)
                    .setDuration(200)
                    .withEndAction(() -> view.setVisibility(View.INVISIBLE))
                    .start();
        }, visibleDurationMs);
    }

}
