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

    private Team newTeam;
    private Button confirmButton;
    private Button cancelButton;
    private EditText teamSport;
    private EditText teamName;
    private EditText teamAdmin;
    private TextView players;
    private ArrayList<String> playersToAdd;


    private static final String TAG = "CreateTeamActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //hides keyboard upon switching to this Activity

        newTeam = new Team();

        setContentView(R.layout.activity_create_team);

        teamName = findViewById(R.id.team_name_input);
        teamSport = findViewById(R.id.team_sport_input);
        players = findViewById(R.id.game_players_input);

        confirmButton = findViewById(R.id.confirm_create_team_button);
        cancelButton = findViewById(R.id.cancel_create_team_button);

        // go to player search
        players.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateTeamActivity.this,PlayerSearchActivity.class);
                startActivityForResult(intent,1);

            }
        });


        newTeam = new Team();


        confirmButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String[] playerList = new String[players.getLineCount()];

                String s = players.getText().toString();
                System.out.print(players.getText().toString());
                Log.i("multLine",players.getText().toString());
                


                if (!teamAdminValid(teamName.getText().toString())) {
                    teamName.setError("Team name is INVALID!");
                } else if (!teamSportValid(teamSport.getText().toString())) {
                    teamSport.setError("Team sport is INVALID!");
                } else {
                    newTeam.setSport(teamSport.getText().toString());
                    newTeam.setTeamAdmin(LoginTask.currentAuthUser.getHandle());
                    newTeam.setTeamName(teamName.getText().toString());

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

        cancelButton.setOnClickListener(new View.OnClickListener() {

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
                playersToAdd = data.getStringArrayListExtra("players");
                Log.i(TAG,"players from search: " + Arrays.toString(playersToAdd.toArray()));
                players.setText(Arrays.toString(playersToAdd.toArray()));

            }
        }

    }
    // code to handle received response from server
    @Override
    public void taskFinished(String output){
        Log.i("CreateTeamActivity", "json response: " + output);
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
            teamName.setError("Team name taken!");
            Log.i(TAG,"error: " + output);


        }
        else{

            // something is wrong
            Log.i(TAG,"Something is wrong with server response "+ "\n" +
                            "response from server: " + output);
        }

    }


}
