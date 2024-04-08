package ms.cs.countries_quiz;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class QuizManager {
    public static final String DEBUG_TAG = "QuizManager";
    private Map<String,List> quizQuestionsMap;
    private List<Question> quizQuestions;
    private List<Question> continentQuizQuestions;
    private List<Question> neighborQuizQuestions;
    private int currentScore;
    private int questionsAnswered;
    private DataManager dataManager_obj;

    public QuizManager(Context context) {
        this.dataManager_obj = new DataManager(context);
        this.quizQuestionsMap = new HashMap<>();
        this.quizQuestions = new ArrayList<>();
        this.continentQuizQuestions = new ArrayList<>();
        this.neighborQuizQuestions = new ArrayList<>();
        this.currentScore = 0;
        this.questionsAnswered = 0;
    }

    public Map<String,List> startNewQuiz() {
        // Retrieve the list of continents from the database
        dataManager_obj.open();
        List<Continents> continentsList = dataManager_obj.retrieve_ContinentsTable_data();
        List<Neighbours> neighboursList = dataManager_obj.retrieve_NeighboursTable_data();
        Log.d( DEBUG_TAG, "startNewQuiz, continentsList: "+continentsList );
        Log.d( DEBUG_TAG, "startNewQuiz, neighboursList: "+neighboursList );
        // Select 6 unique countries for the quiz
        Set<String> selectedCountries = new HashSet<>();
        Random random = new Random();
        while (selectedCountries.size() < 6) {
            int randomIndex = random.nextInt(continentsList.size());
            Continents continent = continentsList.get(randomIndex);
            String countryName = continent.getCountryName();

            // Check if the country is not already selected
            if (!selectedCountries.contains(countryName)) {
                selectedCountries.add(countryName);
            }
        }

        // Populate the quizQuestions list with questions based on the selected countries
        for (String countryName : selectedCountries) {
            Log.d( DEBUG_TAG, "countryName: "+countryName );
            Log.d( DEBUG_TAG, "questionType: continent" );
            Question continentQuestion = new Question(countryName, getContinentForCountry(countryName));
            String correctContinent = continentQuestion.getCorrectContinent();
            for (String continentName : getAllContinents()) {
                if (!continentName.equals(correctContinent)) {
                    continentQuestion.setIncorrectContinents(continentName);
                }
            }
            continentQuestion.setCorrectContinent(correctContinent);
            continentQuestion.setContinentQuestionText("Which continent does the country " + countryName + " belong to?");
            continentQuizQuestions.add(continentQuestion);
            quizQuestionsMap.put("continent",continentQuizQuestions);
            quizQuestions.add(continentQuestion);

            Log.d( DEBUG_TAG, "questionType: neighbor" );
            Question neighborQuestion = new Question(countryName, getNeighborsForCountry(countryName));
            List<String> correctNeighbor = neighborQuestion.getCorrectNeighbor();
            for (String neighbor : getAllNeighbors()) {
                if (!correctNeighbor.contains(neighbor)) {
                    neighborQuestion.setIncorrectNeighbors(neighbor);
                }
            }
            neighborQuestion.setCorrectNeighbor(correctNeighbor);
            neighborQuestion.setNeighborQuestionText("What is the neighboring country of " + countryName + " ?");
            neighborQuizQuestions.add(neighborQuestion);
            quizQuestionsMap.put("neighbor",neighborQuizQuestions);
            quizQuestions.add(neighborQuestion);

        }

        // Initialize current score and questions answered for the new quiz
        currentScore = 0;
        questionsAnswered = 0;
      //  dataManager_obj.close();
        Log.d( DEBUG_TAG, "quizQuestions:"+quizQuestions );
        return quizQuestionsMap;
    }
    private List<String> getAllContinents() {
        // Retrieve all continent names from the database
        List<Continents> continentsList = dataManager_obj.retrieve_ContinentsTable_data();
        List<String> continentNames = new ArrayList<>();
        for (Continents continent : continentsList) {
            continentNames.add(continent.getContinentName());
        }
        return continentNames;
    }
    private String getContinentForCountry(String countryName) {
        // Retrieve the continent for the given country from the database
        List<Continents> continentsList = dataManager_obj.retrieve_ContinentsTable_data();
        for (Continents continent : continentsList) {
            if (continent.getCountryName().equals(countryName)) {
                return continent.getContinentName();
            }
        }
        return ""; // Return empty string if continent not found (handle accordingly in your logic)
    }
    private List<String> getAllNeighbors() {
        // Retrieve all continent names from the database
        List<Neighbours> neighboursList = dataManager_obj.retrieve_NeighboursTable_data();
        List<String> neighbors = new ArrayList<>();
        for (Neighbours neighbour : neighboursList) {
            neighbors.add(neighbour.getNeighbourName());
        }
        return neighbors;
    }
    private List<String> getNeighborsForCountry(String countryName) {
        Log.d( DEBUG_TAG, "getNeighborsForCountry: " +countryName);
        List<Neighbours> neighboursList = dataManager_obj.retrieve_NeighboursTable_data();
        List<String> neighbors = new ArrayList<>();

        for (Neighbours neighbour : neighboursList) {
            if (neighbour.getCountryName().equals(countryName)) {
                neighbors.add(neighbour.getNeighbourName());
            }
        }
        Log.d( DEBUG_TAG, "neighbors: " +neighbors);
        return neighbors;
    }


    public List<Question> getContinentQuizQuestions() {
        return quizQuestions;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public int getQuestionsAnswered() {
        return questionsAnswered;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    public void setQuestionsAnswered(int questionsAnswered) {
        this.questionsAnswered = questionsAnswered;
    }
}
