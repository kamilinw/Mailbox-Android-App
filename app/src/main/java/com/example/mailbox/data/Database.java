package com.example.mailbox.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "mailbox_users";
    public static final int DATABASE_VERSION = 1;
    private final Context context;
    private static Database instance;

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DATABASE_NAME +
                    " (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT NOT NULL, email TEXT, jwt TEXT NOT NULL, mailbox_ids TEXT )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DATABASE_NAME;

    public Database (Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL("INSERT INTO " + DATABASE_NAME + " (username,jwt) VALUES ('','')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    //TODO reset database,
    // add Email and mailboxes (invokes setters), getters and setters

    public static synchronized Database getInstance(final Context c) {
        if (instance == null) {
            instance = new Database(c.getApplicationContext());
        }
        return instance;
    }

    public String getJwtToken(){
        Cursor cursor = getReadableDatabase()
                .query(DATABASE_NAME, new String[]{"jwt"}, "id = 1", null, null, null, null);
        cursor.moveToFirst();
        String retrievedToken = "";

        retrievedToken = cursor.getString(cursor.getColumnIndexOrThrow("jwt"));
        cursor.close();
        return retrievedToken.isEmpty() ? null : retrievedToken;
    }

    public void saveJWT(String username, String jwt){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username",username);
        cv.put("jwt", jwt);
        db.update(DATABASE_NAME, cv, "id=1", null);
        db.close();
    }
}
