package ms.cs.countries_quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String DEBUG_TAG = "MainActivity";
    private DataManager dataManager_obj = null;
    private Button button_Continents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataManager_obj = new DataManager(this);

        button_Continents = findViewById(R.id.button_continents);
        button_Continents.setOnClickListener( new ContinentsButtonClickListener()) ;
    }

    private class ContinentsButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            new ContinentsTableWriter().execute();
        }
    }

    public class ContinentsTableWriter extends AsyncTask<Void, Void> {

        // This method will run as a background process to write into db.
        // It will be automatically invoked by Android, when we call the execute method
        // in the onClick listener of the Save button.
        @Override
        protected Void doInBackground(Void... voids) {
            dataManager_obj.populate_continents_table();
            return null;
        }

        // This method will be automatically called by Android once the writing to the database
        // in a background process has finished.  Note that doInBackground returns a JobLead object.
        // That object will be passed as argument to onPostExecute.
        // onPostExecute is like the notify method in an asynchronous method call discussed in class.
        @Override
        protected void onPostExecute( Void aVoid) {
            Log.d( DEBUG_TAG, "Contents of country_continent.csv file added to DB" );
        }
    }

}