package com.example.android.todolist;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.android.todolist.data.Contract;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    private Uri mCurrentUri;
    private EditText titleEdittext, taskEdittext;
    private String titleString;
    private String taskString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        titleEdittext=findViewById(R.id.title_edittext);
        taskEdittext=findViewById(R.id.task_edittext);

        Intent intent=getIntent();
        mCurrentUri=intent.getData();
        if(mCurrentUri==null){
            setTitle("Add a Task");
            invalidateOptionsMenu();
        }else{
            setTitle("Edit Task");
            LoaderManager.getInstance(this).initLoader(0,null,this);
        }

    }

    private void saveTask(){
        String titleString= titleEdittext.getText().toString().trim();
        String taskString= taskEdittext.getText().toString().trim();

        ContentValues contentValues=new ContentValues();
        contentValues.put(Contract.TODOEntry.COLUMN_TODO_TITLE,titleString);
        contentValues.put(Contract.TODOEntry.COLUMN_TODO_TASK,taskString);

        if(mCurrentUri==null){
            Uri newUri=getContentResolver().insert(Contract.TODOEntry.CONTENT_URI,contentValues);

            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, "ERROR",
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, "Task Successfully Saved",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }else{
            int rowUpdated=getContentResolver().update(mCurrentUri,contentValues,null,null);
            if(rowUpdated<0){
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, "Error",
                        Toast.LENGTH_SHORT).show();
            }else{
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, "Task Successfully Saved",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you really want to delete?");
        builder.setPositiveButton("Delete",(dialog, id) ->{
            deleteContact();
        } );
        builder.setNegativeButton("Cancel", (dialog, id) -> {
            // and continue editing the pet.
            if (dialog != null) {
                dialog.cancel();
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void deleteContact() {
        if (mCurrentUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, "Error",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Task Deleted",
                        Toast.LENGTH_SHORT).show();
            }
        }

        // Close the activity
        finish();
    }




















    @Override
    public void onBackPressed() {
        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                (dialogInterface, i) -> {
                    // User clicked "Discard" button, close the current activity.
                    finish();
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You have unsaved changes, want to discard?");
        builder.setPositiveButton("Discard", discardButtonClickListener);
        builder.setNegativeButton("Keep Editing", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }




























    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_options,menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (mCurrentUri == null) {
            MenuItem menuItem = menu.findItem(R.id.delete);
            menuItem.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.save:
                saveTask();
                return true;
            case R.id.delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {
                Contract.TODOEntry._ID,
                Contract.TODOEntry.COLUMN_TODO_TITLE,
                Contract.TODOEntry.COLUMN_TODO_TASK};

        return new CursorLoader(this,
                mCurrentUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (data == null || data.getCount() < 1) {
            return;
        }
        if(data.moveToFirst()){
            titleString =data.getString(data.getColumnIndexOrThrow(Contract.TODOEntry.COLUMN_TODO_TITLE));
            taskString =data.getString(data.getColumnIndexOrThrow(Contract.TODOEntry.COLUMN_TODO_TASK));

            titleEdittext.setText(titleString);
            taskEdittext.setText(taskString);

        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        titleEdittext.setText(null);
        taskEdittext.setText(null);
    }
}