package com.secureidltd.belemaogan.sqliteproject;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;

public class MarkListActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    MyAdapter myAdapter;
    private DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_list);
        setTitle("Mark List");
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mDatabaseHelper = new DatabaseHelper(this);
        myAdapter = new MyAdapter(this, new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Cursor cursor) {
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
        });
        mRecyclerView.setAdapter(myAdapter);
        Cursor cursor = mDatabaseHelper.getAllData();
        myAdapter.addCursor(cursor);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                long id = (long) viewHolder.itemView.getTag();
                int numberOfItemsDeleted = mDatabaseHelper.deleteData(String.valueOf(id));
                myAdapter.addCursor(mDatabaseHelper.getAllData());
                showMessage("Alert", "The number of data deleted is " + numberOfItemsDeleted);
            }
        });
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_mark_action){
            startActivity(new Intent(this, AddMarkActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();

    }
}
