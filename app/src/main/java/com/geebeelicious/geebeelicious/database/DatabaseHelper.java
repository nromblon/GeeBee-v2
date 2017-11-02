package com.geebeelicious.geebeelicious.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

/*
* The original code was created by Mike Dayupay.
* For the purpose of integration, the code was modified.
* This class serves as the helper class and main
* connection to the database. It initializes, opens, and
* closes the database.
*
* @author Mike Dayupay
* @author Mary Grace Malana
*/

public class DatabaseHelper extends SQLiteOpenHelper {
    /**
     * Directory path of the database
     */
    private static String DB_PATH = "";

    /**
     * Name of the database
     */
    private static String DB_NAME = "get_better";

    /**
     * Version number of the database
     */
    private static int DB_VERSION = 1;

    /**
     * Used to identify the source of a log message
     */
    private static String TAG = "DatabaseHelper";

    /**
     * Context of the database
     */
    private final Context myContext;

    /**
     * Database of the application
     */
    private SQLiteDatabase getBetterDatabase;

    /**
     * Constructor. Initializes class attributes.
     *
     * @param context used by the database.
     */
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

        if (Build.VERSION.SDK_INT >= 17) {
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        } else {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
        this.myContext = context;
    }

    /**
     * Creates the database
     *
     * @throws IOException if database creation problem occurs.
     */
    public void createDatabase() throws IOException {

        boolean databaseExists = checkDatabase();

        if (!databaseExists) {
            this.getReadableDatabase();
            this.close();

            try {
                copyDatabase();
                Log.d(TAG, "createDatabase database created");
            } catch (IOException ioe) {
                throw new Error("Error creating database");
            }
        }
    }

    /**
     * Checks whether an application database already exist.
     *
     * @return true if application database exist, else false.
     */
    private boolean checkDatabase() {

        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    /**
     * Copies the database from the assets folder of
     * the application.
     *
     * @throws IOException if a InputStream/OutputStream problem
     *                     is encountered.
     */
    private void copyDatabase() throws IOException {

        InputStream mInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];

        int mLength;

        while ((mLength = mInput.read(mBuffer)) > 0) {
            mOutput.write(mBuffer, 0, mLength);
        }

        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    /**
     * Opens the database. A readable or writeable database
     * can now be retrieved.
     *
     * @throws SQLException if database does not exist
     *                      or encounters other opening database problem.
     */
    public void openDatabase() throws SQLException {

        String myPath = DB_PATH + DB_NAME;
        getBetterDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

    }

    /**
     * Closes the database. The database is no longer available for
     * read or write. The database must be opened again in order
     * to have access again.
     */
    @Override
    public synchronized void close() {

        if (getBetterDatabase != null) {
            getBetterDatabase.close();
        }

        super.close();
    }

    /**
     * @see SQLiteOpenHelper#onCreate(SQLiteDatabase)
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    /**
     * @see SQLiteOpenHelper#onUpgrade(SQLiteDatabase, int, int)
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
