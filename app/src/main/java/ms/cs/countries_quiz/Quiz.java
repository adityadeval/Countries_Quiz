package ms.cs.countries_quiz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Quiz {
    private List<Question> questions;
    private Date date;
    private int currentScore;
    private int questionsAnswered;

    public Quiz() {
        this.questions = new ArrayList<>();
        this.date = new Date();
        this.currentScore = 0;
        this.questionsAnswered = 0;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    public int getQuestionsAnswered() {
        return questionsAnswered;
    }

    public void setQuestionsAnswered(int questionsAnswered) {
        this.questionsAnswered = questionsAnswered;
    }
}

