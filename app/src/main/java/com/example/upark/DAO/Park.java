package com.example.upark.DAO;

import com.example.upark.DAO.Review;

public class Park {

    int parkID;
    String placeID;
    String parkName;
    double rating;
    double distance;
    double loc[] = new double[2];
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

    public String getPlaceID() { return placeID }

    public void setPlaceID(String placeID) { this.placeID = placeID }

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

    public void setLoc(double lat, double lan) {
        this.loc[0] = lat;
        this.loc[1] = lan;
    }

    public double[] getLoc() { return loc; }

    public void setDistance(double distance) { this.distance = distance; }

    public double getDistance() { return distance; }
}
