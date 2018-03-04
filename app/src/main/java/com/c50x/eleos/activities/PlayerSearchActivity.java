package com.c50x.eleos.activities;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.c50x.eleos.R;
import com.c50x.eleos.controllers.AsyncResponse;
import com.c50x.eleos.controllers.TeamTask;
import com.c50x.eleos.data.Team;

public class PlayerSearchActivity extends AppCompatActivity implements AsyncResponse {

    private TeamTask task;
    private Team newTeam;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_playersearch);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //hides keyboard upon switching to this Activity




    }


    @Override
    public void taskFinished(String output) {

    }
}
