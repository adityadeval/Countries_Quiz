package ms.cs.countries_quiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Scroller;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ResultFragment extends Fragment {
    private static final String DEBUG_TAG = "ResultFragment";
    private static final String ARG_SCORE = "score";

    public ResultFragment() {
        // Required empty public constructor
    }

    /*
    public static ResultFragment newInstance(int score) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SCORE, score);
        fragment.setArguments(args);
        Log.d(DEBUG_TAG, "fragment " + fragment);
        return fragment;
    }

     */

    public static ResultFragment newInstance() {
        ResultFragment fragment = new ResultFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(DEBUG_TAG, "onCreateView ");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(DEBUG_TAG, "onViewCreated ");

        // Get the score from arguments
        // int score = getArguments() != null ? getArguments().getInt(ARG_SCORE) : 0;
        int score = ScoreManager.getTotalScore();
        Log.d(DEBUG_TAG, "score: "+score);
        // Display the score in a TextView
        TextView scoreTextView = view.findViewById(R.id.scoreTextView);
        scoreTextView.setText("Your Score: " + score);

        Button newQuizButton = view.findViewById(R.id.newQuizButton);
        newQuizButton.setOnClickListener(v -> navigateToQuizFragment(v));

        Button scoreButton = view.findViewById(R.id.scoreButton);
        scoreButton.setOnClickListener(v -> navigateToResultsFragment(v));

        Button exitButton = view.findViewById(R.id.exitButton);
        exitButton.setOnClickListener(v -> navigateToExit(v));
    }
    public void navigateToQuizFragment(View view) {
        // Intent to start MainActivity (which will host your QuizFragment)

        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        if (getActivity() != null) {
            getActivity().finish(); // Close the current activity
        }

        // Finish this activity so it's removed from the back stack
      //  finish();
    }

    public void navigateToResultsFragment(View view) {
        // Intent to start QuizResultsActivity
        Log.d(DEBUG_TAG, "navigateToResultsFragment");
        Intent intent = new Intent(getContext(), QuizResultsActivity.class);
        startActivity(intent);

        // Finish this activity so it's removed from the back stack
     //  finish();
    }
    public void navigateToExit(View view) {
        // Finish this activity so it's removed from the back stack
        //   finish();
        if (getActivity() != null) {
            getActivity().finish();
        }
    }
}

