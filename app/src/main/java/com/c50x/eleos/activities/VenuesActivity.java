package com.c50x.eleos.activities;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.c50x.eleos.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;

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

         baseUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";


        type = "";
        radius = "5000";
        key = "AIzaSyAMje2_XXUxPOYCzgtUSKemL3hUEIPEvPM";


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    lat = location.getLatitude();
                    lng = location.getLongitude();

                    //https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=51.503186,-0.126446&radius=5000&types=hospital&key=AIzaSyAMje2_XXUxPOYCzgtUSKemL3hUEIPEvPM
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
                        public void onResponse(Call call, Response response) throws IOException {

                            Log.i(TAG, "Response: " + response.body().string());


                        }
                    });


                }


            }
        });


    }
}
