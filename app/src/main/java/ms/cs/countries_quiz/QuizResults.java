package ms.cs.countries_quiz;

public class QuizResults {
    private long   quiz_id;
    private String quiz_date;
    private Integer quiz_score;

    public QuizResults(){
        this.quiz_id = -1;
        this.quiz_date = null;
        this.quiz_score = null;
    }

    public QuizResults(String quiz_date, Integer quiz_score){
        this.quiz_id = -1;
        this.quiz_date = quiz_date;
        this.quiz_score = quiz_score;
    }

    public long getQuizId()
    {
        return quiz_id;
    }

    public void setQuizId(long id)
    {
        this.quiz_id = id;
    }

    public String getQuizDate()
    {
        return quiz_date;
    }

    public void setQuizDate(String quiz_date)
    {
        this.quiz_date = quiz_date;
    }

    public Integer getQuizScore()
    {
        return quiz_score;
    }

    public void setQuizScore(Integer quiz_score)
    {
        this.quiz_score = quiz_score;
    }

    public String toString()
    {
        return quiz_id + ": " + quiz_date + " " + quiz_score;
    }

}
