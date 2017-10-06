package com.example.bartek.log_work_android;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DateFormat;

import static com.example.bartek.log_work_android.DatabaseNamesHelper.DATABASE_NAME;
import static com.example.bartek.log_work_android.DatabaseNamesHelper.DATABASE_VERSION;
import static com.example.bartek.log_work_android.DatabaseNamesHelper.DATE;
import static com.example.bartek.log_work_android.DatabaseNamesHelper.HOURS_WORKED;
import static com.example.bartek.log_work_android.DatabaseNamesHelper.SQL_CREATE_ENTRIES;
import static com.example.bartek.log_work_android.DatabaseNamesHelper.SQL_DELETE_ENTRIES;
import static com.example.bartek.log_work_android.DatabaseNamesHelper.SQL_SELECT_SUM_OF_WORKED_HOURS;
import static com.example.bartek.log_work_android.DatabaseNamesHelper.SQL_SELECT_WORK_HISTORY;
import static com.example.bartek.log_work_android.DatabaseNamesHelper.SQL_WHERE_CLAUSE;
import static com.example.bartek.log_work_android.DatabaseNamesHelper.TABLE_NAME;

public class DatabaseHelper extends SQLiteOpenHelper {

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
        String date = DateFormat.format("d.M EEEE", dateInMillis).toString();
        values.put(DATE, date);
        values.put(HOURS_WORKED, hoursWorked);
        long result = database.insert(TABLE_NAME, null, values);
        return result != -1;
    }

    public Cursor getWorkHistory() {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery(SQL_SELECT_WORK_HISTORY, null);
    }

    public Cursor getSumOfWorkedHours() {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery(SQL_SELECT_SUM_OF_WORKED_HOURS, null);
    }

    public void clearHistory() {
        SQLiteDatabase database = this.getReadableDatabase();
        database.execSQL(SQL_DELETE_ENTRIES);
        onCreate(database);
    }

    public int delete(String buttonId) {
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete(TABLE_NAME, SQL_WHERE_CLAUSE, new String[]{buttonId});
    }
}
