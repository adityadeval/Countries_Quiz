package ms.cs.countries_quiz;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Question {
    public static final String DEBUG_TAG = "Question";
    private String countryName;
    private String correctContinent;
    private List<String> incorrectContinents;
    private List<String> correctNeighbors;
    private List<String> incorrectNeighbors;
    private String continentQuestionText;
    private String neighborQuestionText;

    public Question(String countryName, String correctContinent) {
        this.countryName = countryName;
        this.correctContinent = correctContinent;
        this.incorrectContinents = new ArrayList<>();
        this.incorrectNeighbors = new ArrayList<>();
    }
    public Question(String countryName, List<String> correctNeighbors) {
        this.countryName = countryName;
        this.correctNeighbors = correctNeighbors;
        this.incorrectContinents = new ArrayList<>();
        this.incorrectNeighbors = new ArrayList<>();
    }

    public Question(Context context) {
    }

    public void setContinentQuestionText(String continentQuestion) {
        this.continentQuestionText = continentQuestion;
    }
    public String getContinentQuestionText() {
        return continentQuestionText;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCorrectContinent() {
        return correctContinent;
    }

    public void setCorrectContinent(String correctContinent) {
        this.correctContinent = correctContinent;
    }

    public List<String> getIncorrectContinents() {
        return incorrectContinents;
    }

    public void setIncorrectContinents(String incorrectContinent) {
        if (!incorrectContinents.contains(incorrectContinent)) {
            incorrectContinents.add(incorrectContinent);
        }
    }
    public void setNeighborQuestionText(String neighborQuestion) {
        this.neighborQuestionText = neighborQuestion;
    }
    public String getNeighborQuestionText() {
        Log.d( DEBUG_TAG, "getNeighborQuestionText, neighborQuestionText: "+neighborQuestionText );
        return neighborQuestionText;
    }
    public List<String> getCorrectNeighbor() {
        Log.d( DEBUG_TAG, "getCorrectNeighbor, correctNeighbors: "+correctNeighbors );
        return correctNeighbors;
    }

    public void setCorrectNeighbor(List<String> correctNeighbors) {
        this.correctNeighbors = correctNeighbors;
    }

    public List<String> getIncorrectNeighbors() {
        Log.d( DEBUG_TAG, "getIncorrectNeighbors, incorrectNeighbors: "+incorrectNeighbors );
        return incorrectNeighbors;
    }

    public void setIncorrectNeighbors(String incorrectNeighbor) {
        if (!incorrectContinents.contains(incorrectNeighbor)) {
            incorrectNeighbors.add(incorrectNeighbor);
        }
    }
}

