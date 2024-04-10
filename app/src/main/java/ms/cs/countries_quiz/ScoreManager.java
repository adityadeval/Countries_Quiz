package ms.cs.countries_quiz;

public class ScoreManager {
    private static int totalScore = 0;

    public static void addScore(int score) {
        totalScore += score;
    }

    public static int getTotalScore() {
        return totalScore;
    }

    public static void resetScore() {
        totalScore = 0;
    }
}
