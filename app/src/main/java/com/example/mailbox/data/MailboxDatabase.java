package com.example.mailbox.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.mailbox.model.Mailbox;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class MailboxDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "mailbox";
    public static final int DATABASE_VERSION = 1;
    private final Context context;
    private static MailboxDatabase instance;

    public static final String COLUMN_NAME_ID = "id";
    public static final String COLUMN_NAME_NAME = "name";
    public static final String COLUMN_NAME_MAIL_HISTORY = "mail_history";
    public static final String COLUMN_NAME_NEW_MAIL = "new_mail";
    public static final String COLUMN_NAME_NOTICE = "notice";
    public static final String COLUMN_NAME_BATTERY = "battery";
    public static final String COLUMN_NAME_TEMPERATURE = "temperature";
    public static final String COLUMN_NAME_PRESSURE = "pressure";
    public static final String COLUMN_NAME_HUMIDITY = "humidity";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DATABASE_NAME +
                    " (" + COLUMN_NAME_ID + " INTEGER PRIMARY KEY, " +
                    COLUMN_NAME_NAME + " TEXT NOT NULL, " +
                    COLUMN_NAME_MAIL_HISTORY + " TEXT NOT NULL, " +
                    COLUMN_NAME_NEW_MAIL + " INTEGER NOT NULL, " +
                    COLUMN_NAME_NOTICE + " INTEGER NOT NULL, " +
                    COLUMN_NAME_BATTERY + " REAL NOT NULL, " +
                    COLUMN_NAME_TEMPERATURE + " REAL NOT NULL, " +
                    COLUMN_NAME_PRESSURE + " REAL NOT NULL, " +
                    COLUMN_NAME_HUMIDITY + " REAL NOT NULL)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DATABASE_NAME;

    private static final String SQL_INSERT_EMPTY_ENTRY =
            "INSERT INTO " + DATABASE_NAME +
                    " (username,jwt) VALUES ('','')";

    public MailboxDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        //db.execSQL(SQL_INSERT_EMPTY_ENTRY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void resetDatabase(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES);
        //db.execSQL(SQL_INSERT_EMPTY_ENTRY);
        db.close();
    }

    public static synchronized MailboxDatabase getInstance(final Context c) {
        if (instance == null) {
            instance = new MailboxDatabase(c.getApplicationContext());
        }
        return instance;
    }

    public Long saveMailbox(Mailbox mailbox){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME_ID, mailbox.getMailboxId());
        cv.put(COLUMN_NAME_NAME, mailbox.getName());
        cv.put(COLUMN_NAME_MAIL_HISTORY, new Gson().toJson(mailbox.getMailHistory()));
        cv.put(COLUMN_NAME_NEW_MAIL, mailbox.isNewMail());
        cv.put(COLUMN_NAME_NOTICE, mailbox.isAttemptedDeliveryNoticePresent());
        cv.put(COLUMN_NAME_BATTERY, mailbox.getBattery());
        cv.put(COLUMN_NAME_TEMPERATURE, mailbox.getTemperature());
        cv.put(COLUMN_NAME_PRESSURE, mailbox.getPressure());
        cv.put(COLUMN_NAME_HUMIDITY, mailbox.getHumidity());
        Long id = db.insert(DATABASE_NAME, null, cv);
        db.close();
        return id;
    }

    public <T> T getMailboxField(String field, Long id){
        Class<?> returnType;
        if (
                field.equals(COLUMN_NAME_ID)
                || field.equals(COLUMN_NAME_NEW_MAIL)
                || field.equals(COLUMN_NAME_NOTICE)
        ){
            returnType = Integer.class;
        } else if (
                field.equals(COLUMN_NAME_NAME)
                        || field.equals(COLUMN_NAME_MAIL_HISTORY)
        ){
            returnType = String.class;
        } else if (
                field.equals(COLUMN_NAME_BATTERY)
                        || field.equals(COLUMN_NAME_HUMIDITY)
                        || field.equals(COLUMN_NAME_PRESSURE)
                        || field.equals(COLUMN_NAME_TEMPERATURE)
        ){
            returnType = Double.class;
        } else {
            return null;
        }

        Cursor cursor = getReadableDatabase()
                .query(DATABASE_NAME,
                        new String[]{field},
                        "id = ?",
                        new String[]{id.toString()},
                        null,
                        null,
                        null);
        cursor.moveToFirst();
        T arg;

        Class<String> stringClass = String.class;
        Class<Integer> integerClass = Integer.class;
        if (returnType == stringClass){
            arg = (T) cursor.getString(cursor.getColumnIndexOrThrow(field));
        } else if (returnType == integerClass){
            arg = (T) Integer.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(field)));
        } else {
            arg = (T) Double.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow(field)));
        }
        cursor.close();

        return arg;
    }


    public Mailbox getMailboxById(Long mailboxId) {
        String historyJson = getMailboxField(MailboxDatabase.COLUMN_NAME_MAIL_HISTORY, mailboxId);

        Type listType = new TypeToken<List<String>>(){}.getType();
        List<String> mailHistory = new Gson().fromJson(historyJson, listType);

        Mailbox mailbox = new Mailbox(
                Long.valueOf((Integer)getMailboxField(MailboxDatabase.COLUMN_NAME_ID, mailboxId)),
                false,
                getMailboxField(MailboxDatabase.COLUMN_NAME_NAME, mailboxId),
                mailHistory,
                false,
                getMailboxField(MailboxDatabase.COLUMN_NAME_BATTERY, mailboxId),
                getMailboxField(MailboxDatabase.COLUMN_NAME_TEMPERATURE, mailboxId),
                getMailboxField(MailboxDatabase.COLUMN_NAME_PRESSURE, mailboxId),
                getMailboxField(MailboxDatabase.COLUMN_NAME_HUMIDITY, mailboxId)

                );

        return mailbox;
    }
}
