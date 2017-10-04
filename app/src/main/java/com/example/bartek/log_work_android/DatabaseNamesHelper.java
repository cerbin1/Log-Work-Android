package com.example.bartek.log_work_android;


public class DatabaseNamesHelper {
    static final String DATABASE_NAME = "work.db";
    static final String TABLE_NAME = "Log_Work";
    static final int DATABASE_VERSION = 1;

    static final String ID = "Id";
    static final String DATE = "Date";
    static final String HOURS_WORKED = "Hours_worked";

    static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
            + TABLE_NAME + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DATE + " TEXT, "
            + HOURS_WORKED + " REAL)";
    static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
    static final String SQL_SELECT_SUM_OF_WORKED_HOURS = "SELECT SUM(" + HOURS_WORKED + ") FROM " + TABLE_NAME;
    static final String SQL_SELECT_WORK_HISTORY = "SELECT * FROM " + TABLE_NAME + " ORDER BY  " + ID + " DESC";
    static final String SQL_WHERE_CLAUSE = "Id = ?";
}
