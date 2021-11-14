package com.example.upark.DAO;

import com.example.upark.DAO.Review;

public class Park {

    int parkID;
    String parkName;
    double rating;
    double distance;

    Review[] reviews;

    public Park(String parkName, double rating) {
        this.parkName = parkName;
        this.rating = rating;
    }

    public int getParkID() {
        return parkID;
    }

    public void setParkID(int parkID) {
        this.parkID = parkID;
    }

    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Review[] getReviews() {
        return reviews;
    }

    public void setReviews(Review[] reviews) {
        this.reviews = reviews;
    }

    public void setDistance(double distance) { this.distance = distance; }

    public double getDistance() { return distance; }
}
