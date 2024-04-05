package ms.cs.countries_quiz;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//Purpose of DBHelper class :
// 1) It is used to create a database, if not created already. Database creation happens when
// getWritableDatabase() and getReadableDatabase() methods present inside DBHelper class are called
// (although they are not overridden here, they are present in SQLIteOpenHelper class, which DBHelper extends).
// 2) It has function to close the database called close().
// (although close() is not overridden here, it is present in SQLIteOpenHelper class, which DBHelper extends).
// 3) It is used to create OR drop all three tables (continents, neighbours, quizresults).
// 4) Only one instance of DBHelper can be created and would be accessible through variable 'helperInstance'.
// (Similar to JobLeadsDBHelper class of JobsTrackerSQLite Android app).

public class DBHelper extends SQLiteOpenHelper {
    private static final String DEBUG_TAG = "DBHelper";

    //Creating string to store Android's inhouse database's name.
    private static final String DB_NAME = "countriestrivia5.db";
    private static final int DB_VERSION = 1;

    //Creating strings to store table name and column names of the continents table.
    public static final String TABLE_CONTINENTS = "continents_table";
    public static final String COLUMN_ID_CONTINENTS_TABLE = "_id";
    public static final String COLUMN_COUNTRY_NAME_CONTINENTS_TABLE = "country_name";
    public static final String COLUMN_CONTINENT_NAME_CONTINENTS_TABLE = "continent_name";


    //Creating strings to store table name and column names of the neighbours table.
    public static final String TABLE_NEIGHBOURS = "neighbours_table";
    public static final String COLUMN_ID_NEIGHBOURS_TABLE = "_id";
    public static final String COLUMN_COUNTRY_NAME_NEIGHBOURS_TABLE = "country_name";
    public static final String COLUMN_NEIGHBOUR_NAME_NEIGHBOURS_TABLE = "neighbour_name";


    //Creating strings to store table name and column names of the quiz results table.
    public static final String TABLE_QUIZRESULTS = "quizresults";
    public static final String COLUMN_QUIZID_QUIZRESULTS_TABLE = "_quiz_id";
    public static final String COLUMN_DATE_QUIZRESULTS_TABLE = "quiz_date";
    public static final String COLUMN_SCORE_QUIZRESULTS_TABLE = "quiz_score";


    // Creating variable helperInstance to store the one and only instance of DBHelper.
    private static DBHelper helperInstance;

    //String storing create table command for continents table.
    private static final String CREATE_CONTINENTS_TABLE =
            "create table " + TABLE_CONTINENTS + " ("
                    + COLUMN_ID_CONTINENTS_TABLE + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_COUNTRY_NAME_CONTINENTS_TABLE + " TEXT, "
                    + COLUMN_CONTINENT_NAME_CONTINENTS_TABLE + " TEXT"
                    +
                    ")";

    //String storing create table command for neighbours table.
    private static final String CREATE_NEIGHBOURS_TABLE =
            "create table " + TABLE_NEIGHBOURS + " ("
                    + COLUMN_ID_NEIGHBOURS_TABLE + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_COUNTRY_NAME_NEIGHBOURS_TABLE + " TEXT, "
                    + COLUMN_NEIGHBOUR_NAME_NEIGHBOURS_TABLE + " TEXT"
                    +
                    ")";

    //String storing create table command for quiz results table.
    private static final String CREATE_QUIZRESULTS_TABLE =
            "create table " + TABLE_QUIZRESULTS + " ("
                    + COLUMN_QUIZID_QUIZRESULTS_TABLE + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_DATE_QUIZRESULTS_TABLE + " TEXT, "
                    + COLUMN_SCORE_QUIZRESULTS_TABLE + " TEXT"
                    +
                    ")";

    private DBHelper( Context context ) {
        super( context, DB_NAME, null, DB_VERSION );
    }

    //Method to generate and store the one and only permitted instance of DBHelper class in variable
    //called helperInstance.
    public static synchronized DBHelper getInstance( Context context ) {
        // check if the instance already exists and if not, create the instance
        if( helperInstance == null ) {
            helperInstance = new DBHelper( context.getApplicationContext() );
        }
        return helperInstance;
    }

    //Execute SQL Commands to create all three tables.
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( CREATE_CONTINENTS_TABLE );
        Log.d( DEBUG_TAG, "Table " + TABLE_CONTINENTS + " has been created" );

        //db.execSQL( CREATE_NEIGHBOURS_TABLE );
        //Log.d( DEBUG_TAG, "Table " + TABLE_NEIGHBOURS + " has been created" );

        //db.execSQL( CREATE_QUIZRESULTS_TABLE );
        //Log.d( DEBUG_TAG, "Table " + TABLE_QUIZRESULTS + " has been created" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "drop table if exists " + TABLE_CONTINENTS );
        onCreate( db );
        Log.d( DEBUG_TAG, "Table " + TABLE_CONTINENTS + " upgraded" );

        //db.execSQL( "drop table if exists " + TABLE_NEIGHBOURS );
        //nCreate( db );
        //Log.d( DEBUG_TAG, "Table " + TABLE_NEIGHBOURS + " upgraded" );

        //db.execSQL( "drop table if exists " + TABLE_QUIZRESULTS );
        //onCreate( db );
        //Log.d( DEBUG_TAG, "Table " + TABLE_QUIZRESULTS + " upgraded" );
    }
}
