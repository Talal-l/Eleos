package com.c50x.eleos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.c50x.eleos.R;
import com.c50x.eleos.controllers.AsyncResponse;
import com.c50x.eleos.controllers.TeamTask;
import com.c50x.eleos.data.Team;

public class CreateTeamActivity extends AppCompatActivity implements AsyncResponse
{
    private Team newTeam;
    private Button confirmButton;
    private Button cancelButton;
    private EditText teamSport;
    private EditText teamName;
    private EditText teamAdmin;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //hides keyboard upon switching to this Activity

        setContentView(R.layout.activity_create_team);

        // test addTeam

//        newTeam.setTeamName("first");
//        newTeam.setSport("football");
//        newTeam.setTeamAdmin("ph1");

        teamName = findViewById(R.id.team_name);
        teamSport = findViewById(R.id.team_sport);
        teamAdmin = findViewById(R.id.team_admin);

        confirmButton = findViewById(R.id.button_confirm);
        cancelButton = findViewById(R.id.button_cancel);




        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!teamAdminValid(teamName.getText().toString())) {
                    teamName.setError("Team name is INVALID!");
                } else if (!teamSportValid(teamSport.getText().toString())) {
                    teamSport.setError("Team sport is INVALID!");
                } else {
                    newTeam.setSport(teamSport.getText().toString());
                    newTeam.setTeamAdmin(teamAdmin.getText().toString());
                    newTeam.setTeamName(teamName.getText().toString());

                }
                // when button is pressed try to save team
                TeamTask task = new TeamTask(CreateTeamActivity.this);
                // call async task that will send info to server
                task.addTeam(newTeam);
            }

        });

        // Cancel button takes you back to Login Page

        cancelButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }

    // code to handle received response from server
    @Override
    public void taskFinished(String output){
        Log.i("CreateTeamActivity", "json response: " + output);
    }



    public boolean teamNameValid(String name){
        if(name.length()>0 && name.length()<=20)
            return true;
        else
            return false;
    }

    public boolean teamSportValid(String sport){
        if(sport.equals("Football") || sport.equals("football") || sport.equals("Basketball") || sport.equals("basketball"))
            return true;
        else
            return false;
    }

    public boolean teamAdminValid(String admin){
        if(admin.length()>0 && admin.length()<=20)
            return true;
        else
            return false;
    }


}
