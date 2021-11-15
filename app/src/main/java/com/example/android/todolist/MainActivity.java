package com.example.android.todolist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.todolist.data.Contract;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private ContactsAdapter mCursorAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, EditorActivity.class));
        });

        // finding listview
        ListView list = findViewById(R.id.list);
        //setting empty view to list
        View emptyView = findViewById(R.id.empty_view);
        list.setEmptyView(emptyView);


        mCursorAdapter = new ContactsAdapter(this, null);
        list.setAdapter(mCursorAdapter);
        LoaderManager.getInstance(this).initLoader(0, null, this);

        list.setOnItemClickListener((adapterView, view, position, id) -> {
            Intent intent = new Intent(MainActivity.this, EditorActivity.class);
            Uri currentUri = ContentUris.withAppendedId(Contract.TODOEntry.CONTENT_URI, id);
            intent.setData(currentUri);
            startActivity(intent);
        });

    }




    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {
                Contract.TODOEntry._ID,
                Contract.TODOEntry.COLUMN_TODO_TITLE,
                Contract.TODOEntry.COLUMN_TODO_TASK};
        return new CursorLoader(this,
                Contract.TODOEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}


