package com.secureidltd.belemaogan.sqliteproject;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddMarkActivity extends AppCompatActivity implements View.OnClickListener{

    private DatabaseHelper mDatabaseHelper;
    private String name;
    private String surname;
    private String mark;
    private String dataID;
    private EditText nameEditText;
    private EditText surnameEditText;
    private EditText markEditText;
    private EditText idEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameEditText = findViewById(R.id.et_name);
        surnameEditText = findViewById(R.id.et_surname);
        markEditText = findViewById(R.id.et_mark);
        idEditText = findViewById(R.id.et_id);
        Button addToDatabaseBtn = findViewById(R.id.add_to_database_btn);
        addToDatabaseBtn.setOnClickListener(this);

        /*Button getAllDataBtn = findViewById(R.id.get_all_data_btn);
        getAllDataBtn.setOnClickListener(this);*/

        Button updateData = findViewById(R.id.update_database_btn);
        updateData.setOnClickListener(this);

        /*Button deleteDataBtn = findViewById(R.id.delete_data_btn);
        deleteDataBtn.setOnClickListener(this);*/

        mDatabaseHelper = new DatabaseHelper(this);
    }

    private void getData(){

        dataID = idEditText.getText().toString();
        name = nameEditText.getText().toString();
        surname = surnameEditText.getText().toString();
        mark = markEditText.getText().toString();

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.add_to_database_btn:
                getData();
                ContentValues contentValues = new ContentValues();
                contentValues.put(DatabaseHelper.COLUMN_2, name);
                contentValues.put(DatabaseHelper.COLUMN_3, surname);
                contentValues.put(DatabaseHelper.COLUMN_4, mark);
                Uri uri;
                try {
                    uri = getContentResolver().insert(DatabaseHelper.CONTENT_URI, contentValues);
                } catch (Exception e){
                    showMessage("Error", e.getMessage());
                    return;
                }

                String title, message;
                if (uri == null){
                    message = "Insert unsuccessful";
                    title = "Error";
                } else {
                    message = "Insert successful";
                    title = "Message";
                }
                showMessage(title, message);
                break;

            case R.id.update_database_btn:
                getData();
                if (TextUtils.isEmpty(dataID)){
                    showMessage("Error", "Provide data ID");
                    return;
                }
                Uri updateUri = ContentUris.withAppendedId(DatabaseHelper.CONTENT_URI, Long.valueOf(dataID));
                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.COLUMN_2, name);
                values.put(DatabaseHelper.COLUMN_3, surname);
                values.put(DatabaseHelper.COLUMN_4, mark);
                int result = getContentResolver().update(updateUri, values, null, null);

                if (result > 0)
                    showMessage("Message", "Update successful");
                else
                    showMessage("Error", "Update failed");
                break;

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
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
