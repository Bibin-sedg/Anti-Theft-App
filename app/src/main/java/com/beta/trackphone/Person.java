package com.beta.trackphone;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Person {
    public static final String TABLE_NAME = "Person";

    public static final String COL_ID = "_id";

    public static final String COL_FIRSTNAME = "firstname";
    public static final String COL_NO="number";
    public static final String[] FIELDS = { COL_ID, COL_FIRSTNAME, COL_NO};

    //The SQL code that creates a Table for storing Persons in.


    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COL_ID + " INTEGER PRIMARY KEY,"
                    + COL_FIRSTNAME + " TEXT NOT NULL DEFAULT '',"
                    + COL_NO + " INTEGER NOT NULL DEFAULT ''"
                    + ")";


    public long id = -1;
    public String firstname = "";
    public long number= -1;

    public Person() {
    }


    public Person(final Cursor cursor) {
        // Indices expected to match order in FIELDS!
        this.id = cursor.getLong(0);
        this.firstname = cursor.getString(1);
        this.number = cursor.getLong(2);

    }


    public static void onCreate(SQLiteDatabase db){
        Log.w("Person_db",CREATE_TABLE);
        db.execSQL(CREATE_TABLE);

    }
    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("Upgrade", "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


}
