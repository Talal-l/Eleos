package com.c50x.eleos.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.c50x.eleos.R;
import com.c50x.eleos.data.User;
import com.google.gson.Gson;

import java.util.HashMap;

public class LoginTask {
    private static final String TAG = "LoginTask";
    public static User currentAuthUser = new User();
    private AsyncResponse activityContext;
    private Context context;
    private Gson gson;
    private String urlBase;

    public LoginTask(Context activityContext) {
        gson = new Gson();
        context = activityContext;
        urlBase = activityContext.getString(R.string.server_address);
        this.activityContext = (AsyncResponse) activityContext;
    }

    public void authUsingToken(String token) {
        String script = "/authUsingToken.php";
        String url = urlBase + script;

        HashMap<String, String> keyValue = new HashMap<>();
        keyValue.put("token", token);
        String json = gson.toJson(keyValue);

        Log.i(TAG, "authToken json request: " + json);

        new AsyncPost(activityContext).execute(url, json);


    }

    public void authUsingEmail(String email, String password) {

        String script = "/authUsingEmail.php";
        String url = urlBase + script;

        LoginInfo loginInfo = new LoginInfo();
        loginInfo.email = email;
        loginInfo.password = password;

        String json = gson.toJson(loginInfo, LoginInfo.class);

        Log.i(TAG, " authUsingEmail json request: " + json);

        new AsyncPost(activityContext).execute(url, json);

        // go to login activity for result
    }

    // used with login and registration
    public void setToken(String json) {
        Log.i(TAG, "setting current user using " + json);
        currentAuthUser = gson.fromJson(json, User.class);

        // Save new token in shared preferences
        SharedPreferences pref = context.getSharedPreferences("token_file", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("token", currentAuthUser.getToken());
        editor.commit();

    }

    public void clearToken() {
        currentAuthUser = new User();
        SharedPreferences pref = context.getSharedPreferences("token_file", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

    private class LoginInfo {
        String email;
        String password;
    }
}
