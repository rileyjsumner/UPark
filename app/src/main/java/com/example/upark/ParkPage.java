package com.example.upark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.upark.DAO.Park;
import com.example.upark.DAO.Review;
import com.example.upark.Database.DBHelper;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ParkPage extends AppCompatActivity {

    DBHelper db;
    Context context;
    ListView reviewList;
    public static ArrayList<Review> curr_reviews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park_page);

        context = getApplicationContext();
        db = new DBHelper(context.openOrCreateDatabase("upark", Context.MODE_PRIVATE,null));

        ArrayList<Park> parksList = db.readParks();

        Intent intent_in = getIntent();
        String park_name = intent_in.getStringExtra("park_name");

        TextView title_view = (TextView)findViewById(R.id.parkName_TextView);
        title_view.setText(park_name);

        Park currentPark = null;

        for(Park p : parksList) {
            if(p.getParkName().equals(park_name)) {
                currentPark = p;
                break;
            }
        }
        if(currentPark == null) {
            //TODO throw some kind of error
        }
        TextView rating_view = (TextView)findViewById(R.id.rating_TextView);
        String rating = currentPark.getRating() + " out of 5 stars";
        rating_view.setText(rating);

        TextView distance_view = (TextView)findViewById(R.id.distance_TextView);
        String dist = currentPark.getDistance() + " miles";
        distance_view.setText(dist);

        curr_reviews = db.readReviews(currentPark.getParkID());

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, curr_reviews);

        reviewList = (ListView)findViewById(R.id.review_list);

        reviewList.setAdapter(adapter);

        reviewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ViewReview.class);
                intent.putExtra("reviewid", position);
                startActivity(intent);
            }
        });

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
            Intent intent = new Intent(ParkPage.this, Account.class);
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.find_parks) {
            Intent intent = new Intent(ParkPage.this, FindPark.class);
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.favorites) {
            Intent intent = new Intent(ParkPage.this, Favorites.class);
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.check_in) {
            Intent intent = new Intent(ParkPage.this, CheckIn.class);
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.log_out) {
            Intent intent = new Intent(ParkPage.this, MainActivity.class);
            startActivity(intent);
            return true;
        }

        return false;
    }
}