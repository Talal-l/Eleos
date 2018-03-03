package com.c50x.eleos.controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.c50x.eleos.data.Team;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.AbstractMap;
import java.util.Map;

public class TeamTask {
    private Team teamList[];
    private String urlBase;
    private Gson gson;


    public TeamTask() {
        // load the server address from string.xml
        gson = new Gson();
    }



    // add team to remote database
    public void addTeam(Team teamToAdd) {

        String script = "/addTeam.php";

        // convert into a JSON object
        String json = gson.toJson(teamToAdd);
        Log.i("TeamTask_addTeam", "json to be sent: " + json);

        // construct the url
        String url = urlBase + script;
        Log.i("TeamTask_addTeam", "using server address: " + urlBase);
        Log.i("TeamTask_addTeam", "url: " + url);

        new AsyncPost().execute(url,json);
    }

     public void loadTeam (String teamName){

     }






}
