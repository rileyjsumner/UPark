package com.example.upark.DAO;

import android.media.Image;

import java.util.ArrayList;

public class User {

    int userID;
    Image profilePic;
    ArrayList<Review> reviewsGiven;
    ArrayList<Park> parksVisited;
    ArrayList<Park> favoriteParks;
    SecurityQuestion[] securityQuestions;

    String username;
    String password;
    String email;
    String fName;
    String lName;

    public User(String username, String password, String email, String fName, String lName) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.fName = fName;
        this.lName = lName;
    }

    public User(int userID, String username, String password, String email, String fName, String lName) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.email = email;
        this.fName = fName;
        this.lName = lName;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public Image getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Image profilePic) {
        this.profilePic = profilePic;
    }

    public ArrayList<Review> getReviewsGiven() {
        return reviewsGiven;
    }

    public void setReviewsGiven(ArrayList<Review> reviewsGiven) {
        this.reviewsGiven = reviewsGiven;
    }

    public ArrayList<Park> getParksVisited() {
        return parksVisited;
    }

    public void setParksVisited(ArrayList<Park> parksVisited) {
        this.parksVisited = parksVisited;
    }

    public ArrayList<Park> getFavoriteParks() {
        return favoriteParks;
    }

    public void setFavoriteParks(ArrayList<Park> favoriteParks) {
        this.favoriteParks = favoriteParks;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }
}
