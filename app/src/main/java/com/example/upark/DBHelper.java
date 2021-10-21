package com.example.upark;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DBHelper {

    SQLiteDatabase sqLiteDatabase;

    public DBHelper(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
    }

    public void createUserTable() {

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS Users " +
                "(user_id INTEGER PRIMARY KEY," +
                "username TEXT," +
                "password TEXT," +
                "email TEXT," +
                "fName TEXT," +
                "lName TEXT)");
    }

    public void createParkTable() {

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS Parks " +
                "(park_id INTEGER PRIMARY KEY," +
                "park_name TEXT," +
                "rating FLOAT)");
    }

    public void createReviewTable() {

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS Reviews " +
                "(review_id INTEGER PRIMARY KEY," +
                "park_id INTEGER," +
                "rating FLOAT," +
                "isBikeFriendly INTEGER," +
                "isChildFriendly INTEGER," +
                "isDisabilityFriendly INTEGER," +
                "isWooded INTEGER," +
                "isCarAccessible INTEGER," +
                "isPetFriendly INTEGER," +
                "user_id INTEGER)");
    }

    public ArrayList<User> readAllUsers() {

        createUserTable();
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM Users", null);

        int userIDIndex = c.getColumnIndex("user_id");
        int usernameIndex = c.getColumnIndex("username");
        int passwordIndex = c.getColumnIndex("password");
        int emailIndex = c.getColumnIndex("email");
        int fNameIndex = c.getColumnIndex("fName");
        int lNameIndex = c.getColumnIndex("lName");

        c.moveToFirst();

        ArrayList<User> userList = new ArrayList<>();

        while(!c.isAfterLast()) {

            int userID = c.getInt(userIDIndex);
            String username = c.getString(usernameIndex);
            String password = c.getString(passwordIndex);
            String email = c.getString(emailIndex);
            String fName = c.getString(fNameIndex);
            String lName = c.getString(lNameIndex);

            User user = new User(userID, username, password, email, fName, lName);
            userList.add(user);
            c.moveToNext();
        }

        c.close();
        sqLiteDatabase.close();

        return userList;
    }

    public void addUser(User user) {
        createUserTable();
        sqLiteDatabase.execSQL(String.format("INSERT INTO Users (username, password, email, fName, lName) VALUES ('%s', '%s', '%s', '%s', '%s'", user.username, user.password, user.email, user.fName, user.lName));
    }

    public ArrayList<Park> readParks() {
        return new ArrayList<>();
    }

    public ArrayList<Review> readReviews() {
        return new ArrayList<>();
    }
}
