package com.c50x.eleos.controllers;

import android.content.Context;
import android.util.Log;

import com.c50x.eleos.R;
import com.c50x.eleos.data.User;
import com.google.gson.Gson;

public class UserTask {
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
        Log.i(TAG, " loaduser url: " + url);

        new AsyncGet(activityContext).execute(url,key, handle);

     }

     public void searchPlayer (String handle){

        String script = "/searchPlayer.php";
        String key = "handle";

        String url = urlBase + script;
        Log.i(TAG, "searchPlayer url: " + url);

        new AsyncGet(activityContext).execute(url,key, handle);
     }
     public void updateUser (User userToAdd){

        String script = "/updateUser.php";

        String url = urlBase + script;

        String json = gson.toJson(userToAdd, User.class);

        Log.i(TAG, "addUser as manager json request: " + json);

        new AsyncPost(activityContext).execute(url, json);

     }
}
