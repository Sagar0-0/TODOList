package com.example.android.todolist.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.todolist.data.Contract.TODOEntry;

public class ContactDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "myTODOList.db";

    private static final int DATABASE_VERSION = 1;


    public ContactDbHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table

        String SQL_CREATE_CONTACTS_TABLE =  "CREATE TABLE " + TODOEntry.TABLE_NAME + " ("
                + TODOEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TODOEntry.COLUMN_TODO_TITLE + " TEXT, "
                + TODOEntry.COLUMN_TODO_TASK + " TEXT);" ;

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}
