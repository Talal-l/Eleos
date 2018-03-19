package com.c50x.eleos.controllers;

import android.content.Context;
import android.util.Log;

import com.c50x.eleos.R;
import com.c50x.eleos.data.User;
import com.google.gson.Gson;

public class RegisterTask {

    private  AsyncResponse activityContext;
    private User newUser;
    private Gson gson;
    private Context context;
    private String urlBase;

    public final static Integer USER_WITH_EMAIL_EXIST = 1;
    public final static Integer USER_WITH_HANDLE_EXIST = 2;
    public final static Integer USER_WITH_HANDLE_AND_EMAIL_EXIST = 3;


    public RegisterTask (Context activityContext){
       gson = new Gson();
       context = activityContext;
        urlBase = activityContext.getString(R.string.server_address);
        this.activityContext = (AsyncResponse)activityContext;
    }


    public void addUser (User userToAdd){

        if (userToAdd.isManager()){
            String script = "/addManager.php";
            String url = urlBase + script;
            Log.i("RegisterTask_addUser","url: " + url);

            String json = gson.toJson(userToAdd,User.class);

            Log.i("RegisterTask_addUser","jsonToSend: " + json);

            new AsyncPost(activityContext).execute(url,json);
            // go to registration activity for result
        }
        else {

            String script = "/addUser.php";
            String url = urlBase + script;
            Log.i("RegisterTask_addUser", "url: " + url);

            String json = gson.toJson(userToAdd, User.class);

            Log.i("RegisterTask_addUser", "jsonToSend: " + json);

            new AsyncPost(activityContext).execute(url, json);
            // go to registration activity for result
        }
    }
}
