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
    private Button button_Neighbours;
    private TableLayout tableLayout_continents;
    private TableLayout tableLayout_neighbours;
    private List<Continents> arr_Continents;
    private List<Neighbours> arr_Neighbours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataManager_obj = new DataManager(this);

        // Below function fetches data from country_continent.csv into table continents.
        // Displays toast once done.
        // This is done in an ASYNC TASK.
        new ContinentsTableWriter().execute();
        new NeighboursTableWriter().execute();


        arr_Continents = new ArrayList<Continents>();
        arr_Neighbours = new ArrayList<Neighbours>();

        tableLayout_continents = findViewById(R.id.table_continents);
        tableLayout_continents.removeAllViews();

        tableLayout_neighbours = findViewById(R.id.table_neighbours);
        tableLayout_neighbours.removeAllViews();

        // Button for retrieving records from continents table
        button_Continents = findViewById(R.id.button_continents);
        button_Continents.setOnClickListener( new ContinentsButtonClickListener()) ;

        button_Neighbours = findViewById(R.id.button_neighbours);
        button_Neighbours.setOnClickListener(new NeighboursButtonClickListener());
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
            Toast.makeText(MainActivity.this, "Table neighbours has been created", Toast.LENGTH_LONG).show();
            dataManager_obj.close();
            Log.d( DEBUG_TAG, "Contents of country_neighbours.csv file added to DB" );
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