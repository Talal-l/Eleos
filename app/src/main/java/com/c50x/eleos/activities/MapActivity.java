package com.c50x.eleos.activities;


import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.c50x.eleos.R;
import com.c50x.eleos.data.Venue;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "MapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private Boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;
    private Venue gameVenue;
    private Gson gson;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;
        String[] coordinate = gameVenue.getVenueCoordinate().split(",");

        double la  = Double.parseDouble(coordinate[0]);
        double lng = Double.parseDouble(coordinate[1]);

        // zoom to location and place a marker
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(la, lng), 16));
        mMap.addMarker(new MarkerOptions().position(new LatLng(la,lng)).title(gameVenue.getVenueName()));


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);

        gson = new Gson();

        // get selected venue object
        String gameVenueJson = getIntent().getStringExtra("gameVenue");
        Log.i(TAG, "onCreate gameVenueJson: " + gameVenueJson);
        gameVenue = gson.fromJson(gameVenueJson, Venue.class);


        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapActivity.this);


    }


    private boolean checkReady() {
        if (mMap == null) {
            Toast.makeText(this, "Map not ready yet", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void onGoToBondi(View view) {
        if (!checkReady()) {
            return;
        }

    }


}
