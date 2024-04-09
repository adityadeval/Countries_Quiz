package ms.cs.countries_quiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class SplashScreen extends AppCompatActivity {
    private static final String DEBUG_TAG = "SplashScreen";
    private DataManager dataManager_obj = null;
    private int quizScore;
    private List<Continents> arr_Continents;
    private List<Neighbours> arr_Neighbours;
    private List<QuizResults> arr_QuizResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
            // Below object of DataManager class will be used for many tasks in MainActivity.
            // Its Most imp methods are the one used for insertions and fetching of data from database tables.
            // It also contains object of DBHelper, which helps in database creation, opening and closing the database.
            dataManager_obj = new DataManager(this);

            // Below function fetches data from country_continent.csv into table continents.
            // Displays toast once done.
            // This is done in an ASYNC TASK.
            new SplashScreen.ContinentsTableWriter().execute();
            new SplashScreen.NeighboursTableWriter().execute();
            //  new SplashScreen.QuizResultsTableWriter().execute();

            arr_Continents = new ArrayList<Continents>();
            arr_Neighbours = new ArrayList<Neighbours>();
            arr_QuizResults = new ArrayList<QuizResults>();
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
        //    Toast.makeText(SplashScreen.this, "Table continents has been created", Toast.LENGTH_LONG).show();
     //       dataManager_obj.close();
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
        //    Toast.makeText(SplashScreen.this, "Table neighbours has been created", Toast.LENGTH_SHORT).show();
      //      dataManager_obj.close();
            Log.d( DEBUG_TAG, "Contents of country_neighbours.csv file added to DB" );
        }
    }

    public class QuizResultsTableWriter extends AsyncTask<Integer, Void> {
        protected Void doInBackground(Integer... scores) {
            dataManager_obj.open();
            dataManager_obj.populate_quizresults_table(6);
            return null;
        }

        protected void onPostExecute( Void aVoid) {
       //     Toast.makeText(SplashScreen.this, "Given score has been saved", Toast.LENGTH_SHORT).show();
          //  dataManager_obj.close();
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
           // tableLayout_quizresults.removeAllViews();
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
            dataManager_obj.close();
        }
    }

    public void navigateToQuizFragment(View view) {
        // Intent to start MainActivity (which will host your QuizFragment)

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        // Finish this activity so it's removed from the back stack
        finish();
    }

    public void navigateToResultsFragment(View view) {
        // Intent to start QuizResultsActivity
        Log.d(DEBUG_TAG, "navigateToResultsFragment");
        Intent intent = new Intent(this, QuizResultsActivity.class);
        startActivity(intent);

        // Finish this activity so it's removed from the back stack
        finish();
    }

    public void navigateToExit(View view) {
        finish();
    }
}