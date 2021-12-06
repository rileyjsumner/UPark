package com.example.upark;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class FindPark extends AppCompatActivity {

    private double lat;
    private double lon;
    LocationManager locationManager;
    LocationListener locationListener;

    //this might be a problem later -> @SuppressLint("MissingPermission")
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_park);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                updateLocationInfo(location);
            }
            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }
            @Override
            public void onProviderEnabled(String s) {

            }
            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if(Build.VERSION.SDK_INT < 23) {
            startListening();
        }
        else {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(location != null) {
                    updateLocationInfo(location);
                }
            }
        }
        Button button = (Button) findViewById(R.id.searchButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                buttonPressed();
            }
        });
    }

    public void startListening() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults );

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startListening();
        }
    }

    public void updateLocationInfo(Location location) {

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            String address = "Could not find address";
            List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if (listAddresses != null && listAddresses.size() > 0) {
                Log.i("PlaceInfo", listAddresses.get(0).toString());
                address = "Address: \n";

                if (listAddresses.get(0).getSubThoroughfare() != null) {
                    address += listAddresses.get(0).getSubThoroughfare() + " ";
                }
                if (listAddresses.get(0).getThoroughfare() != null) {
                    address += listAddresses.get(0).getThoroughfare() + "\n";
                }
                if (listAddresses.get(0).getLocality() != null) {
                    address += listAddresses.get(0).getLocality() + "\n";
                }
                if (listAddresses.get(0).getPostalCode() != null) {
                    address += listAddresses.get(0).getPostalCode() + "\n";
                }
                if (listAddresses.get(0).getCountryCode() != null) {
                    address += listAddresses.get(0).getCountryCode() + "\n";
                }
            }
            lat = location.getLatitude();
            lon = location.getLongitude();
            Log.i("Location", "Lat: " + lat + " Lon: " + lon);
        }

        catch (IOException e) {
            e.printStackTrace();

        }
    }

    /* gets API value */
    //TODO: change this to non static
    public String getPropVal() {
        return "AIzaSyBYa31olm4mK-g37bt36pDQ2gwJAR3eyzA";
        //return BuildConfig.API_KEY; // TODO: maybe able to replace w/ BuildConfig.MAPS_API_KEY, see: https://stackoverflow.com/questions/32117413/how-to-read-local-properties-android-in-java-files

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
                    url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=" + searchInput.getText().toString().replaceAll(" ", "%")+ "&location=" + lat + "%2C" + lon + "&radius=" + "20000" + "&key=" + getPropVal();
                }
                else {
                    url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=" + searchInput.getText().toString().replaceAll(" ", "%") + "%parks" + "&location=" + lat + "%2C" + lon + "&radius=" + "20000" + "&key=" + getPropVal();
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
        ArrayList<String> idArrayList = new  ArrayList<String>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                // TODO: create park object instead and add to arraylist
                arrayList.add(jsonArray.getJSONObject(i).getString("name"));
                idArrayList.add(jsonArray.getJSONObject(i).getString("place_id"));
                Log.i("Array list adding: ", arrayList.get(i));
                Log.i("Array list ID adding: ", idArrayList.get(i));
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