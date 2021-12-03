package com.example.upark;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.ArrayList;
import java.util.ResourceBundle;

public class FindPark extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_park);

        Button button = (Button) findViewById(R.id.searchButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                buttonPressed();
            }
        });
    }

    /* gets API value */
    //TODO: change this to non static
    public String getPropVal() {
        //return "AIzaSyBYa31olm4mK-g37bt36pDQ2gwJAR3eyzA";
        return BuildConfig.MAPS_API_KEY; // TODO: maybe able to replace w/ BuildConfig.MAPS_API_KEY, see: https://stackoverflow.com/questions/32117413/how-to-read-local-properties-android-in-java-files

    }

    //When button pressed, get api data and show in list view
    public void buttonPressed() {
        TextView searchInput = (TextView) findViewById(R.id.parkSearch);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {


                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                String url;
                if (searchInput.getText().toString().contains("park")) {
                    url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=" + searchInput.getText().toString().replaceAll(" ", "%")+ "&key=" + getPropVal();
                }
                else {
                    url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=" + searchInput.getText().toString().replaceAll(" ", "%") + "%parks" + "&key=" + getPropVal();
                }

                //Get request from places API
                Request request = new Request.Builder()
                        .url(url)
                        .method("GET", null)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String data = response.body().string();

                    try {
                        //Creating json object for request
                        JSONObject json = new JSONObject(data);
                        String newData = json.getString("results");
                        String newJsonString = "{\"Parks\" : " + json.getString("results") + "}";

                        //Create new JSON array with park results
                        JSONArray JsonArray = json.getJSONArray("results");
                        String testVal = JsonArray.getJSONObject(0).getString("name");


                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                populate(JsonArray);
                            }
                        });
                        
                    } catch (JSONException e) {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "No Parks Found", Toast.LENGTH_SHORT).show();
                            }
                        });

                        Log.i("FindPark", "JSON ERROR: " + e);
                    }

                } catch (IOException e) {
                    Log.i("FindPark", "IO Exception: " + e);
                }


            }
        });
    }


    //Populates list view with json array values
    public void populate(JSONArray jsonArray) {
        ArrayList<String> arrayList = new  ArrayList<String>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                arrayList.add(jsonArray.getJSONObject(i).getString("name"));
                Log.i("Array list adding: ", arrayList.get(i));
            }
            catch (JSONException e) {
                Log.i("FindPark", "JSON EXCEPTION: " + e);
            }
        }

        ArrayAdapter arrayAdapter =
                new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(arrayAdapter);

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