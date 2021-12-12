package com.example.upark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.Rating;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.upark.DAO.Park;
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