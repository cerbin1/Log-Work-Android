package com.example.bartek.log_work_android;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DateFormat;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "work.db";
    public static final int DATABASE_VERSION = 1;

    private static final String ID = "Id";
    private static final String DATE = "Date";
    private static final String HOURS_WORKED = "Hours_worked";

    private static final String TABLE_NAME = "Log_Work";
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
            + TABLE_NAME + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DATE + " TEXT, "
            + HOURS_WORKED + " REAL)";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    public boolean insert(long dateInMillis, double hoursWorked) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String date = DateFormat.format("EEEE, d.M", dateInMillis).toString();
        values.put(DATE, date);
        values.put(HOURS_WORKED, hoursWorked);
        long result = database.insert(TABLE_NAME, null, values);
        return result != -1;
    }

    public Cursor getWorkHistory() {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public Cursor getSumOfWorkedHours() {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery("SELECT SUM(" + HOURS_WORKED + ") FROM " + TABLE_NAME, null);
    }

    public void clearHistory() {
        SQLiteDatabase database = this.getReadableDatabase();
        database.execSQL(SQL_DELETE_ENTRIES);
        onCreate(database);
    }
}
