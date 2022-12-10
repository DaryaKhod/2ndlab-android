package com.example.myapplication2;

import static com.example.myapplication2.DBContract.UserEntry.COLUMN_NAME_LOGIN;
import static com.example.myapplication2.DBContract.UserEntry.TABLE_NAME;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Users.db";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + DBContract.UserEntry.COLUMN_NAME_KEY_ID + " INTEGER PRIMARY KEY," +
                DBContract.UserEntry.COLUMN_NAME_LOGIN + " TEXT," + DBContract.UserEntry.COLUMN_NAME_PASS + " TEXT" + ")";

        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean addUser(User user) {

        List<User> allUsers = getAllUsers();
        Optional<User> any = allUsers.stream().filter(i -> i.getLogin().equals(user.getLogin())).findAny();

        if (!any.isPresent()) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DBContract.UserEntry.COLUMN_NAME_LOGIN, user.getLogin());
            values.put(DBContract.UserEntry.COLUMN_NAME_PASS, user.getPassword());
            db.insert(TABLE_NAME, null, values);
            db.close();
            return true;
        }
        return false;
    }

    public List<User> getAllUsers() {
        List<User> usersList = new ArrayList<User>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(0)));
                user.setLogin(cursor.getString(1));
                user.setPassword(cursor.getString(2));
                usersList.add(user);
            } while (cursor.moveToNext());
        }
        return usersList;
    }

    public void deleteUser(String login) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM " + TABLE_NAME +
                " WHERE " + DBContract.UserEntry.COLUMN_NAME_LOGIN + " = '" + login + "'";
        db.execSQL(sql);
    }

    public void updateUser(String login, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE " + TABLE_NAME +
                " SET " + DBContract.UserEntry.COLUMN_NAME_PASS + "   = '" + password +
                "' WHERE " + COLUMN_NAME_LOGIN + " = '" + login + "' ;";
        db.execSQL(sql);
    }

}
