package com.c50x.eleos.controllers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.c50x.eleos.R;
import com.c50x.eleos.data.Team;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.AbstractMap;
import java.util.Map;

public class TeamTask {
    private Team teamList[];
    private String urlBase;
    private Gson gson;
    private AsyncResponse activityContext;


    public TeamTask(Context activityContext) {
        // load the server address from string.xml
        gson = new Gson();
        this.activityContext = (AsyncResponse)activityContext;
        urlBase = activityContext.getString(R.string.server_address);

    }


    // add team to remote database
    public void addTeam(Team teamToAdd) {

        String script = "/addTeam.php";

        // convert into a JSON object
        String json = gson.toJson(teamToAdd);
        Log.i("TeamTask_addTeam", "json to be sent: " + json);

        // construct the url
        String url = urlBase + script;
        Log.i("TeamTask_addTeam", "url: " + url);



        new AsyncPost(activityContext).execute(url,json);
    }

     public void loadTeam (String teamName){

        String script = "/loadTeam.php";
        String key = "teamName";

        String url = urlBase + script;
        Log.i("TeamTask_loadTeam", "url: " + url);


        new AsyncGet(activityContext).execute(url,key, teamName);

     }

     public void loadAdminTeams(String teamAdmin){

         String script = "/loadAdminTeams.php";
         String key = "teamAdmin";

         String url = urlBase + script;
         Log.i("TeamTask_loadAdminTeams", "url: " + url);

         new AsyncGet(activityContext).execute(url,key,teamAdmin);
     }

     // json to Team object
    public Team getTeamObject(String json){

         return gson.fromJson(json,Team.class);
    }






}
