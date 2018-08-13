package com.secureidltd.belemaogan.sqliteproject;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.Objects;

public class MyContentProvider extends ContentProvider {

    private DatabaseHelper mDatabaseHelper;
    public static final int MARKS = 100;
    public static final int MARK_WITH_ID = 101;
    public static UriMatcher sUriMatcher = getUriMatcher();

    private static UriMatcher getUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(DatabaseHelper.AUTHORITY, DatabaseHelper.PATH, MARKS);
        uriMatcher.addURI(DatabaseHelper.AUTHORITY, DatabaseHelper.PATH + "/#", MARK_WITH_ID);

        return uriMatcher;
    }


    @Override
    public boolean onCreate() {
        mDatabaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        SQLiteDatabase sqLiteDatabase = mDatabaseHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        switch (match){
            case MARKS:
                cursor = sqLiteDatabase.query(DatabaseHelper.TABLE_NAME,
                        projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            case MARK_WITH_ID:
                selection = DatabaseHelper.COLUMN_1 + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = sqLiteDatabase.query(DatabaseHelper.TABLE_NAME,
                        projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        cursor.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        int match = sUriMatcher.match(uri);
        switch (match){
            case MARKS:
                return insertData(uri, contentValues);

            default:
                throw new UnsupportedOperationException("Unknown Uri: "+ uri);
        }
    }

    private Uri insertData(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        validateData(contentValues);
        SQLiteDatabase sqLiteDatabase = mDatabaseHelper.getWritableDatabase();
        long id = sqLiteDatabase.insert(DatabaseHelper.TABLE_NAME, null, contentValues);
        if (id == -1){
            return null;
        }
        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    private void validateData(@Nullable ContentValues contentValues) {
        if (contentValues != null) {
            String firstName = contentValues.getAsString(DatabaseHelper.COLUMN_2);
            if (TextUtils.isEmpty(firstName)) {
                throw new IllegalArgumentException("First Name cannot be empty");
            }

            String lastName = contentValues.getAsString(DatabaseHelper.COLUMN_3);
            if (TextUtils.isEmpty(lastName)){
                throw new IllegalArgumentException("Last Name cannot be empty");
            }

            String mark = contentValues.getAsString(DatabaseHelper.COLUMN_4);
            if (TextUtils.isEmpty(mark)){
                throw new IllegalArgumentException("Mark cannot be empty");
            }
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String where, @Nullable String[] whereArgs) {
        SQLiteDatabase sqLiteDatabase = mDatabaseHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        switch (match){
            case MARKS:
                return sqLiteDatabase.delete(DatabaseHelper.TABLE_NAME, where, whereArgs);

            case MARK_WITH_ID:
                where = DatabaseHelper.COLUMN_1 + "=?";
                whereArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return sqLiteDatabase.delete(DatabaseHelper.TABLE_NAME, where, whereArgs);

            default:
                throw new IllegalArgumentException("Uri not supported "+ uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {

        int match = sUriMatcher.match(uri);
        switch (match){
            case MARK_WITH_ID:
                selection = DatabaseHelper.COLUMN_1+"=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
                return updateData(contentValues, selection, selectionArgs);

            default:
                throw new IllegalArgumentException("Uri not supported "+uri);

        }
    }

    private int updateData(ContentValues contentValues, String selection, String[] selectionArgs) {
        validateData(contentValues);
        SQLiteDatabase sqLiteDatabase = mDatabaseHelper.getWritableDatabase();
        return sqLiteDatabase.update(DatabaseHelper.TABLE_NAME, contentValues, selection, selectionArgs);
    }
}
