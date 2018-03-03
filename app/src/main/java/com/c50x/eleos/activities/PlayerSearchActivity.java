package com.c50x.eleos.activities;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.c50x.eleos.R;
import com.c50x.eleos.controllers.AsyncResponse;
import com.c50x.eleos.controllers.TeamTask;
import com.c50x.eleos.controllers.UserTask;
import com.c50x.eleos.data.Team;

public class PlayerSearchActivity extends AppCompatActivity implements AsyncResponse{
    private TeamTask task;
    private Team newTeam;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playersearch);

    }


    @Override
    public void taskFinished(String output) {
    }
}
