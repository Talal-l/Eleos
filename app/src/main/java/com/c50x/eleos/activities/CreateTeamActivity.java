package com.c50x.eleos.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.c50x.eleos.R;
import com.c50x.eleos.controllers.AsyncResponse;
import com.c50x.eleos.controllers.TeamTask;
import com.c50x.eleos.data.Team;

public class CreateTeamActivity extends AppCompatActivity implements AsyncResponse
{
    private Team newTeam;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //hides keyboard upon switching to this Activity

        setContentView(R.layout.activity_create_team);

        // test addTeam

        newTeam = new Team();
        newTeam.setTeamName("first");
        newTeam.setSport("football");
        newTeam.setTeamAdmin("ph1");



        // when button is pressed try to save team
        TeamTask task = new TeamTask(this);
        // call async task that will send info to server
        task.addTeam(newTeam);



    }

    // code to handle received response from server
    @Override
    public void taskFinished(String output){
        Log.i("CreateTeamActivity", "json response: " + output);
    }


}
