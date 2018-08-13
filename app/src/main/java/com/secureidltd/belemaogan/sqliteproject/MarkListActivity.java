package com.secureidltd.belemaogan.sqliteproject;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MarkListActivity extends AppCompatActivity{
    RecyclerView mRecyclerView;
    MyAdapter myAdapter;
    private DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_list);
        setTitle("Mark List");
        mRecyclerView = findViewById(R.id.recycler_view);
        FloatingActionButton addDataFab = findViewById(R.id.add_data_fab);
        addDataFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MarkListActivity.this, AddMarkActivity.class));
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mDatabaseHelper = new DatabaseHelper(this);
        myAdapter = new MyAdapter(this, new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Cursor cursor) {
                if (cursor != null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_2));
                    String surname = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_3));
                    String mark = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_4));

                    stringBuilder.append("FirstName: ").append(name);
                    stringBuilder.append("\n");
                    stringBuilder.append("LastName: ").append(surname);
                    stringBuilder.append("\n");
                    stringBuilder.append("Mark: ").append(mark);
                    showMessage("Item Details", stringBuilder.toString());
                }
            }
        });
        mRecyclerView.setAdapter(myAdapter);

        //Cursor cursor = mDatabaseHelper.getAllData();

        myAdapter.addCursor(getContentResolver().query(DatabaseHelper.CONTENT_URI, null, null, null, null));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                long id = (long) viewHolder.itemView.getTag();
                Uri deleteUri = ContentUris.withAppendedId(DatabaseHelper.CONTENT_URI, id);
                int numberOfItemsDeleted = getContentResolver().delete(deleteUri, null, null);
                myAdapter.addCursor(getContentResolver().query(DatabaseHelper.CONTENT_URI,
                        null, null, null, null));
                showMessage("Alert", "The number of data deleted is " + numberOfItemsDeleted);
            }
        });
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

    }

    private void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();

    }
}
