package com.c50x.eleos.controllers;

import com.c50x.eleos.data.User;

public class Auth {
    private String currentUserToken;
    private User authUser;
    private Boolean logedIn;


    public Auth(){
        currentUserToken = "";
        authUser = null;
        logedIn = false;
    }
    public String getToken(){
        return this.currentUserToken;
    }

    public User getAuthUser(){
        return this.authUser;
    }

    public Boolean userLogedIn(){
        return this.logedIn;
    }
    public void clearAuth(){
        currentUserToken = "";
        authUser = null;
        logedIn = false;
    }
    public void setAuth(String token, User user, Boolean in){
       currentUserToken = token;
       authUser = user;
       logedIn = in;
    }
}

