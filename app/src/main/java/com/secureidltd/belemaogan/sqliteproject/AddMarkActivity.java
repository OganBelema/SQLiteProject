package com.secureidltd.belemaogan.sqliteproject;

import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
                Boolean isSuccessful =
                    mDatabaseHelper.insertData(name, surname, mark);
                if (isSuccessful) {
                Toast.makeText(this, "Insert successful",
                        Toast.LENGTH_LONG).show();
                } else {
                Toast.makeText(this, "Insert unsuccessful",
                        Toast.LENGTH_LONG).show();
                }
                break;


            /*case R.id.get_all_data_btn:
                Cursor databaseDataCursor = mDatabaseHelper.getAllData();
                if (databaseDataCursor.getCount() == 0){
                    Toast.makeText(this, "No data",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                StringBuilder stringBuilder = new StringBuilder();
                while (databaseDataCursor.moveToNext()){
                    stringBuilder.append("First name: " + databaseDataCursor.getString(1));
                    stringBuilder.append("\n");
                    stringBuilder.append("Last name: " + databaseDataCursor.getString(2));
                    stringBuilder.append("\n");
                    stringBuilder.append("Mark: "+ databaseDataCursor.getString(3));
                    stringBuilder.append("\n\n");
                }
                showMessage("Data", stringBuilder.toString());
                break;*/


            case R.id.update_database_btn:
                getData();
                if (TextUtils.isEmpty(dataID)){
                    showMessage("Error", "Provide data ID");
                    return;
                }
                boolean result = mDatabaseHelper.updateData(dataID, name, surname, mark);

                if (result)
                    showMessage("Message", "Update successful");
                else
                    showMessage("Error", "Update failed");
                break;


            /*case R.id.delete_data_btn:
                getData();
                if (TextUtils.isEmpty(dataID)){
                    showMessage("Error", "Provide data ID");
                    return;
                }
                int numberOfDataDeleted = mDatabaseHelper.deleteData(dataID);
                showMessage("Alert", "The number of data deleted is " + numberOfDataDeleted);
                break;*/
        }
    }

    private void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();

    }

}
