package com.c50x.eleos.activities;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.c50x.eleos.R;
import com.c50x.eleos.controllers.UserTask;

public class PlayerSearchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playersearch);


        UserTask search = new UserTask(getApplicationContext(),PlayerSearchActivity.this);
        search.loadAllPlayers();

    }



}
