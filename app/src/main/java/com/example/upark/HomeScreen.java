package com.example.upark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
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

import com.example.upark.DAO.Park;
import com.example.upark.Database.DBHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeScreen extends AppCompatActivity {
    // TODO: remove these hard-coded values once loop implemented
    LocationManager locationManager;
    LocationListener locationListener;
    private LatLng mParkLatLng1 = new LatLng(43.06008520706912, -89.41330708131657);
    private LatLng mParkLatLng2 = new LatLng(43.04369413370016, -89.42463949218964);
    private LatLng mParkLatLng3 = new LatLng(43.081909785138, -89.38018043332146);
    String p1;
    String p2;
    String p3;
    private LatLng mLatLng = new LatLng(43.06008520706912, -89.41330708131657); // store latlng that determines zoom level of map
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient; // Save the instance
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 12; // can be any num
    String current_user;
    String selectedPark;
    double lat;
    double lon;
    Context context;
    DBHelper db;
    Marker m1;
    Marker m2;
    Marker m3;
    SupportMapFragment mapFragment;

    public void findParks(View view) {
        Intent intent = new Intent(HomeScreen.this, FindPark.class);
        intent.putExtra("current_user", current_user);
        startActivity(intent);
    }

    public void favorites(View view) {
        Intent intent = new Intent(HomeScreen.this, Favorites.class);
        intent.putExtra("current_user", current_user);
        intent.putExtra("lat", lat);
        intent.putExtra("lon", lon);
        startActivity(intent);

    }

    public void account(View view) {
        Intent intent = new Intent(HomeScreen.this, Account.class);
        intent.putExtra("current_user", current_user);
        startActivity(intent);

    }

    public void parkPage(View view) {
        Intent intent = new Intent(HomeScreen.this, ParkPage.class);
        intent.putExtra("name",selectedPark);
        intent.putExtra("current_user", current_user);
        startActivity(intent);
    }

    public void close(View view) {
        CardView cv = findViewById(R.id.preview);
        TextView tv = findViewById(R.id.tv);
        Button x = findViewById(R.id.xButton);
        TextView pd = findViewById(R.id.parkDetails);
        Button d = findViewById(R.id.detailsButton);

        cv.setVisibility(View.INVISIBLE);
        tv.setVisibility(View.INVISIBLE);
        x.setVisibility(View.INVISIBLE);
        pd.setVisibility(View.INVISIBLE);
        d.setVisibility(View.INVISIBLE);
    }


    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        context = getApplicationContext();
        db = new DBHelper(context.openOrCreateDatabase("upark", Context.MODE_PRIVATE,null));
        Intent intent = getIntent();
        current_user = intent.getStringExtra("current_user");
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

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                String url;

                url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=" + "%parks" + "&location=" + lat + "%2C" + lon + "&radius=" + "20000" + "&key=" + getPropVal();

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

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        // TODO: get nearby parks and add them in a loop to the map (probably own function)
        mapFragment.getMapAsync(googleMap -> {
            mMap = googleMap;
            // zooms map to area (TODO: change to user location)
            float zoomLevel = 11.0f; // goes up to 21.0
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, zoomLevel));
            // code to display markers
            m1 = googleMap.addMarker(new MarkerOptions()
                    .position(mParkLatLng1)
                    .title(p1));
            m2 = googleMap.addMarker(new MarkerOptions()
                    .position(mParkLatLng2)
                    .title(p2));
            m3 = googleMap.addMarker(new MarkerOptions()
                    .position(mParkLatLng3)
                    .title(p3));

            //Maps listener for map preview
            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean  onMarkerClick(Marker marker) {

                    //Making preview visible
                    CardView cv = findViewById(R.id.preview);
                    TextView tv = findViewById(R.id.tv);
                    Button x = findViewById(R.id.xButton);
                    TextView pd = findViewById(R.id.parkDetails);
                    Button d = findViewById(R.id.detailsButton);

                    cv.setVisibility(View.VISIBLE);
                    tv.setVisibility(View.VISIBLE);
                    x.setVisibility(View.VISIBLE);
                    pd.setVisibility(View.VISIBLE);
                    d.setVisibility(View.VISIBLE);


                    //Trying to read parks, this is where I am not sure what to do because I am not getting parks.
                    ArrayList<Park> parks = db.readParks();
                    /**
                    ArrayList<Park> parks = new ArrayList<Park>();

                    //I added my own parks to test, these will be removed in the future.
                    Park newPark = new Park("James Madison Park",4.3, "This is a great park! It has basketball courts, volleyball courts, and a great view.");
                    Park newPark2 = new Park("Arboretum",2.8, "The arboretum is a wonderful place to visit.");
                    Park newPark3 = new Park("Henry Vilas Park",1.2, "This is the best park in the vilas neighborhood by far.");
                    parks.add(newPark);
                    parks.add(newPark2);
                    parks.add(newPark3);
                     */

                    //Going through list of parks to find the matching park and display data
                    boolean foundPark = false;
                    for(int i=0; i < parks.size(); i++) {

                        if (parks.get(i).getParkName().equals(marker.getTitle())) {
                            foundPark = true;
                            selectedPark = parks.get(i).getParkName();
                            pd.setText("Rating: " + parks.get(i).getRating() + "\n\nDescription: " + parks.get(i).getDescription());
                            tv.setText(marker.getTitle());
                            if (parks.get(i).getRating() < 1.7) {
                                cv.setCardBackgroundColor(getResources().getColor(R.color.red));
                            }
                            if ((parks.get(i).getRating() >= 1.7) && (parks.get(i).getRating() < 3.4)) {
                                cv.setCardBackgroundColor(getResources().getColor(R.color.yellow));
                            }
                            if (parks.get(i).getRating() >= 3.4) {
                                cv.setCardBackgroundColor(getResources().getColor(R.color.green));
                            }
                        }
                    }
                    if (!foundPark) {
                        pd.setText("No park information found.");
                        tv.setText(marker.getTitle());
                    }
                    return true;
                }
            });
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
            mLatLng = new LatLng(lat, lon);
            if(mMap != null) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 11.0f));
            }
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

    //Populate 3 marker lat lon
    public void populate(JSONArray jsonArray) {
        int count = 1;
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                String this_name = jsonArray.getJSONObject(i).getString("name");
                String this_placeid = jsonArray.getJSONObject(i).getString("place_id");
                String curr_lat = jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getString("lat");
                String curr_lon = jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getString("lng");
                double p_lat = Double.parseDouble(curr_lat);
                double p_lon = Double.parseDouble(curr_lon);
                Log.i("Populate Lat long", "Lat: " + p_lat + " Lon: " + p_lon);
                int condition = -1;
                if(db.readParks() == null) {
                    Park newPark = new Park(jsonArray.getJSONObject(i).getString("place_id"),
                            this_name, -1,
                            jsonArray.getJSONObject(i).getString("formatted_address"));
                    newPark.setLoc(p_lat,p_lon);
                    db.addPark(newPark);
                }
                else {
                    ArrayList<Park> existingParks = db.readParks();
                    for (Park p : existingParks) {
                        String temp_placeid = p.getPlaceID();
                        if (temp_placeid.equals(this_placeid)) {
                            condition = 1;
                            break;
                        }
                    }
                }
                if(condition == -1) {
                    Park newPark = new Park(jsonArray.getJSONObject(i).getString("place_id"),
                            this_name, -1,
                            jsonArray.getJSONObject(i).getString("formatted_address"));
                    newPark.setLoc(p_lat,p_lon);
                    db.addPark(newPark);
                }

                if(count == 1) {
                    mParkLatLng1 = new LatLng(p_lat, p_lon);
                    p1 = this_name;
                    count++;
                    m1.setPosition(mParkLatLng1);
                    m1.hideInfoWindow();
                    m1.showInfoWindow();
                }
                else if(count == 2) {
                    mParkLatLng2 = new LatLng(p_lat, p_lon);
                    p2 = this_name;
                    count++;
                    m2.setPosition(mParkLatLng2);
                    m2.hideInfoWindow();
                    m2.showInfoWindow();
                }
                else if(count == 3) {
                    mParkLatLng3 = new LatLng(p_lat, p_lon);
                    p3 = this_name;
                    count++;
                    m3.setPosition(mParkLatLng3);
                    m3.hideInfoWindow();
                    m3.showInfoWindow();
                }
                else {
                    break;
                }
            }
            catch (JSONException e) {
                Log.i("FindPark", "JSON EXCEPTION: " + e);
            }
        }


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
            Intent intent = new Intent(HomeScreen.this, Account.class);
            intent.putExtra("current_user", current_user);
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.find_parks) {
            Intent intent = new Intent(HomeScreen.this, FindPark.class);
            intent.putExtra("current_user", current_user);
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.favorites) {
            Intent intent = new Intent(HomeScreen.this, Favorites.class);
            intent.putExtra("current_user", current_user);
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.log_out) {
            Intent intent = new Intent(HomeScreen.this, MainActivity.class);
            startActivity(intent);
            return true;
        }

        return false;
    }
}