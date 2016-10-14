package ru.semper_viventem.chromeor;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by semper-viventem on 09.10.16.
 */

public class WorkDB extends SQLiteOpenHelper {


    public static String origin_url = "origin_url";
    public static String action_url = "action_url";
    public static String username_value = "username_value";
    public static String password_value = "password_value";


    public WorkDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE logins" +
                "(" +
                origin_url + " text, " +
                action_url + " text, " +
                username_value + " text, " +
                password_value + " BLOB" +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
