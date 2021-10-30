package com.example.upark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class home_screen extends AppCompatActivity {

    public void find_parks(View view) {
        Intent intent = new Intent(home_screen.this, FindPark.class);
        startActivity(intent);
    }

    public void favorites(View view) {
        Intent intent = new Intent(home_screen.this, Favorites.class);
        startActivity(intent);

    }

    public void check_in(View view) {
        Intent intent = new Intent(home_screen.this, CheckIn.class);
        startActivity(intent);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
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

        if(item.getItemId() == R.id.find_parks) {
            Intent intent = new Intent(home_screen.this, FindPark.class);
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.favorites) {
            Intent intent = new Intent(home_screen.this, Favorites.class);
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.check_in) {
            Intent intent = new Intent(home_screen.this, CheckIn.class);
            startActivity(intent);
            return true;
        }

        return false;
    }
}