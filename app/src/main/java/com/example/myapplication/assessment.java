package com.example.myapplication;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class assessment extends AppCompatActivity {

    // --- Data Model ---
    private static class Question {
        String questionText;
        int score; // -1 means unanswered

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
    private Button[] answerButtons = new Button[4];

    // Colors
    private int colorBrown = Color.parseColor("#794A3D");
    private int colorHighlight = Color.parseColor("#FFCC00");
    private int colorGray = Color.LTGRAY;
    private int colorSelectedBrown = Color.parseColor("#4D2E24");
    private int colorTextDefault = Color.WHITE;

    private static final float DISABLED_ALPHA = 0.5f;
    private static final float ENABLED_ALPHA = 1.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assessment);

        // --- Initialize UI Elements ---
        backButton = findViewById(R.id.button_back);
        nextButton = findViewById(R.id.button_next);
        progressText = findViewById(R.id.text_progress);
        questionText = findViewById(R.id.text_question);
        segmentContainer = findViewById(R.id.layout_progress_segments);

        // Answer buttons from XML
        answerButtons[0] = findViewById(R.id.button_answer_1);
        answerButtons[1] = findViewById(R.id.button_answer_2);
        answerButtons[2] = findViewById(R.id.button_answer_3);
        answerButtons[3] = findViewById(R.id.button_answer_4);

        // --- Setup Colors ---
        try {
            colorBrown = ContextCompat.getColor(this, R.color.button_brown);
            colorHighlight = ContextCompat.getColor(this, R.color.segment_highlight);
            colorGray = ContextCompat.getColor(this, R.color.segment_gray);
        } catch (Exception e) {
            Toast.makeText(this, "Missing color resources, using defaults.", Toast.LENGTH_SHORT).show();
        }

        // --- Setup Questions ---
        setupQuestionData();

        // --- Setup Answer Buttons Click Listeners ---
        for (int i = 0; i < answerButtons.length; i++) {
            final int index = i;
            answerButtons[i].setOnClickListener(v -> selectAnswer(index));
        }

        // --- Navigation Buttons ---
        backButton.setOnClickListener(v -> navigate(-1));
        nextButton.setOnClickListener(v -> navigate(1));

        // Load first question
        loadQuestion(currentQuestionIndex);
    }

    private void setupQuestionData() {
        try {
            questionList.add(new Question(getString(R.string.question_1)));
            questionList.add(new Question(getString(R.string.question_2)));
            questionList.add(new Question(getString(R.string.question_3)));
            questionList.add(new Question(getString(R.string.question_4)));
            questionList.add(new Question(getString(R.string.question_5)));
            questionList.add(new Question(getString(R.string.question_6)));
            questionList.add(new Question(getString(R.string.question_7)));
            questionList.add(new Question(getString(R.string.question_8)));
            questionList.add(new Question(getString(R.string.question_9)));
        } catch (Exception e) {
            questionList.add(new Question("1. Little interest or pleasure in doing things?"));
            questionList.add(new Question("2. Feeling down, depressed, or hopeless?"));
            questionList.add(new Question("3. Trouble falling or staying asleep, or sleeping too much?"));
            questionList.add(new Question("4. Feeling tired or having little energy?"));
            questionList.add(new Question("5. Poor appetite or overeating?"));
            questionList.add(new Question("6. Feeling bad about yourself or that you are a failure?"));
            questionList.add(new Question("7. Trouble concentrating on things?"));
            questionList.add(new Question("8. Moving or speaking so slowly or being restless?"));
            questionList.add(new Question("9. Thoughts that you would be better off dead, or of hurting yourself?"));
        }
    }

    private void loadQuestion(int index) {
        if (index < 0 || index >= questionList.size()) return;

        Question currentQ = questionList.get(index);

        // Update question text
        questionText.setText(currentQ.questionText);

        // Update progress text
        progressText.setText((index + 1) + " of " + questionList.size());

        // Update progress indicator segments
        for (int i = 0; i < segmentContainer.getChildCount(); i++) {
            View segment = segmentContainer.getChildAt(i);
            int tintColor = (i <= currentQuestionIndex) ? colorHighlight : colorGray;
            if (segment.getBackground() != null) {
                segment.getBackground().setColorFilter(new PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN));
            }
        }

        // Update answer buttons selection
        updateButtonSelection(currentQ.score);

        // Update navigation buttons
        updateNavigationButtons(index, currentQ.score);
    }

    private void selectAnswer(int index) {
        questionList.get(currentQuestionIndex).score = index;
        updateButtonSelection(index);
        updateNavigationButtons(currentQuestionIndex, index);
    }

    private void updateButtonSelection(int selectedScore) {
        for (int i = 0; i < answerButtons.length; i++) {
            Button button = answerButtons[i];
            if (i == selectedScore) {
                button.setBackgroundColor(colorSelectedBrown);
            } else {
                button.setBackgroundColor(colorBrown);
            }
        }
    }

    private void navigate(int direction) {
        int nextIndex = currentQuestionIndex + direction;

        // --- BACK on first question goes to dashboard ---
        if (direction == -1 && currentQuestionIndex == 0) {
            finish(); // closes assessment and returns to PatientDashboard
            return;
        }

        if (nextIndex >= 0 && nextIndex < questionList.size()) {
            currentQuestionIndex = nextIndex;
            loadQuestion(currentQuestionIndex);
        } else if (nextIndex == questionList.size()) {
            if (!allQuestionsAnswered()) {
                int firstUnanswered = findFirstUnansweredQuestionIndex();
                Toast.makeText(this,
                        "Please answer Question " + (firstUnanswered + 1) + " before submitting.",
                        Toast.LENGTH_LONG).show();
                return;
            }
            int totalScore = calculateScore();
            Toast.makeText(this, "Assessment Complete! Total score: " + totalScore, Toast.LENGTH_LONG).show();
        }
    }

    private boolean allQuestionsAnswered() {
        for (Question q : questionList) {
            if (q.score == -1) return false;
        }
        return true;
    }

    private int findFirstUnansweredQuestionIndex() {
        for (int i = 0; i < questionList.size(); i++) {
            if (questionList.get(i).score == -1) return i;
        }
        return -1;
    }

    private int calculateScore() {
        int total = 0;
        for (Question q : questionList) {
            if (q.score != -1) total += q.score;
        }
        return total;
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
