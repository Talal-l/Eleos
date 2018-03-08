package com.c50x.eleos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.c50x.eleos.R;
import com.c50x.eleos.controllers.AsyncResponse;
import com.c50x.eleos.controllers.LoginTask;
import com.c50x.eleos.controllers.TeamTask;
import com.c50x.eleos.data.Team;

import java.util.ArrayList;
import java.util.Arrays;

public class CreateTeamActivity extends AppCompatActivity implements AsyncResponse {

    private Button confirm_btn;
    private Button cancel_btn;
    private EditText team_sport_et;
    private EditText team_name_et;
    private EditText team_admin;
    private TextView players_tv;
    private Team newTeam;
    private ArrayList<String> playersToAdd;


    private static final String TAG = "CreateTeamActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //hides keyboard upon switching to this Activity

        setContentView(R.layout.activity_create_team);

        newTeam = new Team();

        // initializing the views
        team_name_et = findViewById(R.id.team_name_input);
        team_sport_et = findViewById(R.id.team_sport_input);
        players_tv = findViewById(R.id.game_players_input);
        confirm_btn = findViewById(R.id.confirm_create_team_button);
        cancel_btn = findViewById(R.id.cancel_create_team_button);


        // go to player search
        players_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateTeamActivity.this,PlayerSearchActivity.class);
                startActivityForResult(intent,1);

            }
        });


        confirm_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String[] playerList = new String[players_tv.getLineCount()];

                String s = players_tv.getText().toString();

                if (!teamAdminValid(team_name_et.getText().toString())) {
                    team_name_et.setError("Team name is INVALID!");
                } else if (!teamSportValid(team_sport_et.getText().toString())) {
                    team_sport_et.setError("Team sport is INVALID!");
                } else {
                    newTeam.setSport(team_sport_et.getText().toString());
                    newTeam.setTeamAdmin(LoginTask.currentAuthUser.getHandle());
                    newTeam.setTeamName(team_name_et.getText().toString());

                    playersToAdd.add(LoginTask.currentAuthUser.getHandle());
                    newTeam.setTeamPlayers(playersToAdd.toArray(new String[0]));

                }
                // when button is pressed try to save team
                TeamTask task = new TeamTask(CreateTeamActivity.this);
                // call async task that will send info to server
                task.addTeam(newTeam);
            }

        });

        // Cancel button takes you back to Login Page

        cancel_btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

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
    // get player search result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                playersToAdd = data.getStringArrayListExtra("players_tv");
                Log.i(TAG,"players_tv from search: " + Arrays.toString(playersToAdd.toArray()));
                players_tv.setText(Arrays.toString(playersToAdd.toArray()));

            }
        }

    }

    // code to handle received response from server
    @Override
    public void taskFinished(String output){
        Log.i(TAG, "json response: " + output);
        // save/check if input valid

        Log.i(TAG,"output: " + output);
        LoginTask loginTask = new LoginTask(this);
        if(!output.contains("null") && !output.contains("error")) {

            // TODO: SHow message that game was created

            Intent intent = new Intent(CreateTeamActivity.this, MainActivity.class);

            // prevent back button from coming back to this screen
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            finish();

            startActivity(intent);
        }
        else if (output.contains("error")){
            team_name_et.setError("Team name taken!");
            Log.i(TAG,"error: " + output);


        }
        else{

            // something is wrong
            Log.i(TAG,"Something is wrong with server response "+ "\n" +
                            "response from server: " + output);
        }

    }
}
