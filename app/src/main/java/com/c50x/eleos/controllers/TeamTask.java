package com.c50x.eleos.controllers;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.c50x.eleos.data.AppDatabase;
import com.c50x.eleos.data.Team;

public class TeamTask{
    private AppDatabase db;
    private Context context;
    private Team teamList[];


    public TeamTask(Context appContext, Context activityContext){
        this.context = activityContext;
        db = AppDatabase.getDatabaseInstance(appContext);
    }

    private class addTeam extends AsyncTask<Team,Void,Long> {

        @Override
        protected Long doInBackground(Team... par) {

            Team teamToAdd = par[0];


            // Check if the user email and password match with the database
            Long state;

            // TODO: What is the return on abort?
            state = db.teamDao().addTeam(teamToAdd);

        return state;
    }

        protected void onPostExecute(Long rowNum){

            if (rowNum >= 0) { // TODO: Check if this is correct
                Toast.makeText(context, "Team created successfully !", Toast.LENGTH_LONG).show(); // prints on the screen that log in was successful

                // TODO: Show created team Info without the fields being editable
            }
            else {
                // TODO: Show error that name exist
                // load view from activity context and display message

            }
        }
    }

    public void addTeam(Team team){

        new addTeam().execute(team);

    }
}
