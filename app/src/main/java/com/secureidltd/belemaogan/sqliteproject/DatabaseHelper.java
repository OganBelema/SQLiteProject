package com.secureidltd.belemaogan.sqliteproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.net.URI;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "student.db";
    public static final int DATABASE_VERSION = 4;

    public static final String TABLE_NAME = "student_table";
    public static final String COLUMN_1 = "ID";
    public static final String COLUMN_2 = "NAME";
    public static final String COLUMN_3 = "SURNAME";
    public static final String COLUMN_4 = "MARKS";

    public static final String AUTHORITY = "com.secureidltd.belemaogan.sqliteproject";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+AUTHORITY);
    public static final String PATH = "student";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH);

    private static final String CREATE_TABLE_SQL = "CREATE TABLE "+ TABLE_NAME + " (" +
            COLUMN_1 +" integer PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_2 + " text NOT NULL,"
            + COLUMN_3 + " text NOT NULL,"
            + COLUMN_4 + " integer NOT NULL"
            +"); ";

    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";


    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_TABLE);
        onCreate(sqLiteDatabase);
    }

    public boolean updateData(String id, String name, String surname, String mark){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_1, id);
        contentValues.put(COLUMN_2, name);
        contentValues.put(COLUMN_3, surname);
        contentValues.put(COLUMN_4, mark);
        int result = sqLiteDatabase.update(TABLE_NAME, contentValues,
                COLUMN_1 +" = ?",
                new String[]{id});
        return (result != -1);
    }
}
