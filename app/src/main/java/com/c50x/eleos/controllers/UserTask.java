package com.c50x.eleos.controllers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.c50x.eleos.R;
import com.c50x.eleos.data.AppDatabase;
import com.c50x.eleos.data.User;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class UserTask {
    private AppDatabase db;
    private Context context;
    private User userList[];

    private User userist[];
    private String urlBase;
    private Gson gson;
    private AsyncResponse activityContext;

    private static final String TAG = "UserTask";

    public UserTask(Context activityContext) {
        // load the server address from string.xml
        gson = new Gson();
        this.activityContext = (AsyncResponse)activityContext;
        urlBase = activityContext.getString(R.string.server_address);

    }

     public void loadUser (String handle){

        String script = "/loadUser.php";
        String key = "handle";

        String url = urlBase + script;
        Log.i(TAG, "using server address: " + urlBase);
        Log.i(TAG, "url: " + url);

        new AsyncGet(activityContext).execute(url,key, handle);

     }

     public void searchPlayer (String handle){

        String script = "/searchPlayer.php";
        String key = "handle";

        String url = urlBase + script;
        Log.i(TAG, "using server address: " + urlBase);
        Log.i(TAG, "url: " + url);

        new AsyncGet(activityContext).execute(url,key, handle);
     }
}
