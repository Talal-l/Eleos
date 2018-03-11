package com.c50x.eleos.controllers;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.c50x.eleos.R;
import com.c50x.eleos.activities.CreateGameActivity;
import com.c50x.eleos.data.Game;
import com.c50x.eleos.data.Game;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.AbstractMap;
import java.util.Map;

public class GameTask {
    private Game gameList[];
    private String urlBase;
    private Gson gson;
    private AsyncResponse activityContext;

         public class Par{
             public String team2;
             public int gameId;
             public Par(){

             }
         }

    public GameTask(Context activityContext) {
        // load the server address from string.xml
        gson = new Gson();
        this.activityContext = (AsyncResponse)activityContext;
        urlBase = activityContext.getString(R.string.server_address);

    }


    // add game to remote database
    public void addGame(Game gameToAdd) {

        String script = "/addGame.php";

        // convert into a JSON object
        String json = gson.toJson(gameToAdd);
        Log.i("GameTask_addGame", "json to be sent: " + json);

        // construct the url
        String url = urlBase + script;
        Log.i("GameTask_addGame", "using server address: " + urlBase);
        Log.i("GameTask_addGame", "url: " + url);



        new AsyncPost(activityContext).execute(url,json);
    }

    // TODO: Load games based on parameters
     public void loadGames (){

        String script = "/loadGames.php";
        String key = "gameName";

        String url = urlBase + script;
        Log.i("GameTask_loadGame", "using server address: " + urlBase);
        Log.i("GameTask_loadGame", "url: " + url);


        new AsyncGet(activityContext).execute(url);

     }

     public void addTeamToGame(final String teamName, int gameId){

         String script = "/addTeamToGame.php";

         String url = urlBase + script;
         Log.i("GameTask_addTeamToGame", "url: " + url);


         Par p = new Par();
         p.gameId = gameId;
         p.team2 = teamName;

         String json = gson.toJson(p,Par.class);

         Log.i("GameTask_addTeamToGame", "json: " + json);

         new AsyncPost(activityContext).execute(url,json);
     }


     // json to Game objects
    public Game[] toObjects(String json){

        Log.i("GameTasks_toObjects", "json to convert: " + json);
        Game[] gameList = gson.fromJson(json,Game[].class);

         return gameList;
    }
}
