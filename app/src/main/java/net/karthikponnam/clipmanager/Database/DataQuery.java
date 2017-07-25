package net.karthikponnam.clipmanager.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import net.karthikponnam.clipmanager.Const.Constants;
import net.karthikponnam.clipmanager.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ponna on 17-05-2017.
 */

public class DataQuery {

    private String TAG = getClass().getName();
    private DBHelper dbHelper;
    private SQLiteDatabase database;
    private String[] allColumns = { Constants.DATA,
            Constants.DATE, Constants.ID };
    Context context;

    public DataQuery(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public JSONObject addClip(String data) {
        open();
        final JSONObject result = new JSONObject();
        try {
            result.put("status", "ok");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.DATA, data);
        contentValues.put(Constants.DATE, getCurrentDateTime());

        JSONArray jsonArray = getClips();
        Boolean insert = false;
        if(jsonArray.length() == 0) {
            insert = true;
        } else {
            for( int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                if(jsonObject.optString("data").trim().equals(data.trim())) {
                    insert = false;
                    return result;
                } else {
                    insert = true;
                }
            }
        }

        if(insert) {
            open();
            database.insert(Constants.TABLE, null, contentValues);
            close();
        } else {
        }

        return result;
    }

    public JSONArray getClips () {
        open();
        JSONArray jsonArray = new JSONArray();
        Cursor cursor = database.query(Constants.TABLE, allColumns, null, null, null, null, "id DESC", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            JSONObject jsonObject = new JSONObject();
            String data = cursor.getString(cursor.getColumnIndex(Constants.DATA));
            String date = cursor.getString(cursor.getColumnIndex(Constants.DATE));
            int id = cursor.getInt(cursor.getColumnIndex(Constants.ID));

            try {
                jsonObject.put("data", data);
                jsonObject.put("date", date);
                jsonObject.put("id", id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
            cursor.moveToNext();
        }
        close();
        return  jsonArray;
    }

    public JSONObject getClipsById (int _id) {
        open();
        Cursor cursor = database.query(Constants.TABLE, allColumns, "id=" + _id, null, null, null, "id DESC", null);
        cursor.moveToFirst();
        JSONObject jsonObject = new JSONObject();
        while (!cursor.isAfterLast()) {
            jsonObject = new JSONObject();
            String data = cursor.getString(cursor.getColumnIndex(Constants.DATA));
            String date = cursor.getString(cursor.getColumnIndex(Constants.DATE));
            int id = cursor.getInt(cursor.getColumnIndex(Constants.ID));
            try {
                jsonObject.put("data", data);
                jsonObject.put("date", date);
                jsonObject.put("id", id);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            cursor.moveToNext();
        }
        close();
        return  jsonObject;
    }

    public JSONObject deleteClip (int id) {
        open();
        Log.d(TAG, "deleteClip: " + String.valueOf(id));
        database.delete(Constants.TABLE, "id="+ id, null);
        close();
        return null;
    }

    public JSONObject deleteInsertClip (int id, String data) {
        open();
        Log.d(TAG, "deleteClip: " + String.valueOf(id));
        database.delete(Constants.TABLE, "id="+ id, null);
        close();
        addClip(data);
        return null;
    }

    public String getCurrentDateTime() {
        String currentDateTimeString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        return currentDateTimeString;
    }

    public JSONObject updateClipById(String data, int _id) {
        open();
        final JSONObject result = new JSONObject();
        try {
            result.put("status", "ok");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.DATA, data);
        contentValues.put(Constants.DATE, getCurrentDateTime());
        database.update(Constants.TABLE, contentValues, "id=" + _id, null);
        close();
        return result;
    }

}
