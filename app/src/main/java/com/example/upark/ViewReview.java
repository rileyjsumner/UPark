package com.example.upark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.upark.DAO.Park;
import com.example.upark.DAO.Review;
import com.example.upark.DAO.User;

public class ViewReview extends AppCompatActivity {

    int reviewid;
    String current_user;

    public void go_back(View v) {
        Intent intent = new Intent(ViewReview.this, ParkPage.class);
        Intent intent_in = getIntent();
        intent.putExtra("current_user", current_user);
        intent.putExtra("name", intent_in.getStringExtra("name"));
        intent.putExtra("place_id", intent_in.getStringExtra("place_id"));
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_review);
        Intent intent = getIntent();
        reviewid = intent.getIntExtra("reviewid", -1);
        current_user = intent.getStringExtra("current_user");
        Review review = ParkPage.curr_reviews.get(reviewid);

        TextView username = (TextView)findViewById(R.id.username_Review);
        User user = review.getReviewer();
        String name = user.getUsername();
        username.setText(name);

        TextView rating = (TextView) findViewById(R.id.rating_Review);
        Double rate = review.getRating();
        String user_rating = rate + " out of 5 stars";
        rating.setText(user_rating);

        TextView content = (TextView) findViewById(R.id.content_Review);
        String desc = review.getReviewText();
        content.setText(desc);

        TextView woods = (TextView) findViewById(R.id.woods_view);
        TextView disability = (TextView) findViewById(R.id.disability_view);
        TextView pets = (TextView) findViewById(R.id.pet_view);
        TextView car = (TextView) findViewById(R.id.car_view);
        TextView bike = (TextView) findViewById(R.id.bike_view);
        TextView child = (TextView) findViewById(R.id.child_view);

        String wood_val;
        String dis_val;
        String pets_val;
        String car_val;
        String bike_val;
        String child_val;

        if(review.isChildFriendly()) {
            child_val = "Child Friendly: Yes";
        }
        else {
            child_val = "Child Friendly: No";
        }
        child.setText(child_val);

        if(review.isBikeFriendly()) {
            bike_val = "Bike Friendly: Yes";
        }
        else {
            bike_val = "Bike Friendly: No";
        }
        bike.setText(bike_val);

        if(review.isCarAccessible()) {
            car_val = "Car Friendly: Yes";
        }
        else {
            car_val = "Car Friendly: No";
        }
        car.setText(car_val);

        if(review.isDisabilityFriendly()) {
            dis_val = "Disability Friendly: Yes";
        }
        else {
            dis_val = "Disability Friendly: No";
        }
        disability.setText(dis_val);

        if(review.isPetFriendly()) {
            pets_val = "Pet Friendly: Yes";
        }
        else {
            pets_val = "Pet Friendly: No";
        }
        pets.setText(pets_val);

        if(review.isWooded()) {
            wood_val = "Wooded: Yes";
        }
        else {
            wood_val = "Wooded: No";
        }
        woods.setText(wood_val);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.home_screen) {
            Intent intent = new Intent(ViewReview.this, HomeScreen.class);
            intent.putExtra("current_user", current_user);
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.my_account) {
            Intent intent = new Intent(ViewReview.this, Account.class);
            intent.putExtra("current_user", current_user);
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.find_parks) {
            Intent intent = new Intent(ViewReview.this, FindPark.class);
            intent.putExtra("current_user", current_user);
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.favorites) {
            Intent intent = new Intent(ViewReview.this, Favorites.class);
            intent.putExtra("current_user", current_user);
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.log_out) {
            Intent intent = new Intent(ViewReview.this, MainActivity.class);
            startActivity(intent);
            return true;
        }

        return false;
    }
}