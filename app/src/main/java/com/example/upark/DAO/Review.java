package com.example.upark.DAO;

import android.media.Image;

import com.example.upark.DAO.User;

public class Review {

    private long ratingID;
    private long parkID;
    private double rating;
    private String reviewText;
    private boolean isBikeFriendly;
    private boolean isChildFriendly;
    private boolean isDisabilityFriendly;
    private boolean isWooded;
    private boolean isCarAccessible;
    private boolean isPetFriendly;
    private User reviewer;

    private Image[] images;

    public Review(long ratingID, long parkID, double rating, String reviewText, boolean isBikeFriendly, boolean isChildFriendly, boolean isDisabilityFriendly, boolean isWooded, boolean isCarAccessible, boolean isPetFriendly, User reviewer, Image[] images) {
        this.ratingID = ratingID;
        this.parkID = parkID;
        this.rating = rating;
        this.reviewText = reviewText;
        this.isBikeFriendly = isBikeFriendly;
        this.isChildFriendly = isChildFriendly;
        this.isDisabilityFriendly = isDisabilityFriendly;
        this.isWooded = isWooded;
        this.isCarAccessible = isCarAccessible;
        this.isPetFriendly = isPetFriendly;
        this.reviewer = reviewer;
        this.images = images;
    }

    public long getRatingID() {
        return ratingID;
    }

    public void setRatingID(long ratingID) {
        this.ratingID = ratingID;
    }

    public long getParkID() {
        return parkID;
    }

    public void setParkID(long parkID) {
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

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public Image[] getImages() {
        return images;
    }

    public void setImages(Image[] images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return reviewer.getUsername() + ": " + (reviewText.length() > 45 ? reviewText.substring(0, 45) + "..." : reviewText);
    }
}
