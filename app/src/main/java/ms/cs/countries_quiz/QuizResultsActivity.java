package ms.cs.countries_quiz;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class QuizResultsActivity extends AppCompatActivity {
    private static final String DEBUG_TAG = "QuizResults";
    private DataManager dataManager_obj = null;
    private TableLayout tableLayout_quizresults;
    private List<QuizResults> arr_QuizResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizresults);

        dataManager_obj = new DataManager(this);
        arr_QuizResults = new ArrayList<>();
        tableLayout_quizresults = findViewById(R.id.tableLayout_quizresults);

        new QuizResultsTableReader().execute();
    }

    private class QuizResultsTableReader extends AsyncTask<Void, Void, List<QuizResults>> {
        @Override
        protected List<QuizResults> doInBackground(Void... voids) {
            dataManager_obj.open();
            return dataManager_obj.retrieve_QuizResultsTable_data();
        }

        @Override
        protected void onPostExecute(List<QuizResults> list_QuizResults) {
            super.onPostExecute(list_QuizResults);

            // Add data rows
            for (QuizResults quizresults : list_QuizResults) {
                TableRow row = new TableRow(QuizResultsActivity.this);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                row.setLayoutParams(lp);

                TextView quizidView = new TextView(QuizResultsActivity.this);
                quizidView.setText(String.valueOf(quizresults.getQuizId()));
                row.addView(quizidView);

                TextView quizdateView = new TextView(QuizResultsActivity.this);
                quizdateView.setText(quizresults.getQuizDate());
                row.addView(quizdateView);

                TextView quizscoreView = new TextView(QuizResultsActivity.this);
                int score = quizresults.getQuizScore();
                quizscoreView.setText(String.valueOf(score));
                row.addView(quizscoreView);

                // Add the TableRow to the TableLayout.
                tableLayout_quizresults.addView(row);
            }

            dataManager_obj.close();
        }
    }
}
