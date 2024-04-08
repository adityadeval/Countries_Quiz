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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.database.Cursor;

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
                //Log.d(DEBUG_TAG, "Fetched row from csv : "+ nextRow);
                values.put(DBHelper.COLUMN_COUNTRY_NAME_CONTINENTS_TABLE, nextRow[0]);
                values.put(DBHelper.COLUMN_CONTINENT_NAME_CONTINENTS_TABLE, nextRow[1]);
                long id = db.insert(DBHelper.TABLE_CONTINENTS, null, values);
                //continents.setId(id);
                //Log.d( DEBUG_TAG, "Stored new country with id: " + id ) ;
            }
        } catch (Exception e) {
            Log.e( DEBUG_TAG, e.toString() );
        }
        //return continents;
    }

    public List<Continents> retrieve_ContinentsTable_data() {
        ArrayList<Continents> arr_continents = new ArrayList<>();
        Cursor cursor = null;
        int columnIndex;

        try{
            cursor = db.query( DBHelper.TABLE_CONTINENTS, allColumns_continents_table,
                    null, null, null, null, null );

            if( cursor != null && cursor.getCount() > 0 ) {

                while( cursor.moveToNext() ) {

                    if( cursor.getColumnCount() >= 3) {
                        columnIndex = cursor.getColumnIndex( DBHelper.COLUMN_ID_CONTINENTS_TABLE );
                        long continents_id = cursor.getLong( columnIndex );

                        columnIndex = cursor.getColumnIndex( DBHelper.COLUMN_COUNTRY_NAME_CONTINENTS_TABLE);
                        String country_name = cursor.getString( columnIndex );

                        columnIndex = cursor.getColumnIndex( DBHelper.COLUMN_CONTINENT_NAME_CONTINENTS_TABLE);
                        String continent_name = cursor.getString( columnIndex );

                        Continents continents_obj = new Continents(country_name, continent_name);
                        continents_obj.setId(continents_id);
                   //     Log.d(DEBUG_TAG, "Retrieved entry from continents table: " + continents_obj);

                        arr_continents.add(continents_obj);
                    }

                }
            }

        }catch( Exception e ){
            Log.d( DEBUG_TAG, "Exception caught: " + e );
        }
        finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        Log.d( DEBUG_TAG, "arr_continents: " + arr_continents );
        return arr_continents;
    }

    public List<Neighbours> retrieve_NeighboursTable_data() {
        ArrayList<Neighbours> arr_neighbours = new ArrayList<>();
        Cursor cursor = null;
        int columnIndex;

        try{
            cursor = db.query( DBHelper.TABLE_NEIGHBOURS, allColumns_neighbours_table,
                    null, null, null, null, null );

            if( cursor != null && cursor.getCount() > 0 ) {

                while( cursor.moveToNext() ) {

                    if( cursor.getColumnCount() >= 3) {
                        columnIndex = cursor.getColumnIndex( DBHelper.COLUMN_ID_NEIGHBOURS_TABLE );
                        long neighbours_id = cursor.getLong( columnIndex );

                        columnIndex = cursor.getColumnIndex( DBHelper.COLUMN_COUNTRY_NAME_NEIGHBOURS_TABLE);
                        String country_name = cursor.getString( columnIndex );

                        columnIndex = cursor.getColumnIndex( DBHelper.COLUMN_NEIGHBOUR_NAME_NEIGHBOURS_TABLE);
                        String neighbour_name = cursor.getString( columnIndex );

                        Neighbours neighbours_obj = new Neighbours(country_name, neighbour_name);
                        neighbours_obj.setId(neighbours_id);
                        //Log.d(DEBUG_TAG, "Retrieved entry from neighbours table: " + neighbours_obj);

                        arr_neighbours.add(neighbours_obj);
                    }

                }
            }

        }catch( Exception e ){
            Log.d( DEBUG_TAG, "Exception caught: " + e );
        }
        finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        Log.d( DEBUG_TAG, "arr_neighbours: " + arr_neighbours );
        return arr_neighbours;
    }

    public List<QuizResults> retrieve_QuizResultsTable_data() {
        ArrayList<QuizResults> arr_quizresults = new ArrayList<>();
        Cursor cursor = null;
        int columnIndex;

        try{
            cursor = db.query( DBHelper.TABLE_QUIZRESULTS, allColumns_quizresults_table,
                    null, null, null, null, null );

            if( cursor != null && cursor.getCount() > 0 ) {

                while( cursor.moveToNext() ) {

                    if( cursor.getColumnCount() >= 3) {
                        columnIndex = cursor.getColumnIndex( DBHelper.COLUMN_QUIZID_QUIZRESULTS_TABLE );
                        long quiz_id = cursor.getLong( columnIndex );

                        columnIndex = cursor.getColumnIndex( DBHelper.COLUMN_DATE_QUIZRESULTS_TABLE);
                        String quiz_date = cursor.getString( columnIndex );

                        columnIndex = cursor.getColumnIndex( DBHelper.COLUMN_SCORE_QUIZRESULTS_TABLE);
                        Integer quiz_score = cursor.getInt( columnIndex );

                        QuizResults quizresults_obj = new QuizResults(quiz_date, quiz_score);
                        quizresults_obj.setQuizId(quiz_id);

                        //Log.d(DEBUG_TAG, "Retrieved entry from neighbours table: " + neighbours_obj);

                        arr_quizresults.add(quizresults_obj);
                    }

                }

            }


        }catch( Exception e ){
            Log.d( DEBUG_TAG, "Exception caught: " + e );
        }
        finally{
            if (cursor != null) {
                cursor.close();
            }
        }

        return arr_quizresults;
    }

    public void populate_neighbours_table() {
        ContentValues values = new ContentValues();
        try {
            InputStream ip_stream = context.getAssets().open("country_neighbors.csv");
            CSVReader reader = new CSVReader(new InputStreamReader(ip_stream));
            String[] nextRow;
            while ((nextRow = reader.readNext()) != null) {
                String countryName = nextRow[0];
                for (int i = 1; i < nextRow.length; i++) {
                    String neighborName = nextRow[i];
                    if (neighborName != null && !neighborName.isEmpty()) {
                        values.clear();
                        values.put(DBHelper.COLUMN_COUNTRY_NAME_NEIGHBOURS_TABLE, countryName);
                        values.put(DBHelper.COLUMN_NEIGHBOUR_NAME_NEIGHBOURS_TABLE, neighborName);
                        long id = db.insert(DBHelper.TABLE_NEIGHBOURS, null, values);
                        if (id == -1) {
                            Log.e(DEBUG_TAG, "Failed to insert row for country " + countryName + " and neighbor " + neighborName);
                        }
                    }
                }

            }
        }catch (Exception e) {
            Log.e( DEBUG_TAG, e.toString() );
        }

    }

    public void populate_quizresults_table(int quizscore) {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_DATE_QUIZRESULTS_TABLE, currentDate);
        values.put(DBHelper.COLUMN_SCORE_QUIZRESULTS_TABLE, 5);

        long id = db.insert(DBHelper.TABLE_QUIZRESULTS, null, values);
        if (id == -1) {
            Log.e(DEBUG_TAG, "Failed to insert quiz result into database");
        } else {
            Log.d(DEBUG_TAG, "Inserted quiz result with ID: " + id);
        }
    }

}
