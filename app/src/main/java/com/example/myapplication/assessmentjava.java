package com.example.myapplication;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;


/**
 * AssessmentActivity handles the logic and UI for the assessment flow (A-01, A-02, etc.).
 * It manages question changes, state persistence (selected answers), and UI updates.
 * This version implements the requirement to SELECT AN ANSWER before the NEXT button is enabled.
 */
public class assessmentjava extends AppCompatActivity {


    // --- Data Model ---
    private static class Question {
        String questionText;
        int score; // Score is the index of the selected answer (0-3)


        Question(String text) {
            this.questionText = text;
            this.score = -1; // -1 means no answer selected
        }
    }


    // List of all questions in the assessment
    private final List<Question> questionList = new ArrayList<>();
    private int currentQuestionIndex = 0; // 0-based index


    // --- UI Elements ---
    private Button backButton;
    private Button nextButton;
    private TextView progressText;
    private TextView questionText;
    private LinearLayout segmentContainer;
    private LinearLayout buttonContainer;
    private final Button[] answerButtons = new Button[4];


    // Color references and fallbacks
    private int colorBrown = Color.parseColor("#794A3D");
    private int colorHighlight = Color.parseColor("#FFCC00");
    private int colorGray = Color.LTGRAY;
    private int colorSelectedBrown = Color.parseColor("#4D2E24");
    private int colorTextDefault = Color.WHITE;


    // Alpha value for disabled buttons (dimmed look)
    private static final float DISABLED_ALPHA = 0.5f;
    private static final float ENABLED_ALPHA = 1.0f;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Using R.layout.activity_main to resolve the 'Cannot resolve symbol' error
        setContentView(R.layout.assessment);


        // Initialize Colors (with try-catch for robustness)
        try {
            colorBrown = ContextCompat.getColor(this, R.color.button_brown);
            colorHighlight = ContextCompat.getColor(this, R.color.segment_highlight);
            colorGray = ContextCompat.getColor(this, R.color.segment_gray);


        } catch (Exception e) {
            Toast.makeText(this, "Warning: Missing color resources, using default colors.", Toast.LENGTH_SHORT).show();
        }


        // Setup Questions
        setupQuestionData();


        // --- UI Element Initialization ---
        // CRITICAL: Ensure these IDs exist in your XML layout!
        View backView = findViewById(R.id.button_back);
        if (backView instanceof Button) {
            backButton = (Button) backView;
        } else {
            Toast.makeText(this, "FATAL ERROR: button_back must be a Button!", Toast.LENGTH_LONG).show();
            return;
        }


        nextButton = findViewById(R.id.button_next);
        progressText = findViewById(R.id.text_progress);
        questionText = findViewById(R.id.text_question);
        segmentContainer = findViewById(R.id.layout_progress_segments);
        buttonContainer = findViewById(R.id.layout_answer_buttons);


        if (nextButton == null || progressText == null || questionText == null || segmentContainer == null || buttonContainer == null) {
            Toast.makeText(this, "FATAL ERROR: One or more critical UI IDs are missing in your XML layout!", Toast.LENGTH_LONG).show();
            return;
        }


        // --- Setup Logic ---
        setupAnswerButtons();
        loadQuestion(currentQuestionIndex);


        // NEXT Button setup
        nextButton.setOnClickListener(v -> navigate(1));


        // BACK Button setup
        backButton.setOnClickListener(v -> navigate(-1));
    }


    /**
     * Defines all the questions and adds them to the list.
     */
    private void setupQuestionData() {
        // Total of 9 questions based on the screenshots
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
            // Fallback questions if R.string resources are missing
            questionList.add(new Question("1. Little interest or pleasure in doing things?"));
            questionList.add(new Question("2. Feeling down, depressed, or hopeless?"));
            questionList.add(new Question("3. Trouble falling or staying asleep, or sleeping too much?"));
            questionList.add(new Question("4. Feeling tired or having little energy?"));
            questionList.add(new Question("5. Poor appetite or overeating?"));
            questionList.add(new Question("6. Feeling bad about yourself or that you are a failure?"));
            questionList.add(new Question("7. Trouble concentrating on things?"));
            questionList.add(new Question("8. Moving or speaking so slowly or being restless?"));
            questionList.add(new Question("9. Thoughts that you would be better off dead, or of hurting yourself?"));
            Toast.makeText(this, "Warning: Missing question string resources, using default text.", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Updates the UI based on the current question index.
     */
    private void loadQuestion(int index) {
        if (index < 0 || index >= questionList.size()) return;


        Question currentQ = questionList.get(index);


        // 1. Update Question Text
        questionText.setText(currentQ.questionText);


        // 2. Update Progress Indicator and Text
        progressText.setText((index + 1) + " of " + questionList.size());
        setupProgressIndicator();


        // 3. Update Button State (Selection)
        updateButtonSelection(currentQ.score);


        // 4. Update Navigation Button States (this handles disabling NEXT if score is -1)
        updateNavigationButtons(index, currentQ.score);
    }


    /**
     * Sets up the segmented progress bar visual.
     */
    private void setupProgressIndicator() {
        for (int i = 0; i < segmentContainer.getChildCount(); i++) {
            View segment = segmentContainer.getChildAt(i);


            int tintColor = (i <= currentQuestionIndex) ? colorHighlight : colorGray;


            if (segment.getBackground() != null) {
                if (i < questionList.size()) {
                    segment.getBackground().setColorFilter(new PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN));
                }
            }
        }
    }


    /**
     * Dynamically creates the four answer buttons and adds them to the container.
     */
    private void setupAnswerButtons() {
        // FIX for 'Cannot resolve symbol answer_choices': Use try-catch with fallback
        String[] answers = new String[]{
                "Not at all",
                "Several days",
                "More than half the days",
                "Nearly every day"
        };
        try {
            answers = getResources().getStringArray(R.array.answer_choices);
        } catch (Exception e) {
            Toast.makeText(this, "Warning: Missing answer string array, using default choices.", Toast.LENGTH_SHORT).show();
        }


        buttonContainer.removeAllViews();


        for (int i = 0; i < answers.length; i++) {
            Button button = new Button(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );


            // Set margins
            int margin = (int) (12 * getResources().getDisplayMetrics().density);
            params.bottomMargin = margin;
            button.setLayoutParams(params);


            // Set basic visual properties
            button.setText(answers[i]);
            button.setTextColor(colorTextDefault);
            button.setBackgroundColor(colorBrown);


            // Setting a fixed height
            button.setHeight((int) (60 * getResources().getDisplayMetrics().density));


            // Store reference and set listener
            answerButtons[i] = button;
            final int index = i;
            button.setOnClickListener(v -> selectAnswer(index));


            buttonContainer.addView(button);
        }
    }


    /**
     * Updates the visual state of the answer buttons based on the current question's score.
     */
    private void updateButtonSelection(int selectedScore) {
        // Update button colors
        for (int i = 0; i < answerButtons.length; i++) {
            Button button = answerButtons[i];
            if (button != null) {
                // Highlight the selected answer
                if (i == selectedScore) {
                    button.setBackgroundColor(colorSelectedBrown);
                } else {
                    button.setBackgroundColor(colorBrown);
                }
            }
        }


        // Indicate if the question is unanswered
        questionText.setAlpha((selectedScore != -1) ? ENABLED_ALPHA : DISABLED_ALPHA + 0.2f);
    }


    /**
     * Handles the selection of an answer and updates the UI state.
     */
    private void selectAnswer(int index) {
        // 1. Store the selected answer (score is the index 0-3)
        questionList.get(currentQuestionIndex).score = index;


        // 2. Update button colors
        updateButtonSelection(index);


        // 3. Enable the NEXT button, as an answer has been selected
        updateNavigationButtons(currentQuestionIndex, index);
    }


    /**
     * Handles navigation: +1 for NEXT, -1 for BACK.
     */
    private void navigate(int direction) {
        int nextIndex = currentQuestionIndex + direction;


        // Standard navigation (not the last page)
        if (nextIndex >= 0 && nextIndex < questionList.size()) {
            currentQuestionIndex = nextIndex;
            loadQuestion(currentQuestionIndex);
        } else if (nextIndex == questionList.size()) {
            // Last page: SUBMIT/FINISH button pressed


            // REQ: Disable FINISH until all questions are answered
            if (!allQuestionsAnswered()) {
                int firstUnanswered = findFirstUnansweredQuestionIndex();
                String questionNumber = (firstUnanswered + 1) + "";
                Toast.makeText(this,
                        "Please go back and answer Question " + questionNumber + " before submitting.",
                        Toast.LENGTH_LONG).show();
                return; // Prevent completion
            }


            // If all answered, proceed to submission
            int totalScore = calculateScore();


            Toast.makeText(this, "Assessment Complete! Your total score is: " + totalScore + "/27", Toast.LENGTH_LONG).show();
            // TODO: Add logic to start ResultsActivity here
        }
    }


    /**
     * Checks if every question in the list has a score (i.e., score != -1).
     */
    private boolean allQuestionsAnswered() {
        for (Question q : questionList) {
            if (q.score == -1) {
                return false;
            }
        }
        return true;
    }


    /**
     * Finds the 0-based index of the first unanswered question.
     */
    private int findFirstUnansweredQuestionIndex() {
        for (int i = 0; i < questionList.size(); i++) {
            if (questionList.get(i).score == -1) {
                return i;
            }
        }
        return -1;
    }


    /**
     * Calculates the total score of the assessment (sum of all selected answer indices).
     */
    private int calculateScore() {
        int score = 0;
        for (Question q : questionList) {
            if (q.score != -1) {
                // Scores are 0, 1, 2, or 3 based on the index of the answer selected.
                score += q.score;
            }
        }
        return score;
    }


    /**
     * Updates the enabled/disabled state and appearance of the navigation buttons.
     * @param index The current question index.
     * @param selectedScore The score of the current question (-1 if unanswered).
     */
    private void updateNavigationButtons(int index, int selectedScore) {


        // --- BACK Button Logic ---
        // BACK is disabled only on the first question (index 0).
        boolean isFirstQuestion = (index == 0);
        backButton.setEnabled(!isFirstQuestion);
        backButton.setAlpha(isFirstQuestion ? DISABLED_ALPHA : ENABLED_ALPHA);


        // --- NEXT/SUBMIT Button Logic ---
        boolean isAnswered = (selectedScore != -1);


        if (index == questionList.size() - 1) {
            // Last question: Button is SUBMIT/FINISH
            nextButton.setText("SUBMIT");
            try {
                // Use resource string if available
                nextButton.setText(R.string.button_submit);
            } catch (Exception e) { /* Fallback used */ }


            // SUBMIT is enabled only if an answer is selected for this LAST question.
            nextButton.setEnabled(isAnswered);
            nextButton.setAlpha(isAnswered ? ENABLED_ALPHA : DISABLED_ALPHA);


        } else {
            // All other questions: Button is NEXT
            nextButton.setText("NEXT");
            try {
                // Use resource string if available
                nextButton.setText(R.string.button_next);
            } catch (Exception e) { /* Fallback used */ }


            // NEXT is enabled only if an answer is selected.
            nextButton.setEnabled(isAnswered);
            nextButton.setAlpha(isAnswered ? ENABLED_ALPHA : DISABLED_ALPHA);
        }
    }
}

