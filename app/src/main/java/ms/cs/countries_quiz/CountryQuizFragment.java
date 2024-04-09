package ms.cs.countries_quiz;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

public class CountryQuizFragment extends Fragment {
    public static final String DEBUG_TAG = "CountryQuizFragment";
    private static Context context = null;
    private QuizManager quizManager_obj;
    private Question question_obj;
    private Map<String,List> quizQuestionsMap;
    private static List<Question> quizQuestions;
    private static List<Question> continentQuizQuestions;
    private static List<Question> neighborQuizQuestions;
    private int questionNumber;
    private String userAnswer;

    /*
    public CountryQuizFragment(Context context) {
        Log.d( DEBUG_TAG, "Inside Constructor" );
        this.context = context;

        // Below lines moved in onAttach()
        this.quizManager_obj = new QuizManager(this.context);
        this.question_obj = new Question(this.context);

        // Moved into onCreate()
        this.quizQuestions = new ArrayList<>();
        this.continentQuizQuestions = new ArrayList<>();
        this.neighborQuizQuestions = new ArrayList<>();


        this.quizQuestionsMap = this.quizManager_obj.startNewQuiz();
    }

     */

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(DEBUG_TAG, "Inside onAttach");
        this.quizManager_obj = new QuizManager(context);
        this.question_obj = new Question(context);
    }

    public static CountryQuizFragment newInstance(int questionNumber) {
        Log.d(DEBUG_TAG, "Creating new fragment position/questionNumber is " + questionNumber);
        CountryQuizFragment fragment = new CountryQuizFragment();
        Bundle args = new Bundle();
        args.putInt("questionNumber", questionNumber);
        fragment.setArguments(args);
        Log.d( DEBUG_TAG, "fragment:"+fragment );
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d( DEBUG_TAG, "onCreate, getArguments:"+questionNumber );

            quizQuestions = new ArrayList<>();
            continentQuizQuestions = new ArrayList<>();
            neighborQuizQuestions = new ArrayList<>();

            if (getArguments() != null) {
                questionNumber = getArguments().getInt("questionNumber");
            }

            this.quizQuestionsMap = this.quizManager_obj.startNewQuiz();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d( DEBUG_TAG, "onCreateView, getArguments:"+questionNumber );
        View rootView = inflater.inflate(R.layout.activity_fragment, container, false);

        // Initialize userAnswer as empty string
        userAnswer = "";

        // Set up swipe gesture detection
        rootView.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                // Record user's answer when swiping left
                recordUserAnswer();
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        continentQuizQuestions = quizQuestionsMap.get("continent");
        neighborQuizQuestions = quizQuestionsMap.get("neighbor");
        Log.d(DEBUG_TAG, "onViewCreated, continentQuizQuestions: " + continentQuizQuestions);
        Log.d(DEBUG_TAG, "onViewCreated, neighborQuestions: " + neighborQuizQuestions);

        TextView question1TextView = view.findViewById(R.id.textView3);
        RadioGroup radioGroup1 = view.findViewById(R.id.radioGroup1);

        TextView question2TextView = view.findViewById(R.id.textView4);
        RadioGroup radioGroup2 = view.findViewById(R.id.radioGroup2);

        if (continentQuizQuestions != null && questionNumber >= 0 && questionNumber < continentQuizQuestions.size()) {
            Log.d(DEBUG_TAG, "continentQuizQuestions inside: " + continentQuizQuestions);
            Log.d(DEBUG_TAG, "questionNumber: " + questionNumber);
            quizQuestions.addAll(continentQuizQuestions);
            Question question_obj = continentQuizQuestions.get(questionNumber);
            String questionText = question_obj.getContinentQuestionText();
            question1TextView.setText(questionText);

            // Get the correct continent and incorrect continents for the current question
            String correctContinent = question_obj.getCorrectContinent();
            List<String> incorrectContinents = question_obj.getIncorrectContinents();

            // Create a list of all options (3 incorrect + 1 correct) and shuffle it
            List<String> allOptions = new ArrayList<>();
            Random random = new Random();

            // Add 3 random incorrect neighbors
            for (int i = 0; i < 3 && i < incorrectContinents.size(); i++) {
                int randomIndex = random.nextInt(incorrectContinents.size());
                allOptions.add(incorrectContinents.get(randomIndex));
                incorrectContinents.remove(randomIndex);  // Avoid repeating options
            }
            allOptions.add(correctContinent);
            Collections.shuffle(allOptions);

            // Set the radio button texts based on the shuffled list
            if (allOptions.size() >= 4) {
                ((RadioButton) radioGroup1.getChildAt(0)).setText(allOptions.get(0));
                ((RadioButton) radioGroup1.getChildAt(1)).setText(allOptions.get(1));
                ((RadioButton) radioGroup1.getChildAt(2)).setText(allOptions.get(2));
                ((RadioButton) radioGroup1.getChildAt(3)).setText(allOptions.get(3));
            }
        }

        if (neighborQuizQuestions != null && questionNumber >= 0 && questionNumber < neighborQuizQuestions.size()) {
            Log.d(DEBUG_TAG, "neighborQuizQuestions inside: " + neighborQuizQuestions);
            Log.d(DEBUG_TAG, "questionNumber: " + questionNumber);
            quizQuestions.addAll(neighborQuizQuestions);
            Question question_obj = neighborQuizQuestions.get(questionNumber);
            String questionText = question_obj.getNeighborQuestionText();
            Log.d(DEBUG_TAG, "questionText: " + questionText);
            question2TextView.setText(questionText);

            // Get the correct continent and incorrect continents for the current question
            List<String> correctNeighbors = question_obj.getCorrectNeighbor();
            List<String> incorrectNeighbors = question_obj.getIncorrectNeighbors();

            Log.d(DEBUG_TAG, "correctNeighbors: " + correctNeighbors);
            Log.d(DEBUG_TAG, "incorrectNeighbors: " + incorrectNeighbors);

            // Create a list of all options (3 incorrect + 1 correct) and shuffle it
            List<String> allOptions = new ArrayList<>();
            Random random = new Random();

            // Add 3 random incorrect neighbors
            for (int i = 0; i < 3 && i < incorrectNeighbors.size(); i++) {
                int randomIndex = random.nextInt(incorrectNeighbors.size());
                allOptions.add(incorrectNeighbors.get(randomIndex));
                incorrectNeighbors.remove(randomIndex);  // Avoid repeating options
            }

            // Add one correct neighbor if available
            if (correctNeighbors != null && !correctNeighbors.isEmpty()) {
                int correctIndex = random.nextInt(correctNeighbors.size());
                allOptions.add(correctNeighbors.get(correctIndex));
            }else{
                allOptions.add("No neighbors");
            }
            Log.d(DEBUG_TAG, "allOptions: " + allOptions);
            // Shuffle the options
            Collections.shuffle(allOptions);

            // Set the radio button texts based on the shuffled list
            if (allOptions.size() >= 4) {
                ((RadioButton) radioGroup2.getChildAt(0)).setText(allOptions.get(0));
                ((RadioButton) radioGroup2.getChildAt(1)).setText(allOptions.get(1));
                ((RadioButton) radioGroup2.getChildAt(2)).setText(allOptions.get(2));
                ((RadioButton) radioGroup2.getChildAt(3)).setText(allOptions.get(3));
            }
        }

        // Check if all questions are answered after swiping left
        if (isAllQuestionsAnswered()) {
            displayQuizResult();
        }
    }

    private void recordUserAnswer() {
        RadioGroup radioGroup1 = getView().findViewById(R.id.radioGroup1);
        int selectedRadioButtonId = radioGroup1.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = getView().findViewById(selectedRadioButtonId);
        if (selectedRadioButton != null) {
            userAnswer = selectedRadioButton.getText().toString();
        }
    }
    // Add this method to check if all questions are answered
    private boolean isAllQuestionsAnswered() {
        return userAnswer != null && !userAnswer.isEmpty();
    }

    // Add this method to calculate and display quiz result
    private void displayQuizResult() {
        if (isAllQuestionsAnswered()) {
            // Calculate quiz score
            int score = calculateScore();

            // Display quiz result in a Toast
            String resultMessage = "Quiz Completed!\nYour Score: " + score;
            Toast.makeText(getContext(), resultMessage, Toast.LENGTH_LONG).show();

            // Navigate to the result fragment
            navigateToResultFragment(score);
        } else {
            // Handle case where not all questions are answered
            Toast.makeText(getContext(), "Please answer all questions.", Toast.LENGTH_SHORT).show();
        }
    }

    // Add this method to calculate the quiz score
    private int calculateScore() {
        int score = 0;
        for (Question question : quizQuestions) {
            if (question.getCorrectContinent().equals(userAnswer)) {
                score++;
            }
        }
        return score;
    }
    // Add this method to navigate to the result fragment
    private void navigateToResultFragment(int score) {
        // Create a new instance of the ResultFragment
        ResultFragment resultFragment = ResultFragment.newInstance(score);

        // Get the FragmentManager and start a transaction
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Replace the current fragment with the result fragment
        transaction.replace(R.id.fragment_container, resultFragment);

        // Add the transaction to the back stack and commit
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public static int getNumberOfQuestions() {
        Log.d( DEBUG_TAG, "getNumberOfQuestions, quizQuestions: "+quizQuestions );
        return quizQuestions != null ? quizQuestions.size() : 0;
    }
}
