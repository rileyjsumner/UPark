package com.example.upark;

import android.media.Image;

public class User {

    int userID;
    Image profilePic;
    Review[] reviewsGiven;
    Park[] parksVisited;

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

    public Review[] getReviewsGiven() {
        return reviewsGiven;
    }

    public void setReviewsGiven(Review[] reviewsGiven) {
        this.reviewsGiven = reviewsGiven;
    }

    public Park[] getParksVisited() {
        return parksVisited;
    }

    public void setParksVisited(Park[] parksVisited) {
        this.parksVisited = parksVisited;
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
