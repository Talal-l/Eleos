package com.c50x.eleos.controllers;

import android.content.Context;

import com.c50x.eleos.R;
import com.c50x.eleos.data.User;
import com.google.gson.Gson;

public class LoginTask {
    private  AsyncResponse activityContext;
    private User userList[];
    private Gson gson;
    private String urlBase;


    public static Auth currentAuthUser;

    private class LoginInfo{
       String email;
       String password;
    }

    public LoginTask(Context activityContext){
       currentAuthUser = new Auth();
       gson = new Gson();
        urlBase = activityContext.getString(R.string.server_address);
        this.activityContext = (AsyncResponse)activityContext;
    }

    public void authUsingToken (String token){

    }
    public void authUsingEmail(String email, String password){

        String script = "/authUsingEmail.php";
        String url = urlBase + script;

        LoginInfo loginInfo = new LoginInfo();
        loginInfo.email = email;
        loginInfo.password = password;

        String json = gson.toJson(loginInfo,LoginInfo.class);

        new AsyncPost(activityContext).execute(url,json);

        // go to login activity for result
        // check if valid and set the token in this class using Token method
    }

    public void setToken(String token, User user, Boolean in){
        currentAuthUser.setAuth(token,user,in);
    }



}
