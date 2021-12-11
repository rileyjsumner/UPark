package com.example.upark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.upark.DAO.Park;
import com.example.upark.DAO.User;
import com.example.upark.Database.DBHelper;

import java.util.ArrayList;

public class Account extends AppCompatActivity {

    TextView accountUsername;
    TextView parkVisits;
    String current_user;
    DBHelper db;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        context = getApplicationContext();
        db = new DBHelper(context.openOrCreateDatabase("upark", Context.MODE_PRIVATE,null));

        Intent intent = getIntent();
        accountUsername = (TextView)findViewById(R.id.username_account_textview);
        String current_user = intent.getStringExtra("current_user");
        User user_obj = db.getUserByUsername(current_user);
        String full_name = user_obj.getfName() + " " + user_obj.getlName();
        accountUsername.setText(full_name);
        //TODO: Parks visited still needs set somewhere. This works but will crash currently as no
        //      parks have been set to visited
        /**
        Park[] parks_visited = user_obj.getParksVisited();
        int num_parks_visited = parks_visited.length;
        String visits = "Total Parks Visited: " + num_parks_visited;
        parkVisits = (TextView)findViewById(R.id.totalparksvisted_textview);
        parkVisits.setText(visits);
        */
        long user_id = user_obj.getUserID();
        ArrayList<Park> favorite_list = db.getUsersFavoriteParks(user_id);
        ArrayList<String> park_names = new ArrayList<>();
        for(Park p: favorite_list) {
            park_names.add(p.getParkName());
        }
        ArrayAdapter arrayAdapter =
                new ArrayAdapter(this, android.R.layout.simple_list_item_1, park_names);
        ListView listView = findViewById(R.id.favorites_list_account);
        listView.setAdapter(arrayAdapter);
        //set on click maybe

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
            Intent intent = new Intent(Account.this, Account.class);
            intent.putExtra("current_user", current_user);
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.find_parks) {
            Intent intent = new Intent(Account.this, FindPark.class);
            intent.putExtra("current_user", current_user);
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.favorites) {
            Intent intent = new Intent(Account.this, Favorites.class);
            intent.putExtra("current_user", current_user);
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.check_in) {
            Intent intent = new Intent(Account.this, CheckIn.class);
            intent.putExtra("current_user", current_user);
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.log_out) {
            Intent intent = new Intent(Account.this, MainActivity.class);
            startActivity(intent);
            return true;
        }

        return false;
    }
}