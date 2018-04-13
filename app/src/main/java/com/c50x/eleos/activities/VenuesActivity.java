package com.c50x.eleos.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.c50x.eleos.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class VenuesActivity extends AppCompatActivity {

    private final static String TAG = "VenuesActivity";
    private FusedLocationProviderClient fusedLocationClient;
    private String type;
    private double lat;
    private double lng;
    private String radius;
    private String key;
    private String baseUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venues);


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    getVenues(location);
                }
            }
        });
    }


    void getVenues(Location location) {

        baseUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";


        type = "stadium";
        radius = "90000";
        key = "AIzaSyAMje2_XXUxPOYCzgtUSKemL3hUEIPEvPM";




        lat = location.getLatitude();
        lng = location.getLongitude();

        String url = baseUrl + "location=" + lat + "," + lng + "&" + "radius=" + radius + "&" +
                "type=" + type + "&" + "key=" + key;

        Log.i(TAG, "url: " + url);
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url).build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                VenuesActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();

                        String responseBody = null;
                        try {
                            responseBody = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        PlaceResponse placeResponse = gson.fromJson(responseBody,
                                PlaceResponse.class);

                        displayVenues(placeResponse);

                    }

                });
            }
        });


    }


    void displayVenues(PlaceResponse placesResponse) {

        for (PlaceResult place : placesResponse.results) {
            Log.i(TAG, "place name: " + place.name + "Type: " + Arrays.toString(place.types));

        }
    }


    public class PlaceResult {
        // geometry;
        String icon;
        String id;
        String name;
        // Photos[]
        String place_id;
        String reference;
        //String scope;
        String types[];
        String vicinity;

    }

    public class PlaceResponse {

        String next_page_token;
        PlaceResult results[];
    }

}
