package com.example.android.todolist;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cursoradapter.widget.CursorAdapter;
import com.example.android.todolist.data.Contract.TODOEntry;

public class ContactsAdapter extends CursorAdapter {


    public ContactsAdapter(Context context, Cursor c) {
        super(context, c,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView titletv=view.findViewById(R.id.title);

        String titleString=cursor.getString(cursor.getColumnIndexOrThrow(TODOEntry.COLUMN_TODO_TITLE));


        if(TextUtils.isEmpty(titleString)){
            titleString="--Title--";
        }

        titletv.setText(titleString);
    }
}
