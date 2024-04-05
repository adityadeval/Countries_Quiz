package ms.cs.countries_quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    private TableLayout tableLayout_continents;
    private List<Continents> arr_Continents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataManager_obj = new DataManager(this);

        // Below function fetches data from country_continent.csv into table continents.
        // Displays toast once done.
        // This is done in an ASYNC TASK.
        new ContinentsTableWriter().execute();


        arr_Continents = new ArrayList<Continents>();

        tableLayout_continents = findViewById(R.id.table_continents);
        tableLayout_continents.removeAllViews();
        // Button for retrieving records from continents table
        button_Continents = findViewById(R.id.button_continents);
        button_Continents.setOnClickListener( new ContinentsButtonClickListener()) ;
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
            Toast.makeText(MainActivity.this, "Table country_continent has been created", Toast.LENGTH_LONG).show();
            dataManager_obj.close();
            Log.d( DEBUG_TAG, "Contents of country_continent.csv file added to DB" );
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

}