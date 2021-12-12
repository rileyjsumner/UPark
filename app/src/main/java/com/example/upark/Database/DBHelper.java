package com.example.upark.Database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.upark.DAO.Park;
import com.example.upark.DAO.Review;
import com.example.upark.DAO.SecurityQuestion;
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
                "rating FLOAT," +
                "lat FLOAT," +
                "lon FLOAT," +
                "description TEXT," +
                "googleAPIID TEXT)");
    }

    /**
     * Creates a table to store review information
     */
    public void createReviewTable() {

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS Reviews " +
                "(review_id INTEGER PRIMARY KEY," +
                "park_id INTEGER," +
                "rating FLOAT," +
                "review_text TEXT," +
                "isBikeFriendly INTEGER," +
                "isChildFriendly INTEGER," +
                "isDisabilityFriendly INTEGER," +
                "isWooded INTEGER," +
                "isCarAccessible INTEGER," +
                "isPetFriendly INTEGER," +
                "user_id INTEGER)");
    }

    public void createFavoriteParkTable() {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS FavoriteParks " +
                "(favorite_id INTEGER PRIMARY KEY," +
                "park_id INTEGER," +
                "user_id INTEGER)");
    }

    public void createSecurityQuestionTable() {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS SecurityQuestions " +
                "(question_id INTEGER PRIMARY KEY," +
                "user_id INTEGER," +
                "question_text TEXT," +
                "answer TEXT)");
    }

    /**
     * Reads all users in the database
     * @return a list of all users
     */
    public ArrayList<User> readAllUsers() {

        createUserTable();
        Cursor c = sqLiteDatabase.rawQuery("SELECT *, Users.rowid AS user_id FROM Users", null);

        int userIDIndex = c.getColumnIndexOrThrow("user_id");
        int usernameIndex = c.getColumnIndex("username");
        int passwordIndex = c.getColumnIndex("password");
        int emailIndex = c.getColumnIndex("email");
        int fNameIndex = c.getColumnIndex("fName");
        int lNameIndex = c.getColumnIndex("lName");

        c.moveToFirst();

        ArrayList<User> userList = new ArrayList<>();

        while(!c.isAfterLast()) {

            long userID = c.getLong(userIDIndex);
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

        return userList;
    }

    /**
     * Add a user to the database
     * @param user to be added to the database
     */
    public boolean addUser(User user) {
        // TODO: check if user is inserted properly
        createUserTable();
        Log.i("LOGIN", "Inserting into db" + user.toString());
        sqLiteDatabase.execSQL(String.format("INSERT INTO Users (username, password, email, fName, lName) VALUES ('%s', '%s', '%s', '%s', '%s');", user.getUsername(), user.getPassword(), user.getEmail(), user.getfName(), user.getlName()));
        return true;
    }

    public boolean addPark(Park park) {
        createParkTable();
        double[] temp_loc = park.getLoc();
        double temp_lat = temp_loc[0];
        double temp_lon = temp_loc[1];
        if(park.getRating() > -1) {
            sqLiteDatabase.execSQL(String.format("INSERT INTO Parks (park_name, rating, description, lat, lon, googleAPIID) VALUES ('%s', '%s', '%s', '%s', '%s', '%s');", park.getParkName(), park.getRating(), park.getDescription(), temp_lat, temp_lon, park.getPlaceID())); // TODO add googleAPIID
        } else {
            sqLiteDatabase.execSQL(String.format("INSERT INTO Parks (park_name, description, lat, lon, googleAPIID) VALUES ('%s', '%s', '%s', '%s', '%s');", park.getParkName(), park.getDescription(), temp_lat, temp_lon, park.getPlaceID()));
        }
        return true;
    }

    public boolean addReview(Review review) {
        createReviewTable();
        sqLiteDatabase.execSQL(String.format("INSERT INTO Reviews (park_id, rating, review_text, isBikeFriendly, isChildFriendly, isDisabilityFriendly, isWooded, isCarAccessible, isPetFriendly, user_id) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');",
                review.getParkID(),
                review.getRating(),
                review.getReviewText(),
                review.isBikeFriendly(),
                review.isChildFriendly(),
                review.isDisabilityFriendly(),
                review.isWooded(),
                review.isCarAccessible(),
                review.isPetFriendly(),
                review.getReviewer().getUserID()));
        return true;
    }

    public boolean addSecurityQuestion(User user, SecurityQuestion q) {
        createSecurityQuestionTable();
        sqLiteDatabase.execSQL(String.format("INSERT INTO SecurityQuestions (user_id, question_text, answer) VALUES ('%s', '%s', '%s');", user.getUserID(), q.getQuestionText(), q.getAnswerText()));
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
        sqLiteDatabase.execSQL(String.format("UPDATE Users SET password = '%s' WHERE Users.rowid = '%s'", newPassword, user.getUserID()));
        return true;
    }

    public boolean updateEmail(User user, String newEmail) {
        createUserTable();
        sqLiteDatabase.execSQL(String.format("UPDATE Users SET email = '%s' WHERE Users.rowid = '%s'", newEmail, user.getUserID()));
        return true;
    }

    public ArrayList<Park> readParks() {
        createParkTable();
        Cursor c = sqLiteDatabase.rawQuery("SELECT *, Parks.rowid as park_id FROM Parks", null);

        int parkIDIndex = c.getColumnIndex("park_id");
        int nameIndex = c.getColumnIndex("park_name");
        int descriptionIndex = c.getColumnIndex("description");
        int latIndex = c.getColumnIndex("lat");
        int lonIndex = c.getColumnIndex("lon");
        int googleAPIIDIndex = c.getColumnIndex("googleAPIID");

        c.moveToFirst();

        ArrayList<Park> parkList = new ArrayList<>();

        while(!c.isAfterLast()) {

            long parkID = c.getLong(parkIDIndex);
            String name = c.getString(nameIndex);
            double rating = getParkRating(parkID);
            String description = c.getString(descriptionIndex);
            String googleAPIID = c.getString(googleAPIIDIndex);
            double lat = c.getFloat(latIndex);
            double lon = c.getFloat(lonIndex);

            Park park = new Park(parkID, googleAPIID, name, rating, description);
            park.setLoc(lat, lon);
            parkList.add(park);
            c.moveToNext();
        }

        c.close();

        return parkList;
    }

    public ArrayList<Review> readReviews(long parkID) {

        createReviewTable();
        Cursor c = sqLiteDatabase.rawQuery("SELECT *, Reviews.rowid AS review_id FROM Reviews WHERE park_id = ?", new String[]{ parkID + "" });

        int reviewIDIndex = c.getColumnIndex("review_id");
        int ratingIndex = c.getColumnIndex("rating");
        int reviewTextIndex = c.getColumnIndex("review_text");
        int isBikeFriendlyIndex = c.getColumnIndex("isBikeFriendly");
        int isChildFriendlyIndex = c.getColumnIndex("isChildFriendly");
        int isDisabilityFriendlyIndex = c.getColumnIndex("isDisabilityFriendly");
        int isWoodedIndex = c.getColumnIndex("isWooded");
        int isCarAccessibleIndex = c.getColumnIndex("isCarAccessible");
        int isPetFriendlyIndex = c.getColumnIndex("isPetFriendly");
        int userIDIndex = c.getColumnIndex("user_id");

        c.moveToFirst();

        ArrayList<Review> reviewList = new ArrayList<>();

        while(!c.isAfterLast()) {

            long reviewID = c.getLong(reviewIDIndex);
            double rating = c.getDouble(ratingIndex);
            String reviewText = c.getString(reviewTextIndex);
            boolean isBikeFriendly = c.getInt(isBikeFriendlyIndex) == 1;
            boolean isChildFriendly = c.getInt(isChildFriendlyIndex) == 1;
            boolean isDisabilityFriendly = c.getInt(isDisabilityFriendlyIndex) == 1;
            boolean isWooded = c.getInt(isWoodedIndex) == 1;
            boolean isCarAccessible = c.getInt(isCarAccessibleIndex) == 1;
            boolean isPetFriendly = c.getInt(isPetFriendlyIndex) == 1;
            long userID = c.getLong(userIDIndex);
            User user = getUserByID(userID);

            Review review = new Review(reviewID, parkID, rating, reviewText, isBikeFriendly, isChildFriendly, isDisabilityFriendly, isWooded, isCarAccessible, isPetFriendly, user, null);
            reviewList.add(review);
            c.moveToNext();
        }

        c.close();

        return reviewList;
    }

    public ArrayList<Review> getReviewsByUser(long userID) {
        createReviewTable();
        Cursor c = sqLiteDatabase.rawQuery("SELECT *, Reviews.rowid AS review_id FROM Reviews WHERE user_id = ?", new String[]{ userID + "" });

        int reviewIDIndex = c.getColumnIndex("review_id");
        int ratingIndex = c.getColumnIndex("rating");
        int reviewTextIndex = c.getColumnIndex("review_text");
        int isBikeFriendlyIndex = c.getColumnIndex("isBikeFriendly");
        int isChildFriendlyIndex = c.getColumnIndex("isChildFriendly");
        int isDisabilityFriendlyIndex = c.getColumnIndex("isDisabilityFriendly");
        int isWoodedIndex = c.getColumnIndex("isWooded");
        int isCarAccessibleIndex = c.getColumnIndex("isCarAccessible");
        int isPetFriendlyIndex = c.getColumnIndex("isPetFriendly");
        int parkIDIndex = c.getColumnIndex("park_id");

        c.moveToFirst();

        ArrayList<Review> reviewList = new ArrayList<>();

        while(!c.isAfterLast()) {

            long reviewID = c.getLong(reviewIDIndex);
            double rating = c.getDouble(ratingIndex);
            String reviewText = c.getString(reviewTextIndex);
            boolean isBikeFriendly = c.getInt(isBikeFriendlyIndex) == 1;
            boolean isChildFriendly = c.getInt(isChildFriendlyIndex) == 1;
            boolean isDisabilityFriendly = c.getInt(isDisabilityFriendlyIndex) == 1;
            boolean isWooded = c.getInt(isWoodedIndex) == 1;
            boolean isCarAccessible = c.getInt(isCarAccessibleIndex) == 1;
            boolean isPetFriendly = c.getInt(isPetFriendlyIndex) == 1;
            long parkID = c.getLong(parkIDIndex);
            User user = getUserByID(userID);

            Review review = new Review(reviewID, parkID, rating, reviewText, isBikeFriendly, isChildFriendly, isDisabilityFriendly, isWooded, isCarAccessible, isPetFriendly, user, null);
            reviewList.add(review);
            c.moveToNext();
        }

        c.close();

        return reviewList;
    }

    /**
     * Fetch the average park rating from the database
     * @param parkID id of the park to get the rating of
     * @return rating of park with given id
     */
    public double getParkRating(long parkID) {
        createReviewTable();
        Cursor c = sqLiteDatabase.rawQuery("SELECT AVG(rating) AS avg_rating FROM Reviews WHERE Reviews.rowid = ?", new String[]{ parkID + "" });

        int ratingIndex = c.getColumnIndex("avg_rating");

        c.moveToFirst();
        double rating = c.getDouble(ratingIndex);

        c.close();

        return rating;
    }

    public User getUserByID(long userID) {
        createUserTable();
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM Users WHERE Users.rowid = ?", new String[]{ userID + ""});

        int usernameIndex = c.getColumnIndex("username");
        int passwordIndex = c.getColumnIndex("password");
        int emailIndex = c.getColumnIndex("email");
        int fNameIndex = c.getColumnIndex("fName");
        int lNameIndex = c.getColumnIndex("lName");

        c.moveToFirst();

        String username = c.getString(usernameIndex);
        String password = c.getString(passwordIndex);
        String email = c.getString(emailIndex);
        String fName = c.getString(fNameIndex);
        String lName = c.getString(lNameIndex);

        c.close();

        return new User(userID, username, password, email, fName, lName);
    }

    public SecurityQuestion getUsersSecurityQuestion(long userID) {
        createSecurityQuestionTable();
        Cursor c = sqLiteDatabase.rawQuery("SELECT *, SecurityQuestions.rowid AS question_id FROM SecurityQuestions WHERE user_id = ?", new String[]{ userID + "" });

        int questionIDIndex = c.getColumnIndex("question_id");
        int questionTextIndex = c.getColumnIndex("question_text");
        int answerTextIndex = c.getColumnIndex("answer");

        c.moveToFirst();

        long qID = c.getLong(questionIDIndex);
        String qText = c.getString(questionTextIndex);
        String aText = c.getString(answerTextIndex);

        c.close();

        return new SecurityQuestion(qID, qText, aText);
    }

    public Park getParkById(long parkID) {
        createParkTable();
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM Parks WHERE Parks.rowid = ?", new String[]{ parkID + ""});

        int nameIndex = c.getColumnIndex("park_name");

        c.moveToFirst();

        String name = c.getString(nameIndex);
        double rating = getParkRating(parkID);

        Park park = new Park(parkID, name, rating);

        c.close();

        return park;
    }


    public User getUserByUsername(String user_lookup) {
        createUserTable();
        Cursor c = sqLiteDatabase.rawQuery("SELECT user_id FROM Users WHERE username = ?", new String[]{ user_lookup + ""});

        int userIDIndex = c.getColumnIndex("user_id");

        c.moveToFirst();

        long user_id = c.getLong(userIDIndex);

        c.close();

        return getUserByID(user_id);
    }

    public ArrayList<Park> getUsersFavoriteParks(long userID) {
        createParkTable();
        createFavoriteParkTable();

        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM FavoriteParks WHERE user_id = ?", new String[]{ userID + ""});

        int parkIDIndex = c.getColumnIndex("park_id");
        c.moveToFirst();

        ArrayList<Park> parkList = new ArrayList<>();

        while(!c.isAfterLast()) {

            long parkID = c.getLong(parkIDIndex);

            Park favoritePark = getParkById(parkID);

            parkList.add(favoritePark);
            c.moveToNext();
        }

        return parkList;
    }

    public boolean addUserFavoritePark(Park park, User user) {
        createParkTable();
        createFavoriteParkTable();

        sqLiteDatabase.execSQL(String.format("INSERT INTO FavoriteParks (park_id, user_id) VALUES ('%s', '%s');", park.getParkID(), user.getUserID()));
        return true;
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
        String db_pass;
        if( c != null && c.moveToFirst() ){
            db_pass = c.getString(passwordIndex);
            c.close();
        }
        else {
            c.close();
            return false;
        }

        return password.equals(db_pass);
    }

    /**
     * Check whether a username is in the database
     * @param username to check against the database
     * @return boolean if user exists in database
     */
    public boolean userExists(String username) {
        createUserTable();
        Cursor c = sqLiteDatabase.rawQuery("SELECT username FROM Users", null);

        int usernameIndex = c.getColumnIndex("username");
        c.moveToFirst();

        while(!c.isAfterLast()) {
            Log.i("LOGIN", "check exists");
            if(c.getString(usernameIndex).equals(username)) {
                return true;
            } else {
                Log.i("CHECK", username + " != " + c.getString(usernameIndex));
            }
            c.moveToNext();
        }

        return false;
    }

    public boolean parkExists(long parkID) {
        createParkTable();

        Cursor c = sqLiteDatabase.rawQuery("SELECT Parks.rowid AS park_id FROM Parks", null);

        int parkIndex = c.getColumnIndex("park_id");
        c.moveToFirst();

        boolean parkExists = false;

        while(!c.isAfterLast()) {
            Log.i("LOGIN", "check exists");
            if(c.getLong(parkIndex) == parkID) {
                parkExists = true;
            }
            c.moveToNext();
        }

        return parkExists;
    }
}
