package com.c50x.eleos.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.c50x.eleos.R;
import com.c50x.eleos.controllers.AsyncResponse;
import com.c50x.eleos.controllers.LoginTask;
import com.c50x.eleos.data.User;
import com.google.gson.Gson;

public class LoadingActivity extends AppCompatActivity implements AsyncResponse {

    private static final String TAG = "LoadingActivity";
    private Gson gson;
    private LoginTask loginTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        // init variables
        gson = new Gson();
        loginTask = new LoginTask(this);

        // load token from shared preferences
        SharedPreferences pref = getSharedPreferences("token_file", Context.MODE_PRIVATE);
        String token = pref.getString("token", "null");
        Log.i(TAG, "Token from shared preferences: " + token);

        // check if token exist
        if (token.contains("null")) { // user not logged in
            // go to login activity
            Intent intent = new Intent(LoadingActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            finish();
            startActivity(intent);
        } else if (LoginTask.currentAuthUser != null) { // we have a token but no user is loaded
            // set the global current user using the token
            loginTask.authUsingToken(token);
        }
    }

    @Override
    public void taskFinished(String output) {
        if (!output.contains("null") && !output.contains("game")) { // user is valid
            // load data from json to current user
            LoginTask.currentAuthUser = gson.fromJson(output, User.class);
            Log.i("mainActivity_taskF", "current user is manager: " + LoginTask.currentAuthUser.isManager());
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            finish();
            startActivity(intent);
        }
    }
}
