package com.c50x.eleos.controllers;

import android.content.Context;
import android.util.Log;

import com.c50x.eleos.R;
import com.c50x.eleos.data.Request;
import com.c50x.eleos.data.Team;
import com.google.gson.Gson;

import java.util.HashMap;

public class TeamTask {

    private static final String TAG = "TeamTask";
    private String urlBase;
    private Gson gson;
    private AsyncResponse activityContext;


    public TeamTask(Context activityContext) {
        // load the server address from string.xml
        gson = new Gson();
        this.activityContext = (AsyncResponse) activityContext;
        urlBase = activityContext.getString(R.string.server_address);

    }


    // add team to remote database
    public void addTeam(Team teamToAdd) {

        String script = "/addTeam.php";

        // convert into a JSON object
        String json = gson.toJson(teamToAdd);
        Log.i(TAG, "addTeam json request: " + json);

        // construct the url
        String url = urlBase + script;
        Log.i(TAG, "addTeam url: " + url);


        new AsyncPost(activityContext).execute(url, json);
    }

    public void loadTeam(String teamName) {

        String script = "/loadTeam.php";
        String key = "teamName";

        String url = urlBase + script;
        Log.i(TAG, "loadTeam url: " + url);


        new AsyncGet(activityContext).execute(url, key, teamName);

    }

    public void loadAdminTeams(String teamAdmin) {

        String script = "/loadAdminTeams.php";
        String key = "teamAdmin";

        String url = urlBase + script;
        Log.i(TAG, "loadAdminTeams url: " + url);

        new AsyncGet(activityContext).execute(url, key, teamAdmin);
    }

    public void loadAllTeams() {
        // TODO: Change to post

        String script = "/loadAllTeams.php";
        String key = "";

        String url = urlBase + script;
        Log.i(TAG, "loadAllTeams url: " + url);

        new AsyncGet(activityContext).execute(url, key, "");


    }

    public void updateTeam(Team teamToAdd, String oldTeamName) {

        String script = "/updateTeam.php";

        String url = urlBase + script;
        String json = gson.toJson(teamToAdd, Team.class);

        StringBuilder sb = new StringBuilder(json);
        sb.insert(sb.length() - 1, ",\"oldTeamName\":" + "\"" + oldTeamName + "\"");

        json = sb.toString();


        Log.i(TAG, "Json request: " + json);

        new AsyncPost(activityContext).execute(url, json);

    }

    public void removePlayerFromTeam(String player, String team) {

        String script = "/removePlayerFromTeam.php";

        String url = urlBase + script;

        Log.i(TAG, "removePlayerFromTeam url: " + url);

        HashMap<String, String> map = new HashMap<>();
        map.put("playerHandle", player);
        map.put("teamName", team);

        String json = gson.toJson(map);

        Log.i(TAG, "remove player json request: " + json);

        new AsyncPost(activityContext).execute(url, json);

    }

    public void sendTeamInvite(Team team, String receiver) {

        String script = "/newRequest.php";

        String url = urlBase + script;

        Log.i(TAG, "sendTeamInvite url: " + url);

        String sender = team.getTeamAdmin();
        String teamName = team.getTeamName();
        int state = Request.PENDING;

        Request teamInvite = new Request(teamName, sender, receiver, state);

        String json = gson.toJson(teamInvite, Request.class);

        Log.i(TAG, "Invite request to be sent : " + json);

        new AsyncPost(activityContext).execute(url, json);


    }

    // json to Team object
    public Team getTeamObject(String json) {

        return gson.fromJson(json, Team.class);
    }
}
