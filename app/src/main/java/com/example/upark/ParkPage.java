package com.example.upark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.upark.DAO.Park;
import com.example.upark.DAO.Review;
import com.example.upark.DAO.User;
import com.example.upark.Database.DBHelper;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ParkPage extends AppCompatActivity {

    DBHelper db;
    Context context;
    ListView reviewList;
    public static ArrayList<Review> curr_reviews = new ArrayList<>();
    double[] park_lat_lon;
    double[] last_location;
    String current_user;
    Park found_park;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park_page);
        Intent intent = getIntent();
        current_user = intent.getStringExtra("current_user");
        Log.i("FETCH", current_user);
        context = getApplicationContext();
        db = new DBHelper(context.openOrCreateDatabase("upark", Context.MODE_PRIVATE,null));
        User user_obj = db.getUserByUsername(current_user);
        ArrayList<Park> parksList = db.readParks();

        String park_name = intent.getStringExtra("name");
        String park_placeid= intent.getStringExtra("place_id");
        double[] lastLocation = intent.getDoubleArrayExtra("coords");
        Log.i("tiger", "Intent PID: " + park_placeid);

        long park_id = intent.getLongExtra("park_id", -1);

        TextView title_view = (TextView)findViewById(R.id.parkName_TextView);
        title_view.setText(park_name);


        Park currentPark = null;

        for(Park p : parksList) {
            if(p.getPlaceID().equals(park_placeid) || p.getParkID() == park_id) {
                currentPark = p;
                found_park = p;
                break;
            }
        }
        if(currentPark == null) {
            Toast toast = Toast.makeText(context, "Error fetching park info!", Toast.LENGTH_LONG);
            toast.show();
            Intent intent1 = new Intent(ParkPage.this, HomeScreen.class);
            intent.putExtra("current_user", current_user);
            startActivity(intent1);
        } else {

            Log.i("Shark",currentPark.getParkName());
            TextView rating_view = (TextView) findViewById(R.id.rating_TextView);

            curr_reviews = db.readReviews(currentPark.getParkID());
            if (curr_reviews.size() == 0) {
                String rating = 0 + " out of 5 stars";
                rating_view.setText(rating);
            } else {
                double rating_total = 0;
                int rating_amt = 0;
                for (Review r : curr_reviews) {
                    rating_total += r.getRating();
                    rating_amt++;
                }
                String rating = rating_total / rating_amt + " out of 5 stars";
                rating_view.setText(rating);
            }

            Log.i("water", currentPark.getParkID() + "");
            String rating = currentPark.getRating() + " out of 5 stars";
            rating_view.setText(rating);

            TextView distance_view = (TextView) findViewById(R.id.distance_TextView);
            park_lat_lon = currentPark.getLoc();
            last_location = intent.getDoubleArrayExtra("coords");
            Log.i("tampa", "lat: " + lastLocation[0] + ", lon: " + lastLocation[1]);
            float[] results = new float[10];
            Location.distanceBetween(last_location[0], last_location[1], park_lat_lon[0], park_lat_lon[1], results);
            double miles = results[0] * 0.000621371192;
            DecimalFormat df = new DecimalFormat("###.##");
            String dist = df.format(miles) + " miles";
            distance_view.setText(dist);
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, curr_reviews);

        reviewList = (ListView)findViewById(R.id.review_list);

        reviewList.setAdapter(adapter);

        reviewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ViewReview.class);
                intent.putExtra("reviewid", position);
                intent.putExtra("current_user", current_user);
                intent.putExtra("name", found_park.getParkName());
                intent.putExtra("coords", last_location);
                String temp_placeid = found_park.getPlaceID();
                intent.putExtra("place_id", temp_placeid);
                startActivity(intent);
            }
        });

    }

    public void navigate(View v) {
        String uri = "http://maps.google.com/maps?f=d&hl=en&saddr="+last_location[0]+","+last_location[1]+"&daddr="+park_lat_lon[0]+","+park_lat_lon[1];
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(Intent.createChooser(intent, "Select an application"));
    }

    public void add_favorite(View v) {
        User curr = db.getUserByUsername(current_user);
        boolean add = false;
        add = db.addUserFavoritePark(found_park, curr);
        if(!add) {
            //throw some kind of error
            Toast toast = Toast.makeText(context, "Could not add park to Favorites", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void checkin(View v) {
        Intent intent = new Intent(ParkPage.this, CheckIn.class);
        intent.putExtra("current_user", current_user);
        if (found_park != null) {
            intent.putExtra("current_park", found_park.getParkID());
        } else {
            intent.putExtra("current_park", -1);
        }
        startActivity(intent);
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
            Intent intent = new Intent(ParkPage.this, HomeScreen.class);
            intent.putExtra("current_user", current_user);
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.my_account) {
            Intent intent = new Intent(ParkPage.this, Account.class);
            intent.putExtra("current_user", current_user);
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.find_parks) {
            Intent intent = new Intent(ParkPage.this, FindPark.class);
            intent.putExtra("current_user", current_user);
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.favorites) {
            Intent intent = new Intent(ParkPage.this, Favorites.class);
            intent.putExtra("current_user", current_user);
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