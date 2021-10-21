package com.example.upark;

import android.media.Image;

public class Review {

    int ratingID;
    int parkID;
    double rating;
    boolean isBikeFriendly;
    boolean isChildFriendly;
    boolean isDisabilityFriendly;
    boolean isWooded;
    boolean isCarAccessible;
    boolean isPetFriendly;
    User reviewer;

    Image[] images;

    public Review(int parkID, double rating, boolean isBikeFriendly, boolean isChildFriendly, boolean isDisabilityFriendly, boolean isWooded, boolean isCarAccessible, boolean isPetFriendly, User reviewer, Image[] images) {
        this.parkID = parkID;
        this.rating = rating;
        this.isBikeFriendly = isBikeFriendly;
        this.isChildFriendly = isChildFriendly;
        this.isDisabilityFriendly = isDisabilityFriendly;
        this.isWooded = isWooded;
        this.isCarAccessible = isCarAccessible;
        this.isPetFriendly = isPetFriendly;
        this.reviewer = reviewer;
        this.images = images;
    }

    public int getRatingID() {
        return ratingID;
    }

    public void setRatingID(int ratingID) {
        this.ratingID = ratingID;
    }

    public int getParkID() {
        return parkID;
    }

    public void setParkID(int parkID) {
        this.parkID = parkID;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public boolean isBikeFriendly() {
        return isBikeFriendly;
    }

    public void setBikeFriendly(boolean bikeFriendly) {
        isBikeFriendly = bikeFriendly;
    }

    public boolean isChildFriendly() {
        return isChildFriendly;
    }

    public void setChildFriendly(boolean childFriendly) {
        isChildFriendly = childFriendly;
    }

    public boolean isDisabilityFriendly() {
        return isDisabilityFriendly;
    }

    public void setDisabilityFriendly(boolean disabilityFriendly) {
        isDisabilityFriendly = disabilityFriendly;
    }

    public boolean isWooded() {
        return isWooded;
    }

    public void setWooded(boolean wooded) {
        isWooded = wooded;
    }

    public boolean isCarAccessible() {
        return isCarAccessible;
    }

    public void setCarAccessible(boolean carAccessible) {
        isCarAccessible = carAccessible;
    }

    public boolean isPetFriendly() {
        return isPetFriendly;
    }

    public void setPetFriendly(boolean petFriendly) {
        isPetFriendly = petFriendly;
    }

    public User getReviewer() {
        return reviewer;
    }

    public void setReviewer(User reviewer) {
        this.reviewer = reviewer;
    }

    public Image[] getImages() {
        return images;
    }

    public void setImages(Image[] images) {
        this.images = images;
    }
}
