package com.example.upark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Rating;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.upark.DAO.Park;
import com.example.upark.DAO.Review;
import com.example.upark.DAO.User;
import com.example.upark.Database.DBHelper;

import org.w3c.dom.Text;

public class CheckIn extends AppCompatActivity {
    Context context;
    DBHelper db;
    String current_user;
    long curr_park_id;
    Park curr_park;
    User curr_user;
    TextView park_label;
    RatingBar rating_bar;
    TextView chars_label;
    EditText review_field;
    Button submit_button;
    CheckBox woods;
    CheckBox disability;
    CheckBox pet;
    CheckBox car;
    CheckBox bike;
    CheckBox child;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

        context = getApplicationContext();
        db = new DBHelper(context.openOrCreateDatabase("upark", Context.MODE_PRIVATE,null));

        Intent intent = getIntent();
        // TODO: removed String (should i have?)
        current_user = intent.getStringExtra("current_user"); // curr username str
        curr_park_id = intent.getLongExtra("current_park", 0);
        curr_user = db.getUserByUsername(current_user);
        curr_park = db.getParkById(curr_park_id);

        // get/store elements
        park_label = (TextView) findViewById(R.id.parkName);
        rating_bar = (RatingBar) findViewById(R.id.ratingBar);
        chars_label = (TextView) findViewById(R.id.charLimit);
        review_field = (EditText) findViewById(R.id.reviewText);
        submit_button = (Button) findViewById(R.id.submitButton);
        woods = (CheckBox) findViewById(R.id.treeCheck);
        disability = (CheckBox) findViewById(R.id.abilityCheck);
        pet = (CheckBox) findViewById(R.id.petCheck);
        car = (CheckBox) findViewById(R.id.carCheck);
        bike = (CheckBox) findViewById(R.id.bikeCheck);
        child = (CheckBox) findViewById(R.id.childCheck);

        park_label.setText(curr_park.getParkName());
        chars_label.setTextColor(Color.parseColor("#cf4b23")); // set to red since chars 0

        review_field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int charCount = review_field.length();
                String countStr = String.valueOf(charCount);

                chars_label.setText(countStr + "/500");

                if (charCount > 25 && charCount < 501) {
                    chars_label.setTextColor(Color.parseColor("#81c45c")); // set to red since num out of range
                } else {
                    chars_label.setTextColor(Color.parseColor("#cf4b23")); // set to red since num out of range
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void submitPressed(View view) {
        int charCount = review_field.getText().length();
        double rating = rating_bar.getRating();
        boolean woodsy = woods.isChecked();
        boolean disability_friend = disability.isChecked();
        boolean pet_friend = pet.isChecked();
        boolean car_access = car.isChecked();
        boolean bike_friend = bike.isChecked();
        boolean child_friend = child.isChecked();

        if (rating == 0) { // if ratingBar blank
            String toastText = "Please select a rating.";
            Toast.makeText(CheckIn.this, toastText, Toast.LENGTH_LONG).show();

        } else if (charCount == 0) {
            review_field.setError("Review can't be blank.");

        } else if (charCount < 25) {
            String toastText = "Write some more before submitting.";
            Toast.makeText(CheckIn.this, toastText, Toast.LENGTH_LONG).show();

        } else if (charCount > 500) {
            String toastText = "You went over the character limit. Edit your review before submitting.";
            Toast.makeText(CheckIn.this, toastText, Toast.LENGTH_LONG).show();

        } else { // success
            // create review
            String review_text = review_field.getText().toString();
            // TODO: sanitize review text
            String clean_review = sanitize(review_text);

            Review user_review = new Review(0, curr_park_id, rating, clean_review,
                    bike_friend,
                    child_friend,
                    disability_friend,
                    woodsy,
                    car_access,
                    pet_friend,
                    curr_user,
                    null
                    );

            // save review
            db.addReview(user_review);

            String toastText = "Review added successfully. Thank you!";
            Toast.makeText(CheckIn.this, toastText, Toast.LENGTH_LONG).show();

            // go back to park page
            goBackToPark();
        }
    }

    /*
    * Replace ' in strings so db accepts
    * */
    public String sanitize(String text) {
        return text.replace("\'", "''");
    }

    public void goBackToPark() {
        Intent intent = new Intent(this, ParkPage.class); // TODO: edit
        intent.putExtra("name",curr_park.getParkName());
        intent.putExtra("current_user",curr_user.getUsername());
        intent.putExtra("place_id",curr_park_id);
        startActivity(intent);
        // TODO: look at find parks to see how to do it
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
            Intent intent = new Intent(CheckIn.this, Account.class);
            intent.putExtra("current_user", current_user);
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.find_parks) {
            Intent intent = new Intent(CheckIn.this, FindPark.class);
            intent.putExtra("current_user", current_user);
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.favorites) {
            Intent intent = new Intent(CheckIn.this, Favorites.class);
            intent.putExtra("current_user", current_user);
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.log_out) {
            Intent intent = new Intent(CheckIn.this, MainActivity.class);
            startActivity(intent);
            return true;
        }

        return false;
    }
}