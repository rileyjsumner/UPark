package com.example.upark;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.ResourceBundle;

public class FindPark extends AppCompatActivity {
    public String placesJSON = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PlacesRunnable runnable = new PlacesRunnable();
        new Thread(runnable).start();

    }

    public class PlacesRunnable implements Runnable {
        @Override
        public void run() {
            placesJSON = getPlaces();
        }
    }

    public String getPlaces() {
        // TODO: edit URL further to customize lat and long
        String s1 = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522%2C151.1957362&radius=1500&type=park&keyword=park&key=";
        String s2 = getPropVal();
        String url = String.format("%s%s",s1,s2);

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null; // TODO fix
    }

    /* gets API value */
    public String getPropVal() {
        return BuildConfig.MAPS_API_KEY; // TODO: maybe able to replace w/ BuildConfig.MAPS_API_KEY, see: https://stackoverflow.com/questions/32117413/how-to-read-local-properties-android-in-java-files

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
            Intent intent = new Intent(FindPark.this, Account.class);
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.find_parks) {
            Intent intent = new Intent(FindPark.this, FindPark.class);
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.favorites) {
            Intent intent = new Intent(FindPark.this, Favorites.class);
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.check_in) {
            Intent intent = new Intent(FindPark.this, CheckIn.class);
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.log_out) {
            Intent intent = new Intent(FindPark.this, MainActivity.class);
            startActivity(intent);
            return true;
        }

        return false;
    }
}