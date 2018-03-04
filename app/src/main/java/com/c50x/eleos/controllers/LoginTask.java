package com.c50x.eleos.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.c50x.eleos.R;
import com.c50x.eleos.data.User;
import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

public class LoginTask {
    private  AsyncResponse activityContext;
    private Context context;
    private User userList[];
    private Gson gson;
    private String urlBase;


    public static User currentAuthUser = new User();

    private class LoginInfo{
       String email;
       String password;
    }

    public LoginTask(Context activityContext){
       gson = new Gson();
       context = activityContext;
        urlBase = activityContext.getString(R.string.server_address);
        this.activityContext = (AsyncResponse)activityContext;
    }

    public void authUsingToken (String token){

    }
    public void authUsingEmail(String email, String password){

        String script = "/authUsingEmail.php";
        String url = urlBase + script;
        Log.i("LoginTask_authEmail","url: " + url);

        LoginInfo loginInfo = new LoginInfo();
        loginInfo.email = email;
        loginInfo.password = password;

        String json = gson.toJson(loginInfo,LoginInfo.class);

        Log.i("LoginTask_authEmail","jsonToSend: " + json);

        new AsyncPost(activityContext).execute(url,json);

        // go to login activity for result
        // check if valid and set the token in this class using Token method
    }

    public void setToken(String json){
        // json is an auth class
        Log.i("LoginTask_setToken","json: " + json);
        currentAuthUser = gson.fromJson(json,User.class);
        Log.i("LoginTask_setToken", "token: " + currentAuthUser.getToken());

        // Save new token in shared preferences
        SharedPreferences pref = context.getSharedPreferences("token_file",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("token",currentAuthUser.getToken());
        editor.commit();


    }



}
