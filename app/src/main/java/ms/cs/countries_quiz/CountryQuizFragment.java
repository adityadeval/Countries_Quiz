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
    private String continentAnswer;
    private String neighborAnswer;

    public CountryQuizFragment(Context context) {
        Log.d( DEBUG_TAG, "Inside Constructor" );
        this.context = context;
        this.quizManager_obj = new QuizManager(this.context);
        this.question_obj = new Question(this.context);
        this.quizQuestions = new ArrayList<>();
        this.continentQuizQuestions = new ArrayList<>();
        this.neighborQuizQuestions = new ArrayList<>();
        this.quizQuestionsMap = this.quizManager_obj.startNewQuiz();
    }

    public static CountryQuizFragment newInstance(int questionNumber) {
        CountryQuizFragment fragment = new CountryQuizFragment(context);
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
        if (getArguments() != null) {
            questionNumber = getArguments().getInt("questionNumber");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d( DEBUG_TAG, "onCreateView" );
        View rootView = inflater.inflate(R.layout.activity_fragment, container, false);

        // Initialize userAnswer as empty string
        continentAnswer = "";
        neighborAnswer = "";

        setupSwipeListener(rootView);

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
        // Creating and displaying first question is done at this point

        radioGroup1.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton checked_rb_que1 = getView().findViewById(checkedId);
            continentAnswer = checked_rb_que1.getText().toString();
            int continent_score = calculateScore_continent();
            Log.d("DEBUG_TAG", "Storing Continent question score to DB : " + continent_score);
            saveScoreToDatabase(continent_score);
            // Logic to save the selected radio button
            // For example, save the checkedId or associated data in the fragment's instance variables or shared preferences, or any persistent storage you prefer.
        });

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


        /*
        // Check if all questions are answered after swiping left
        if (isAllQuestionsAnswered()) {
            Log.d( DEBUG_TAG, "isAllQuestionsAnswered condition satisfied, call displayQuizResult" );
            displayQuizResult();
        }

         */
    }
    private void recordUserAnswer() {
        Log.d(DEBUG_TAG, "recordUserAnswer: ");
        RadioGroup radioGroup1 = getView().findViewById(R.id.radioGroup1);
        RadioGroup radioGroup2 = getView().findViewById(R.id.radioGroup2);

        int selectedRadioButtonId1 = radioGroup1.getCheckedRadioButtonId();
        RadioButton selectedRadioButton1 = getView().findViewById(selectedRadioButtonId1);
        if (selectedRadioButton1 != null) {
            continentAnswer = selectedRadioButton1.getText().toString();
        }

        int selectedRadioButtonId2 = radioGroup2.getCheckedRadioButtonId();
        RadioButton selectedRadioButton2 = getView().findViewById(selectedRadioButtonId2);
        if (selectedRadioButton2 != null) {
            neighborAnswer = selectedRadioButton2.getText().toString();
        }
        Log.d(DEBUG_TAG, "continentAnswer: "+continentAnswer);
        Log.d(DEBUG_TAG, "neighborAnswer: "+neighborAnswer);
    }

    // Add this method to check if all questions are answered
    private boolean isAllQuestionsAnswered() {
        Log.d(DEBUG_TAG, "isAllQuestionsAnswered, continentAnswer: "+neighborAnswer);
        Log.d(DEBUG_TAG, "isAllQuestionsAnswered, neighborAnswer: "+neighborAnswer);
        return continentAnswer != null && !continentAnswer.isEmpty() && neighborAnswer != null && !neighborAnswer.isEmpty();
    }

    // Add this method to calculate and display quiz result
    private void displayQuizResult() {
        Log.d(DEBUG_TAG, "displayQuizResult");
        if (isAllQuestionsAnswered()) {
            // Calculate quiz score
            int score = calculateScore();
            Log.d(DEBUG_TAG, "score: "+score);

            // Save score to database
            saveScoreToDatabase(score);

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

    // Add this method to calculate the quiz score for both questions
    private int calculateScore() {
        Log.d(DEBUG_TAG, "calculateScore");
        int score = 0;
        for (Question question : quizQuestions) {
            if (question.getCorrectContinent().equals(continentAnswer) || question.getCorrectNeighbor().contains(neighborAnswer)) {
                score++;
            }
        }
        Log.d(DEBUG_TAG, "score: "+score);
        return score;
    }

    // Add this method to navigate to the result fragment
    private void navigateToResultFragment(int score) {
        Log.d(DEBUG_TAG, "navigateToResultFragment");
        // Create a new instance of the ResultFragment
        ResultFragment resultFragment = ResultFragment.newInstance(score);

        // Get the FragmentManager and start a transaction
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Replace the current fragment with the result fragment
        transaction.replace(R.id.fragment_quiz, resultFragment);

        // Add the transaction to the back stack and commit
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void setupSwipeListener(View view) {
        OnSwipeTouchListener.OnSwipeListener swipeListener = new OnSwipeTouchListener.OnSwipeListener() {
            @Override
            public void onSwipeLeft() {
                // Implement what happens on swipe left
                Toast.makeText(getContext(), "Swiped Left", Toast.LENGTH_SHORT).show();
                recordUserAnswer();
            }

            @Override
            public void onSwipeRight() {
                // Implement what happens on swipe right
                Toast.makeText(getContext(), "Swiped Right", Toast.LENGTH_SHORT).show();
                // Handle the swipe as needed
            }
        };

        OnSwipeTouchListener touchListener = new OnSwipeTouchListener(getActivity(), swipeListener);
        view.setOnTouchListener(touchListener);
    }

    public static int getNumberOfQuestions() {
        Log.d( DEBUG_TAG, "getNumberOfQuestions, quizQuestions: "+quizQuestions );
        return quizQuestions != null ? quizQuestions.size() : 0;
    }

    private void saveScoreToDatabase(int score) {
        // Call AsyncTask to save the score
        new SplashScreen.QuizResultsTableWriter().execute(score);
    }

    private int calculateScore_continent() {
        Log.d(DEBUG_TAG, "calculateScore_continent");
        int score = 0;
        for (Question question : continentQuizQuestions) {
            if (question.getCorrectContinent().equals(continentAnswer)) {
                Log.d("DEBUG_TAG", "Correct answer is " + question.getCorrectContinent() + "Selected answer" + continentAnswer);
                score++;
            }
        }
        Log.d(DEBUG_TAG, "score: "+score);
        return score;
    }


}
