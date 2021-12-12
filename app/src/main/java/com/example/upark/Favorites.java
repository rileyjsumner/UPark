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
import android.widget.Toast;

import com.example.upark.DAO.Park;
import com.example.upark.DAO.User;
import com.example.upark.Database.DBHelper;

import java.util.ArrayList;

public class Favorites extends AppCompatActivity {

    String current_user;
    Context context;
    DBHelper db;
    double current_lat;
    double current_lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        Intent intent = getIntent();
        current_user = intent.getStringExtra("current_user");
        current_lat = intent.getDoubleExtra("lat", 0);
        current_lon = intent.getDoubleExtra("lon", 0);
        context = getApplicationContext();
        db = new DBHelper(context.openOrCreateDatabase("upark", Context.MODE_PRIVATE,null));
        User user = db.getUserByUsername(current_user);
        ArrayList<Park> favorite_parks = db.getUsersFavoriteParks(user.getUserID());
        ArrayList<String> park_names = new ArrayList<>();
        for(Park p: favorite_parks) {
            park_names.add(p.getParkName());
        }
        ArrayAdapter arrayAdapter =
                new ArrayAdapter(this, android.R.layout.simple_list_item_1, park_names);
        ListView listView = findViewById(R.id.favoritelistview);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ParkPage.class);
                boolean found = false;
                long parkid = favorite_parks.get(position).getParkID();
                ArrayList<Park> park_list= db.readParks();
                String temp_name = park_names.get(position);
                Park selected = null;
                for(Park p : park_list) {
                    if(p.getParkName().equals(temp_name) && p.getParkID() == parkid) {
                        selected = p;
                        found = true;
                        break;
                    }
                }
                if(!found) {
                    Toast toast = Toast.makeText(context, "Could not find park please contact app administrator", Toast.LENGTH_LONG);
                    toast.show();
                }
                
                intent.putExtra("name", temp_name);
                double[] current_loc = {current_lat, current_lon};
                intent.putExtra("coords", current_loc);
                String temp_placeid = selected.getPlaceID();
                intent.putExtra("place_id", temp_placeid);
                intent.putExtra("current_user", current_user);

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
            Intent intent = new Intent(Favorites.this, Account.class);
            intent.putExtra("current_user", current_user);
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.find_parks) {
            Intent intent = new Intent(Favorites.this, FindPark.class);
            intent.putExtra("current_user", current_user);
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.favorites) {
            Intent intent = new Intent(Favorites.this, Favorites.class);
            intent.putExtra("current_user", current_user);
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.check_in) {
            Intent intent = new Intent(Favorites.this, CheckIn.class);
            intent.putExtra("current_user", current_user);
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.log_out) {
            Intent intent = new Intent(Favorites.this, MainActivity.class);
            intent.putExtra("current_user", current_user);
            startActivity(intent);
            return true;
        }

        return false;
    }
}