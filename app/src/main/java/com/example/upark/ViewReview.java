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

    public void go_back(View v) {
        Intent intent = new Intent(ViewReview.this, ParkPage.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_review);
        Intent intent = getIntent();
        reviewid = intent.getIntExtra("reviewid", -1);
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
        //TODO: add user content

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

        if(item.getItemId() == R.id.my_account) {
            Intent intent = new Intent(ViewReview.this, Account.class);
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.find_parks) {
            Intent intent = new Intent(ViewReview.this, FindPark.class);
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.favorites) {
            Intent intent = new Intent(ViewReview.this, Favorites.class);
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.check_in) {
            Intent intent = new Intent(ViewReview.this, CheckIn.class);
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