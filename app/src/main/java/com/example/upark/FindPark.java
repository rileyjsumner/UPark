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

import com.example.upark.DAO.Park;
import com.example.upark.Database.DBHelper;
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
    DBHelper db;
    Context context;
    String current_user;

    //this might be a problem later -> @SuppressLint("MissingPermission")
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_park);
        context = getApplicationContext();
        Intent intent = getIntent();
        current_user = intent.getStringExtra("current_user");
        db = new DBHelper(context.openOrCreateDatabase("upark", Context.MODE_PRIVATE,null));
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
        ArrayList<Park> newParks = new  ArrayList<Park>();
        ArrayList<String> arrayList = new ArrayList<String>();
        ArrayList<String> place_id_list = new ArrayList<String>();
        ArrayList<Park> existingParks = db.readParks();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                String this_name = jsonArray.getJSONObject(i).getString("name");
                String this_placeid = jsonArray.getJSONObject(i).getString("place_id");
                String curr_lat = jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getString("lat");
                String curr_lon = jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getString("lng");
                double p_lat = Double.parseDouble(curr_lat);
                double p_lon = Double.parseDouble(curr_lon);
                arrayList.add(this_name);
                place_id_list.add(this_placeid);
                int condition = -1;
                if(existingParks != null) {
                    for (Park p : existingParks) {
                        String temp_placeid = p.getPlaceID();
                        if (temp_placeid.equals(this_placeid)) {
                            condition = 1;
                            break;
                        }
                    }
                }
                if(condition == 1) {
                    //do nothing
                }
                else {
                    Park newPark = new Park(jsonArray.getJSONObject(i).getString("place_id"),
                            this_name, -1,
                            jsonArray.getJSONObject(i).getString("formatted_address"));
                    newPark.setLoc(p_lat,p_lon);
                    newParks.add(newPark);
                    Log.i("New Park", this_name);
                }
            }
            catch (JSONException e) {
                Log.i("FindPark", "JSON EXCEPTION: " + e);
            }
        }

        for(Park p: newParks) {
            db.addPark(p);
        }

        ArrayAdapter arrayAdapter =
                new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ParkPage.class);
                String temp_name = arrayList.get(position);
                intent.putExtra("name", temp_name);
                double[] current_loc = {lat, lon};
                intent.putExtra("coords", current_loc);
                String temp_placeid = place_id_list.get(position);
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
            Intent intent = new Intent(FindPark.this, Account.class);
            intent.putExtra("current_user", current_user);
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.find_parks) {
            Intent intent = new Intent(FindPark.this, FindPark.class);
            intent.putExtra("current_user", current_user);
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.favorites) {
            Intent intent = new Intent(FindPark.this, Favorites.class);
            intent.putExtra("current_user", current_user);
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.check_in) {
            Intent intent = new Intent(FindPark.this, CheckIn.class);
            intent.putExtra("current_user", current_user);
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