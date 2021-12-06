package com.example.upark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

import java.util.ArrayList;

public class HomeScreen extends AppCompatActivity {
    // TODO: remove these hard-coded values once loop implemented
    private final LatLng mParkLatLng1 = new LatLng(43.06008520706912, -89.41330708131657);
    private final LatLng mParkLatLng2 = new LatLng(43.04369413370016, -89.42463949218964);
    private final LatLng mParkLatLng3 = new LatLng(43.081909785138, -89.38018043332146);
    private final LatLng mLatLng = new LatLng(43.06008520706912, -89.41330708131657); // store latlng that determines zoom level of map
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient; // Save the instance
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 12; // can be any num
    String current_user;
    String selectedPark;

    public void findParks(View view) {
        Intent intent = new Intent(HomeScreen.this, FindPark.class);
        startActivity(intent);
    }

    public void favorites(View view) {
        Intent intent = new Intent(HomeScreen.this, Favorites.class);
        startActivity(intent);

    }

    public void checkIn(View view) {
        Intent intent = new Intent(HomeScreen.this, CheckIn.class);
        startActivity(intent);

    }

    public void parkPage(View view) {
        Intent intent = new Intent(HomeScreen.this, ParkPage.class);
        intent.putExtra("name",selectedPark);
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Intent intent = getIntent();
        current_user = intent.getStringExtra("current_user");
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

            //Maps listener for map preview
            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean  onMarkerClick(Marker marker) {
                    Double pr = 0.0;
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

                    Context context;
                    DBHelper db;
                    context = getApplicationContext();
                    db = new DBHelper(context.openOrCreateDatabase("upark", Context.MODE_PRIVATE,null));
                    ArrayList<Park> parks = db.readParks();
                    Park newPark = new Park("James Madison Park",4.3, "This is a great park! It has basketball courts, volleyball courts, and a great view.");
                    Park newPark2 = new Park("Arboretum",2.8, "The arboretum is a wonderful place to visit.");
                    Park newPark3 = new Park("Henry Vilas Park",1.2, "This is the best park in the vilas neighborhood by far.");
                    parks.add(newPark);
                    parks.add(newPark2);
                    parks.add(newPark3);

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
        if(item.getItemId() == R.id.check_in) {
            Intent intent = new Intent(HomeScreen.this, CheckIn.class);
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