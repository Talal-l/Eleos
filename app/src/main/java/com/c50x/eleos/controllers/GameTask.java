package com.c50x.eleos.controllers;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.c50x.eleos.data.AppDatabase;
import com.c50x.eleos.data.Game;
import com.c50x.eleos.data.Team;

public class GameTask{
    private AppDatabase db;
    private Context context;
    private Team gameList[];


    public GameTask(Context appContext, Context activityContext){
        this.context = activityContext;
        db = AppDatabase.getDatabaseInstance(appContext);
    }

    private class addTeam extends AsyncTask<Game,Void,Long> {

        @Override
        protected Long doInBackground(Game... par) {

            Game gameToAdd= par[0];


            // Check if the user email and password match with the database
            Long state;

            // TODO: What is the return on abort?
            state = db.gameDao().addGame(gameToAdd);

        return state;
    }

        protected void onPostExecute(Long rowNum){

            if (rowNum >= 0) { // TODO: Check if this is correct
                Toast.makeText(context, "Team created successfully !", Toast.LENGTH_LONG).show(); // prints on the screen that log in was successful

            }
            else {
                // TODO: Show error that the game conflict with another
                // load view from activity context and display message
            }
        }
    }

    public void addGame(Game game){

        new addTeam().execute(game);

    }
}
