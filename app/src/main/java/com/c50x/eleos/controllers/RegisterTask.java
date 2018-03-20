package com.c50x.eleos.controllers;

import android.content.Context;
import android.util.Log;

import com.c50x.eleos.R;
import com.c50x.eleos.data.User;
import com.google.gson.Gson;

public class RegisterTask {

    private static final String TAG = "RegisterTask";
    private AsyncResponse activityContext;
    private Gson gson;
    private Context context;
    private String urlBase;


    public RegisterTask(Context activityContext) {
        gson = new Gson();
        context = activityContext;
        urlBase = activityContext.getString(R.string.server_address);
        this.activityContext = (AsyncResponse) activityContext;
    }


    public void addUser(User userToAdd) {

        if (userToAdd.isManager()) {
            String script = "/addManager.php";
            String url = urlBase + script;
            Log.i(TAG, "addUser as manager url: " + url);

            String json = gson.toJson(userToAdd, User.class);

            Log.i(TAG, "addUser as manager json request: " + json);

            new AsyncPost(activityContext).execute(url, json);
            // go to registration activity for result
        } else {

            String script = "/addUser.php";
            String url = urlBase + script;
            Log.i(TAG, "addUser as player url: " + url);

            String json = gson.toJson(userToAdd, User.class);

            Log.i(TAG, "addUser as player json request: " + json);

            new AsyncPost(activityContext).execute(url, json);
            // go to registration activity for result
        }
    }
}
