package com.example.android.todolist.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ContactProvider extends ContentProvider {


    private static final int TODOs = 100;

    private static final int TODOItem_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY,Contract.PATH_TODO, TODOs);
        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY,Contract.PATH_TODO+"/#", TODOItem_ID);
    }


    //  **Database helper object**
    private ContactDbHelper mDbHelper;


    public static final String LOG_TAG = ContactProvider.class.getSimpleName();


    @Override
    public boolean onCreate() {
        mDbHelper=new ContactDbHelper(getContext());
        return true;
    }



    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);

        switch (match){
            case TODOs:
                cursor = database.query(Contract.TODOEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case TODOItem_ID:
                selection = Contract.TODOEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = database.query(Contract.TODOEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);

        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }










    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = sUriMatcher.match(uri);
        if (match == TODOs) {
            return insertTask(uri, values);
        }else{
            throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }
    private Uri insertTask(Uri uri, ContentValues values) {

        SQLiteDatabase db=mDbHelper.getWritableDatabase();

        long id=db.insert(Contract.TODOEntry.TABLE_NAME,null,values);
        if(id==-1){
            Log.e(LOG_TAG,"Failed to insert row for "+uri);
            return null;
        }


        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }
















    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case TODOs:
                return updateTask(uri, values, selection, selectionArgs);
            case TODOItem_ID:
                selection = Contract.TODOEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateTask(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateTask(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db=mDbHelper.getWritableDatabase();
        int rows=db.update(Contract.TODOEntry.TABLE_NAME,values,selection,selectionArgs);

        if (rows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rows;
    }












    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        int rowDeleted;
        switch (match) {
            case TODOs:
                rowDeleted=db.delete(Contract.TODOEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case TODOItem_ID:
                selection = Contract.TODOEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowDeleted=db.delete(Contract.TODOEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if (rowDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowDeleted;
    }



















    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case TODOs:
                return Contract.TODOEntry.CONTENT_LIST_TYPE;
            case TODOItem_ID:
                return Contract.TODOEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
