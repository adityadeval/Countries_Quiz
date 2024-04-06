package ms.cs.countries_quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String DEBUG_TAG = "MainActivity";
    private DataManager dataManager_obj = null;
    private Button button_Continents;
    private Button button_Neighbours;
    private Button button_quizscore;
    private Button button_quizresults;
    private TableLayout tableLayout_continents;
    private TableLayout tableLayout_neighbours;
    private TableLayout tableLayout_quizresults;
    private EditText et_quizscore;
    private int quizScore;
    private List<Continents> arr_Continents;
    private List<Neighbours> arr_Neighbours;
    private List<QuizResults> arr_QuizResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Below object of DataManager class will be used for many tasks in MainActivity.
        // Its Most imp methods are the one used for insertions and fetching of data from database tables.
        // It also contains object of DBHelper, which helps in database creation, opening and closing the database.
        dataManager_obj = new DataManager(this);

        // Below function fetches data from country_continent.csv into table continents.
        // Displays toast once done.
        // This is done in an ASYNC TASK.
        new ContinentsTableWriter().execute();
        new NeighboursTableWriter().execute();


        arr_Continents = new ArrayList<Continents>();
        arr_Neighbours = new ArrayList<Neighbours>();
        arr_QuizResults = new ArrayList<QuizResults>();

        tableLayout_continents = findViewById(R.id.table_continents);
        tableLayout_continents.removeAllViews();

        tableLayout_neighbours = findViewById(R.id.table_neighbours);
        tableLayout_neighbours.removeAllViews();

        tableLayout_quizresults = findViewById(R.id.table_quizresults);
        tableLayout_quizresults.removeAllViews();

        et_quizscore = findViewById(R.id.et_quizscore);


        // Button for retrieving records from continents table
        button_Continents = findViewById(R.id.button_continents);
        button_Continents.setOnClickListener( new ContinentsButtonClickListener()) ;

        button_Neighbours = findViewById(R.id.button_neighbours);
        button_Neighbours.setOnClickListener(new NeighboursButtonClickListener());

        button_quizscore = findViewById(R.id.button_quizscore);
        button_quizscore.setOnClickListener(new QuizScoreButtonClickListener());

        button_quizresults = findViewById(R.id.button_quizresults);
        button_quizresults.setOnClickListener(new QuizResultsButtonClickListener());
    }

    private class ContinentsButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            // IMP NOTE : Here, the same dataManager_obj is being used for storing and retrieving data from continents table.
            // However, in case, the storing and retrieving was done in different activities, we would have to create objects
            // of DataManager in both activities. DataManager creates instance of DBHelper which creates a database.
            // It is okay, as only one instance of DataManager would be used at a time as it is a Singleton class.
            dataManager_obj.open();
            new ContinentsTableReader().execute();

        }
    }

    private class NeighboursButtonClickListener implements View.OnClickListener {
        public void onClick(View v) {
            dataManager_obj.open();
            new NeighboursTableReader().execute();
        }
    }

    private class QuizScoreButtonClickListener implements View.OnClickListener {

        public void onClick(View v) {

            String score_string = et_quizscore.getText().toString();
            try {
                    quizScore = Integer.parseInt(score_string);
                // Use quizScore for your operations
            } catch (NumberFormatException e) {
                // Handle the exception, maybe show an error message to the user
                Toast.makeText(MainActivity.this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
            }

            dataManager_obj.open();
            new QuizResultsTableWriter().execute(quizScore);

        }
    }

    private class QuizResultsButtonClickListener implements View.OnClickListener {
        public void onClick(View v) {
            dataManager_obj.open();
            new QuizResultsTableReader().execute();
        }
    }

    public class ContinentsTableWriter extends AsyncTask<Void, Void> {

        // This method will run as a background process to write into db.
        // It will be automatically invoked by Android, when we call the execute method
        // in the onClick listener of the Save button.
        @Override
        protected Void doInBackground(Void... voids) {
            dataManager_obj.open();
            dataManager_obj.populate_continents_table();
            return null;
        }

        // This method will be automatically called by Android once the writing to the database
        // in a background process has finished.  Note that doInBackground returns a JobLead object.
        // That object will be passed as argument to onPostExecute.
        // onPostExecute is like the notify method in an asynchronous method call discussed in class.
        @Override
        protected void onPostExecute( Void aVoid) {
            Toast.makeText(MainActivity.this, "Table continents has been created", Toast.LENGTH_LONG).show();
            dataManager_obj.close();
            Log.d( DEBUG_TAG, "Contents of country_continent.csv file added to DB" );
        }
    }

    public class NeighboursTableWriter extends AsyncTask<Void, Void> {
        protected Void doInBackground(Void... voids) {
            dataManager_obj.open();
            dataManager_obj.populate_neighbours_table();
            return null;
        }

        protected void onPostExecute( Void aVoid) {
            Toast.makeText(MainActivity.this, "Table neighbours has been created", Toast.LENGTH_SHORT).show();
            dataManager_obj.close();
            Log.d( DEBUG_TAG, "Contents of country_neighbours.csv file added to DB" );
        }
    }

    public class QuizResultsTableWriter extends AsyncTask<Integer, Void> {
        protected Void doInBackground(Integer... scores) {
            dataManager_obj.open();
            dataManager_obj.populate_quizresults_table(scores[0]);
            return null;
        }

        protected void onPostExecute( Void aVoid) {
            Toast.makeText(MainActivity.this, "Given score has been saved", Toast.LENGTH_SHORT).show();
            dataManager_obj.close();
            Log.d(DEBUG_TAG, "Given score has been added to DB");
        }

    }

    private class ContinentsTableReader extends AsyncTask<Void, List<Continents>> {
        @Override
        protected List<Continents> doInBackground( Void... params ) {
            dataManager_obj.open();
            List<Continents> arr_Continents = dataManager_obj.retrieve_ContinentsTable_data();
            return arr_Continents;
        }

        protected void onPostExecute( List<Continents> list_Continents ) {
            // For actual project implementation, make use of below arr_Continents. It's going to contain all data
            // retrieved from Continents table in the form of multiple Continents class objects.
            arr_Continents.addAll(list_Continents);

            // Below lines of code are for displaying the contents of Continents table created on the screen.
            // Won't be necessary for final project.
            for (Continents continent : list_Continents) {
                TableRow row = new TableRow(MainActivity.this);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                row.setLayoutParams(lp);

                TextView idView = new TextView(MainActivity.this);
                idView.setText(String.valueOf(continent.getId()));
                row.addView(idView);

                // Create a TextView for the country name.
                TextView countryView = new TextView(MainActivity.this);
                countryView.setText(continent.getCountryName());
                row.addView(countryView);

                // Create a TextView for the continent name.
                TextView continentView = new TextView(MainActivity.this);
                continentView.setText(continent.getContinentName());
                row.addView(continentView);

                // Add the TableRow to the TableLayout.
                tableLayout_continents.addView(row);
            }

            dataManager_obj.close();

        }

    }

    private class QuizResultsTableReader extends AsyncTask<Void, List<QuizResults>> {
        protected List<QuizResults> doInBackground( Void... params ) {
            dataManager_obj.open();
            List<QuizResults> arr_QuizResults = dataManager_obj.retrieve_QuizResultsTable_data();
            return arr_QuizResults;
        }

        protected void onPostExecute( List<QuizResults> list_QuizResults ) {
            arr_QuizResults.addAll(list_QuizResults);
            tableLayout_quizresults.removeAllViews();

            for (QuizResults quizresults : list_QuizResults) {
                TableRow row = new TableRow(MainActivity.this);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                row.setLayoutParams(lp);

                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                // Convert dp to pixels for consistent spacing across different screen densities
                int marginInPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
                layoutParams.setMargins(marginInPixels, 0, marginInPixels, 0);

                TextView quizidView = new TextView(MainActivity.this);
                quizidView.setText(String.valueOf(quizresults.getQuizId()));
                quizidView.setLayoutParams(layoutParams);
                row.addView(quizidView);

                TextView quizdateView = new TextView(MainActivity.this);
                quizdateView.setText(quizresults.getQuizDate());
                quizidView.setLayoutParams(layoutParams);
                row.addView(quizdateView);

                TextView quizscoreView = new TextView(MainActivity.this);
                int score = quizresults.getQuizScore();
                quizscoreView.setText(String.valueOf(score));
                quizidView.setLayoutParams(layoutParams);
                row.addView(quizscoreView);

                // Add the TableRow to the TableLayout.
                tableLayout_quizresults.addView(row);
            }

            dataManager_obj.close();
        }
    }


    private class NeighboursTableReader extends AsyncTask<Void, List<Neighbours>> {
        @Override
        protected List<Neighbours> doInBackground( Void... params ) {
            dataManager_obj.open();
            List<Neighbours> arr_Neighbours = dataManager_obj.retrieve_NeighboursTable_data();
            return arr_Neighbours;
        }

        protected void onPostExecute( List<Neighbours> list_Neighbours ) {
            // For actual project implementation, make use of below arr_Continents. It's going to contain all data
            // retrieved from Continents table in the form of multiple Continents class objects.
            arr_Neighbours.addAll(list_Neighbours);

            // Below lines of code are for displaying the contents of Continents table created on the screen.
            // Won't be necessary for final project.
            for (Neighbours neighbour : list_Neighbours) {
                TableRow row = new TableRow(MainActivity.this);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                row.setLayoutParams(lp);

                TextView idView = new TextView(MainActivity.this);
                idView.setText(String.valueOf(neighbour.getId()));
                row.addView(idView);

                // Create a TextView for the country name.
                TextView countryView = new TextView(MainActivity.this);
                countryView.setText(neighbour.getCountryName());
                row.addView(countryView);

                // Create a TextView for the continent name.
                TextView continentView = new TextView(MainActivity.this);
                continentView.setText(neighbour.getNeighbourName());
                row.addView(continentView);

                // Add the TableRow to the TableLayout.
                tableLayout_neighbours.addView(row);
            }

            dataManager_obj.close();

        }

    }

}