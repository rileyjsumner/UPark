package com.example.upark.Database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.upark.DAO.Park;
import com.example.upark.DAO.Review;
import com.example.upark.DAO.User;

import java.util.ArrayList;

public class DBHelper {

    SQLiteDatabase sqLiteDatabase; // SQLite database instance

    public DBHelper(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
    }

    /**
     * Creates a table to store user information
     */
    public void createUserTable() {

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS Users " +
                "(user_id INTEGER PRIMARY KEY," +
                "username TEXT," +
                "password TEXT," +
                "email TEXT," +
                "fName TEXT," +
                "lName TEXT)");
    }

    /**
     * Creates a table to store park information
     */
    public void createParkTable() {

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS Parks " +
                "(park_id INTEGER PRIMARY KEY," +
                "park_name TEXT," +
                "rating FLOAT)");
    }

    /**
     * Creates a table to store review information
     */
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

    /**
     * Reads all users in the database
     * @return a list of all users
     */
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

    /**
     * Add a user to the database
     * @param user to be added to the database
     */
    public boolean addUser(User user) {
        // TODO: check if user is inserted properly
        createUserTable();
        sqLiteDatabase.execSQL(String.format("INSERT INTO Users (username, password, email, fName, lName) VALUES ('%s', '%s', '%s', '%s', '%s'", user.getUsername(), user.getPassword(), user.getEmail(), user.getfName(), user.getlName()));
        return true;
    }

    /**
     * Update a users password in the database
     * @param user to update password of
     * @param newPassword new password to be inserted
     * @return boolean if operation was successful
     */
    public boolean updatePassword(User user, String newPassword) {
        createUserTable();
        sqLiteDatabase.execSQL(String.format("UPDATE Users SET password = %s WHERE username = %s", newPassword, user.getUsername()));
        return true;
    }

    public ArrayList<Park> readParks() {
        return new ArrayList<>();
    }

    public ArrayList<Review> readReviews() {
        return new ArrayList<>();
    }

    /**
     * Fetches the given users password from the database
     * @param username supplied into login form from UI
     * @param password supplied into login form from UI
     * @return boolean if the supplied password is equal to password from DB
     */
    public boolean login(String username, String password) {
        createUserTable();
        Cursor c = sqLiteDatabase.rawQuery("SELECT password FROM Users WHERE username = ?", new String[] {username});

        int passwordIndex = c.getColumnIndex("password");
        c.moveToFirst();

        String db_pass = c.getString(passwordIndex);

        return password.equals(db_pass);
    }

    /**
     * Check whether a username is in the database
     * @param username to check against the database
     * @return boolean if user exists in database
     */
    public boolean userExists(String username) {
        createUserTable();
        Cursor c = sqLiteDatabase.rawQuery("SELECT username FROM USERS", null);

        int usernameIndex = c.getColumnIndex("username");
        c.moveToFirst();

        boolean userExists = false;

        while(!c.isAfterLast()) {
            if(c.getString(usernameIndex).equals(username)) {
                userExists = true;
            }
        }

        return userExists;
    }
}
