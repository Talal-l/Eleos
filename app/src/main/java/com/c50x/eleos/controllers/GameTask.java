package com.c50x.eleos.controllers;

import android.content.Context;
import android.util.Log;

import com.c50x.eleos.R;
import com.c50x.eleos.data.Game;
import com.c50x.eleos.data.GameRequest;
import com.c50x.eleos.data.Request;
import com.c50x.eleos.data.Team;
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

        int state = game.getState();

        // will be added in the server side
        String receiver = null;

        GameRequest gameInvite = new GameRequest(game, receiver, state);

        String json = gson.toJson(gameInvite, GameRequest.class);

        Log.i(TAG, "Game invite request to be sent : " + json);

        new AsyncPost(activityContext).execute(url, json);


    }

    public void updateGameInviteState(int id, int state) {

        String script = "/updateGameInvite.php";

        String url = urlBase + script;

        Log.i(TAG, "update game invite url: " + url);

        Request gameInviteResponse = new Request();
        gameInviteResponse.setState(state);
        gameInviteResponse.setRequestId(id);


        String json = gson.toJson(gameInviteResponse, Request.class);

        Log.i(TAG, "Game invite update response json:  " + json);

        new AsyncPost(activityContext).execute(url, json);
    }

    public void sendJoinRequest(Team teamToAdd, Game gameToJoin) {

        String script = "/newGameRequest.php";

        String url = urlBase + script;

        Log.i(TAG, "join game request url: " + url);

        int state = gameToJoin.getState();

        GameRequest gameInvite = new GameRequest(gameToJoin, teamToAdd, state);

        String json = gson.toJson(gameInvite, GameRequest.class);

        Log.i(TAG, "Join game request to be sent : " + json);

        new AsyncPost(activityContext).execute(url, json);

    }

    public class Par {
        public String team2;
        public int gameId;

        public Par() {

        }
    }
}
