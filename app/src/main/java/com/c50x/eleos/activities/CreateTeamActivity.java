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

    private static final String TAG = "CreateTeamActivity";
    private Button btnConfirmCreateTeam;
    private Button btnCancelCreateTeam;
    private EditText etTeamSport;
    private EditText etTeamName;
    private TextView tvPlayers;
    private Team newTeam;
    private ArrayList<String> playersToAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //hides keyboard upon switching to this Activity

        setContentView(R.layout.activity_create_team);

        newTeam = new Team();

        // initializing the views
        etTeamName = findViewById(R.id.et_team_name);
        etTeamSport = findViewById(R.id.et_team_sport);
        tvPlayers = findViewById(R.id.tv_game_players);
        btnConfirmCreateTeam = findViewById(R.id.btn_confirm_create_team);
        btnCancelCreateTeam = findViewById(R.id.btn_cancel_create_team);


        // go to player search
        tvPlayers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateTeamActivity.this, PlayerSearchActivity.class);
                startActivityForResult(intent, 1);

            }
        });


        btnConfirmCreateTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String[] playerList = new String[tvPlayers.getLineCount()];

                String s = tvPlayers.getText().toString();

                if (!teamAdminValid(etTeamName.getText().toString())) {
                    etTeamName.setError("Team name is INVALID!");
                } else if (!teamSportValid(etTeamSport.getText().toString())) {
                    etTeamSport.setError("Team sport is INVALID!");
                } else {
                    newTeam.setSport(etTeamSport.getText().toString());
                    newTeam.setTeamAdmin(LoginTask.currentAuthUser.getHandle());
                    newTeam.setTeamName(etTeamName.getText().toString());

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

        btnCancelCreateTeam.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                Intent intent = new Intent(CreateTeamActivity.this, MainActivity.class);

                // prevent back button from coming back to this screen
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                finish();
            }
        });

    }


    public boolean teamNameValid(String name) {
        if (name.length() > 0 && name.length() <= 20)
            return true;
        else
            return false;
    }

    public boolean teamSportValid(String sport) {
        if (sport.equals("Football") || sport.equals("football") || sport.equals("Basketball") || sport.equals("basketball"))
            return true;
        else
            return false;
    }

    public boolean teamAdminValid(String admin) {
        if (admin.length() > 0 && admin.length() <= 20)
            return true;
        else
            return false;
    }

    // get player search result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                playersToAdd = data.getStringArrayListExtra("players");
                Log.i(TAG, "players from search: " + Arrays.toString(playersToAdd.toArray()));
                tvPlayers.setText(Arrays.toString(playersToAdd.toArray()));

            }
        }

    }

    // code to handle received response from server
    @Override
    public void taskFinished(String output) {
        Log.i(TAG, "json response: " + output);
        // save/check if input valid

        Log.i(TAG, "output: " + output);
        LoginTask loginTask = new LoginTask(this);
        if (!output.contains("null") && !output.contains("error") && !output.contains("newRequest")) {

            // TODO: Show message that team was created

            // send invitation to added players and update

            TeamTask teamTask = new TeamTask(CreateTeamActivity.this);
            for (String player : playersToAdd) {

                if (!player.equals(LoginTask.currentAuthUser.getHandle())) {
                    teamTask.sendTeamInvite(newTeam, player);
                }
            }


            Intent intent = new Intent(CreateTeamActivity.this, MainActivity.class);

            // prevent back button from coming back to this screen
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            finish();

            startActivity(intent);
        } else if (output.contains("error")) {
            etTeamName.setError("Team name taken!");
            Log.i(TAG, "error: " + output);


        } else {

            // something is wrong
            Log.i(TAG, "Something is wrong with server response " + "\n" +
                    "response from server: " + output);
        }

    }
}
