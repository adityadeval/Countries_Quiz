package ms.cs.countries_quiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.opencsv.CSVReader;
import java.io.InputStream;
import java.io.IOException;
import java.io.InputStreamReader;

// This class is similar to JobLeadsData class, with a few changes.
// Purpose of this class / This class holds functions for :
// 1) Open and close DB
// 2) Populate tables continents and neighbours from the csv files.
// 3) Retrieve data from continents and neighbours tables, for generating quizzes.
// 4) Store data into quiz_results table.
public class DataManager {
    public static final String DEBUG_TAG = "DataManager";
    private SQLiteDatabase db;

    // Variable to store an object of DBHelper class.
    private SQLiteOpenHelper dbHelper_obj;
    private Context context;

    // Below string array contains all column names present in the continents table.
    // This string would be helpful while writing a database query to retrieve data from continents table.
    private static final String[] allColumns_continents_table = {
            DBHelper.COLUMN_ID_CONTINENTS_TABLE,
            DBHelper.COLUMN_COUNTRY_NAME_CONTINENTS_TABLE,
            DBHelper.COLUMN_CONTINENT_NAME_CONTINENTS_TABLE
    };

    // Below string array contains all column names present in the neighbours table.
    // This string would be helpful while writing a database query to retrieve data from continents table.
    private static final String[] allColumns_neighbours_table = {
            DBHelper.COLUMN_ID_NEIGHBOURS_TABLE,
            DBHelper.COLUMN_COUNTRY_NAME_NEIGHBOURS_TABLE,
            DBHelper.COLUMN_NEIGHBOUR_NAME_NEIGHBOURS_TABLE
    };

    // Below string array contains all column names present in the quiz_results table.
    // This string would be helpful while writing a database query to retrieve data from quiz_results table.
    private static final String[] allColumns_quizresults_table = {
            DBHelper.COLUMN_QUIZID_QUIZRESULTS_TABLE,
            DBHelper.COLUMN_DATE_QUIZRESULTS_TABLE,
            DBHelper.COLUMN_SCORE_QUIZRESULTS_TABLE
    };

    // Constructor initializes the variable dbHelper_obj with instance of DBHelper class.
    public DataManager( Context context ) {
        this.dbHelper_obj = DBHelper.getInstance( context );
        this.context = context;
    }

    // Function to open the database
    public void open() {
        db = dbHelper_obj.getWritableDatabase();
        Log.d( DEBUG_TAG, "DataManager: db is open" );
    }

    // Function to close the database
    public void close() {
        if( dbHelper_obj != null ) {
            dbHelper_obj.close();
            Log.d(DEBUG_TAG, "JobLeadsData: db closed");
        }
    }

    // Function to check if db is open
    public boolean isDBOpen()
    {
        return db.isOpen();
    }

    public void populate_continents_table() {
        // Note : Passing context is required in order to use the getAssets() method used below.
        ContentValues values = new ContentValues();
        try{
            InputStream ip_stream = context.getAssets().open( "country_continent.csv" );
            CSVReader reader = new CSVReader( new InputStreamReader( ip_stream ) );
            String[] nextRow;
            while( ( nextRow = reader.readNext() ) != null ) {
                Log.d(DEBUG_TAG, "Fetched row from csv : "+ nextRow);
                values.put(DBHelper.COLUMN_COUNTRY_NAME_CONTINENTS_TABLE, nextRow[0]);
                values.put(DBHelper.COLUMN_CONTINENT_NAME_CONTINENTS_TABLE, nextRow[1]);
                long id = db.insert(DBHelper.TABLE_CONTINENTS, null, values);
                //continents.setId(id);
                Log.d( DEBUG_TAG, "Stored new country with id: " + id ) ;
            }
        } catch (Exception e) {
            Log.e( DEBUG_TAG, e.toString() );
        }
        //return continents;
    }

}
