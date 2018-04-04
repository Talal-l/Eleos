package com.c50x.eleos.controllers;

import android.content.Context;
import android.util.Log;

import com.c50x.eleos.R;
import com.c50x.eleos.data.Game;
import com.c50x.eleos.data.Request;
import com.google.gson.Gson;

public class GameTask {
    private static final String TAG = "GameTask";
    private String urlBase;
    private Gson gson;
    private AsyncResponse activityContext;

    public GameTask(Context activityContext) {
        // load the server address from string.xml
        gson = new Gson();
        this.activityContext = (AsyncResponse) activityContext;
        urlBase = activityContext.getString(R.string.server_address);

    }

    // add game to remote database
    public void addGame(Game gameToAdd) {

        String script = "/addGame.php";

        // convert into a JSON object
        String json = gson.toJson(gameToAdd);
        Log.i(TAG, "addGame json request: " + json);

        // construct the url
        String url = urlBase + script;
        Log.i(TAG, "addGame url: " + url);


        new AsyncPost(activityContext).execute(url, json);
    }

    public void updateGame(Game game) {

        String script = "/updateGame.php";

        String url = urlBase + script;
        String json = gson.toJson(game, Game.class);

        Log.i(TAG, "Json request : " + json);

        Log.i(TAG, "updateGame url: " + url);
        new AsyncPost(activityContext).execute(url, json);

    }


    // TODO: Load games based on parameters
    public void loadGames() {

        String script = "/loadGames.php";
        String key = "gameName";

        String url = urlBase + script;
        Log.i(TAG, "loadGame url: " + url);


        new AsyncGet(activityContext).execute(url);

    }

    public void addTeamToGame(final String teamName, int gameId) {

        String script = "/addTeamToGame.php";

        String url = urlBase + script;
        Log.i(TAG, "addTeamToGame url: " + url);


        Par p = new Par();
        p.gameId = gameId;
        p.team2 = teamName;

        String json = gson.toJson(p, Par.class);

        Log.i(TAG, "addTeamToGame json request: " + json);

        new AsyncPost(activityContext).execute(url, json);
    }

    public void sendGameInvite(Game game) {

        String script = "/newGameRequest.php";

        String url = urlBase + script;

        Log.i(TAG, "game invite url: " + url);

        String sender = game.getGameAdmin();
        String teamName1 = game.getTeam1();
        String teamName2 = game.getTeam2();
        int gameId = game.getGameId();
        int state = Request.PENDING;

        // will be added in the server side
        String receiver = null;

        Request gameInvite = new Request(gameId,teamName1,teamName2,sender, receiver, state);

        String json = gson.toJson(gameInvite, Request.class);

        Log.i(TAG, "Game invite request to be sent : " + json);

        new AsyncPost(activityContext).execute(url, json);


    }

    public void updateTeamInviteState(int id, int state) {

        String script = "/updateRequest.php";

        String url = urlBase + script;

        Log.i(TAG, "update team invite url: " + url);

        Request teamInviteResponse = new Request();
        teamInviteResponse.setState(state);
        teamInviteResponse.setRequestId(id);


        String json = gson.toJson(teamInviteResponse, Request.class);

        Log.i(TAG, "Team invite update response json:  " + json);

        new AsyncPost(activityContext).execute(url, json);
    }
    public class Par {
        public String team2;
        public int gameId;

        public Par() {

        }
    }
}
