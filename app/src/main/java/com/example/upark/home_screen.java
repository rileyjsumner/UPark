package com.example.upark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class home_screen extends AppCompatActivity {
    // TODO: remove these hard-coded values once loop implemented
    private final LatLng mParkLatLng1 = new LatLng(43.06008520706912, -89.41330708131657);
    private final LatLng mParkLatLng2 = new LatLng(43.04369413370016, -89.42463949218964);
    private final LatLng mParkLatLng3 = new LatLng(43.081909785138, -89.38018043332146);
    private final LatLng mLatLng = new LatLng(43.06008520706912, -89.41330708131657); // store latlng that determines zoom level of map
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient; // Save the instance
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 12; // can be any num

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
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        // TODO: get nearby parks and add them in a loop to the map (probably own function)
        mapFragment.getMapAsync(googleMap -> {
            mMap = googleMap;
            // zooms map to area (TODO: change to user location)
            float zoomLevel = 11.0f; // goes up to 21.0
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, zoomLevel));
            // code to display markers
            googleMap.addMarker(new MarkerOptions()
                    .position(mParkLatLng1)
                    .title("Henry Vilas Park"));
            googleMap.addMarker(new MarkerOptions()
                    .position(mParkLatLng2)
                    .title("Arboretum"));
            googleMap.addMarker(new MarkerOptions()
                    .position(mParkLatLng3)
                    .title("James Madison Park"));
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
        if(item.getItemId() == R.id.log_out) {
            Intent intent = new Intent(home_screen.this, MainActivity.class);
            startActivity(intent);
            return true;
        }

        return false;
    }
}