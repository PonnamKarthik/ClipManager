package net.karthikponnam.clipmanager.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ponna on 17-05-2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static String TABLE = "clipdata";
    private static String DATA = "data";
    private static String DATE = "date";
    public static final String ID = "id";
    private static final String DATABASE_NAME = "clip.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE + "( " + ID
            + " integer primary key autoincrement, " + DATA
            + " text not null ," + DATE + " text not null);";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

}
