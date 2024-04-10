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
        neighborAnswer = "";

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
            String continentAnswer = "";
            continentAnswer  = checked_rb_que1.getText().toString();
            int continent_score = calculateScore_continent(continentAnswer);
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
    }

    // Method to calculate the quiz score for neighbor questions
    private int calculateScore_neighbor() {
        Log.d(DEBUG_TAG, "calculateScore_neighbor");
        int score = 0;
        for (Question question : neighborQuizQuestions) {
            if (question.getCorrectNeighbor().contains(neighborAnswer)) {
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

    public static int getNumberOfQuestions() {
        Log.d( DEBUG_TAG, "getNumberOfQuestions, quizQuestions: "+quizQuestions );
        return quizQuestions != null ? quizQuestions.size() : 0;
    }

    private void saveScoreToDatabase(int score) {
        // Call AsyncTask to save the score
        new SplashScreen.QuizResultsTableWriter().execute(score);
    }

    private int calculateScore_continent(String continentAnswer) {
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
