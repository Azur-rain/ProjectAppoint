package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;

public class assessment extends AppCompatActivity {

    // --- Data Model ---
    private static class Question {
        String questionText;
        int score; // -1 = unanswered

        Question(String text) {
            this.questionText = text;
            this.score = -1;
        }
    }

    private final List<Question> questionList = new ArrayList<>();
    private int currentQuestionIndex = 0;

    // --- UI Elements ---
    private Button backButton;
    private Button nextButton;
    private TextView progressText;
    private TextView questionText;
    private LinearLayout segmentContainer;
    private LinearLayout buttonContainer;
    private final Button[] answerButtons = new Button[4];

    // Colors
    private int colorBrown = Color.parseColor("#794A3D");
    private int colorHighlight = Color.parseColor("#FFCC00");
    private int colorGray = Color.LTGRAY;
    private int colorSelectedBrown = Color.parseColor("#4D2E24");
    private int colorTextDefault = Color.WHITE;

    private static final float DISABLED_ALPHA = 0.5f;
    private static final float ENABLED_ALPHA = 1.0f;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assessment);

        // Initialize colors
        try {
            colorBrown = ContextCompat.getColor(this, R.color.button_brown);
            colorHighlight = ContextCompat.getColor(this, R.color.segment_highlight);
            colorGray = ContextCompat.getColor(this, R.color.segment_gray);
        } catch (Exception ignored) {}

        // Setup questions
        setupQuestionData();

        // --- UI Element Initialization ---
        backButton = findViewById(R.id.button_back);
        nextButton = findViewById(R.id.button_next);
        progressText = findViewById(R.id.text_progress);
        questionText = findViewById(R.id.text_question);
        segmentContainer = findViewById(R.id.layout_progress_segments);
        buttonContainer = findViewById(R.id.layout_answer_buttons);

        // Setup answer buttons dynamically
        setupAnswerButtons();
        loadQuestion(currentQuestionIndex);

        // NEXT button
        nextButton.setOnClickListener(v -> navigate(1));

        // BACK button
        backButton.setOnClickListener(v -> {
            if (currentQuestionIndex == 0) {
                // Go back to dashboard
                finish();
            } else {
                navigate(-1);
            }
        });
    }

    private void setupQuestionData() {
        questionList.add(new Question("Little interest or pleasure in doing things?"));
        questionList.add(new Question("Feeling down, depressed, or hopeless?"));
        questionList.add(new Question("Trouble falling or staying asleep, or sleeping too much?"));
        questionList.add(new Question("Feeling tired or having little energy?"));
        questionList.add(new Question("Poor appetite or overeating?"));
        questionList.add(new Question("Feeling bad about yourself, or that you are a failure?"));
        questionList.add(new Question("Trouble concentrating on things?"));
        questionList.add(new Question("Moving or speaking slowly or being restless?"));
        questionList.add(new Question("Thoughts that you would be better off dead or of hurting yourself?"));
    }

    private void loadQuestion(int index) {
        if (index < 0 || index >= questionList.size()) return;

        Question currentQ = questionList.get(index);
        questionText.setText(currentQ.questionText);
        progressText.setText((index + 1) + " of " + questionList.size());
        setupProgressIndicator();
        updateButtonSelection(currentQ.score);
        updateNavigationButtons(index, currentQ.score);
    }

    private void setupProgressIndicator() {
        for (int i = 0; i < segmentContainer.getChildCount(); i++) {
            View segment = segmentContainer.getChildAt(i);
            int tintColor = (i <= currentQuestionIndex) ? colorHighlight : colorGray;
            if (segment.getBackground() != null) {
                segment.getBackground().setColorFilter(new PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN));
            }
        }
    }

    private void setupAnswerButtons() {
        String[] answers;
        try {
            answers = getResources().getStringArray(R.array.answer_choices);
        } catch (Exception e) {
            answers = new String[]{"Not at all", "Several days", "More than half the days", "Nearly every day"};
        }

        buttonContainer.removeAllViews();

        for (int i = 0; i < answers.length; i++) {
            Button button = new Button(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            int margin = (int) (12 * getResources().getDisplayMetrics().density);
            params.bottomMargin = margin;
            button.setLayoutParams(params);

            button.setText(answers[i]);
            button.setTextColor(colorTextDefault);
            button.setBackgroundColor(colorBrown);
            button.setHeight((int) (60 * getResources().getDisplayMetrics().density));

            answerButtons[i] = button;
            final int index = i;
            button.setOnClickListener(v -> selectAnswer(index));

            buttonContainer.addView(button);
        }
    }

    private void updateButtonSelection(int selectedScore) {
        for (int i = 0; i < answerButtons.length; i++) {
            Button button = answerButtons[i];
            if (button != null) {
                button.setBackgroundColor(i == selectedScore ? colorSelectedBrown : colorBrown);
            }
        }
        questionText.setAlpha(selectedScore != -1 ? ENABLED_ALPHA : DISABLED_ALPHA + 0.2f);
    }

    private void selectAnswer(int index) {
        questionList.get(currentQuestionIndex).score = index;
        updateButtonSelection(index);
        updateNavigationButtons(currentQuestionIndex, index);
    }

    private void navigate(int direction) {
        int nextIndex = currentQuestionIndex + direction;

        if (nextIndex >= 0 && nextIndex < questionList.size()) {
            currentQuestionIndex = nextIndex;
            loadQuestion(currentQuestionIndex);
        } else if (nextIndex == questionList.size()) {
            // SUBMIT clicked
            if (!allQuestionsAnswered()) return;

            int totalScore = calculateScore();
            Intent intent = new Intent(assessment.this, assessmentresult.class);
            intent.putExtra("totalScore", totalScore);
            startActivity(intent);
            finish();
        }
    }

    private boolean allQuestionsAnswered() {
        for (Question q : questionList) if (q.score == -1) return false;
        return true;
    }

    private int calculateScore() {
        int score = 0;
        for (Question q : questionList) if (q.score != -1) score += q.score;
        return score;
    }

    private void updateNavigationButtons(int index, int selectedScore) {
        boolean isAnswered = selectedScore != -1;

        if (index == questionList.size() - 1) {
            nextButton.setText("SUBMIT");
            nextButton.setEnabled(isAnswered);
            nextButton.setAlpha(isAnswered ? ENABLED_ALPHA : DISABLED_ALPHA);
        } else {
            nextButton.setText("NEXT");
            nextButton.setEnabled(isAnswered);
            nextButton.setAlpha(isAnswered ? ENABLED_ALPHA : DISABLED_ALPHA);
        }
    }
}
