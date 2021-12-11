package com.example.upark.DAO;

import com.example.upark.DAO.Review;

public class Park {

    private long parkID;
    private String placeID;
    private String parkName;
    private double rating;
    private double distance;
    private final double[] loc = new double[2];
    private Review[] reviews;
    private String description;

    public Park(long parkID, String parkName, double rating) {
        this.parkName = parkName;
        this.rating = rating;
    }

    public Park(String parkName, double rating, String description) {
        this.parkName = parkName;
        this.rating = rating;
        this.description = description;
    }

    public Park(long parkID, String placeID, String parkName, double rating, String description) {
        this.parkID = parkID;
        this.placeID = placeID;
        this.parkName = parkName;
        this.rating = rating;
        this.description = description;
    }

    public Park(String placeID, String parkName, double rating, String description) {
        this.placeID = placeID;
        this.parkName = parkName;
        this.description = description;
    }

    public long getParkID() {
        return parkID;
    }

    public void setParkID(int parkID) {
        this.parkID = parkID;
    }

    public String getPlaceID() { return placeID; }

    public void setPlaceID(String placeID) { this.placeID = placeID; }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
