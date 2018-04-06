package com.c50x.eleos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.c50x.eleos.R;
import com.c50x.eleos.controllers.AsyncResponse;
import com.c50x.eleos.controllers.GameTask;
import com.c50x.eleos.controllers.LoginTask;
import com.c50x.eleos.data.Game;
import com.c50x.eleos.data.Team;
import com.google.gson.Gson;

import java.util.HashMap;

public class GameInfoActivity extends AppCompatActivity implements AsyncResponse {


    private final static String TAG = "GameInfoActivity";
    private TextView tvGameName;
    private TextView tvGameTeam1;
    private TextView tvGameTeam2;
    private TextView tvGameSport;
    private TextView tvGameDate;
    private TextView tvGameTime;
    private TextView tvGameVenue;
    private TextView tvGameAdmin;
    private Gson gson;
    private com.c50x.eleos.data.Game selectedGame;
    private Menu mnuGameInfo;
    private HashMap<Game, com.c50x.eleos.data.Game> modelObjectMap;
    private MenuItem menuButton;
    private Team selectedTeam;
    private GameTask gameTask;
    private String gameJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //hides keyboard upon switching to this Activity
        setContentView(R.layout.game_info);


        // init members
        gson = new Gson();
        gameTask = new GameTask(GameInfoActivity.this);

        gameJson = getIntent().getStringExtra("gameId");
        Log.i(TAG, "selected game Json: " + gameJson);


        // get game object from json
        selectedGame = gson.fromJson(gameJson, com.c50x.eleos.data.Game.class);


        // find views

        tvGameName = findViewById(R.id.tv_game_name_info);
        tvGameTeam1 = findViewById(R.id.tv_game_team1_info);
        tvGameTeam2 = findViewById(R.id.tv_game_team2_info);
        tvGameDate = findViewById(R.id.tv_game_date_info);
        tvGameTime = findViewById(R.id.tv_game_time_info);
        tvGameSport = findViewById(R.id.tv_game_game_sport_info);
        tvGameVenue = findViewById(R.id.tv_game_location_info);
        tvGameAdmin = findViewById(R.id.tv_game_admin_info);


        tvGameName.setText(selectedGame.getGameName());
        tvGameAdmin.setText(selectedGame.getGameAdmin());
        tvGameTeam1.setText(selectedGame.getTeam1());
        tvGameTeam2.setText(selectedGame.getTeam2());
        tvGameDate.setText(selectedGame.getStartDate());
        tvGameTime.setText(selectedGame.getStartTime());
        tvGameSport.setText(selectedGame.getSport());
        tvGameVenue.setText(selectedGame.getVenueAddress());


        // display back button in action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    // add menu actions to toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_actionbar, menu);
        mnuGameInfo = menu;

        menuButton = mnuGameInfo.findItem(R.id.mnut_done);

        if (LoginTask.currentAuthUser.getHandle().equals(selectedGame.getGameAdmin())) {
            menuButton.setTitle("Edit");

        } else {

            menuButton.setTitle("Join");
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnut_done: // TODO: Change id
                // select done option

                if (item.getTitle().equals("Join")) {
                    // go to team selection and get selected team for the current player
                    Intent intent = new Intent(GameInfoActivity.this, TeamSelectionActivity.class);
                    intent.putExtra("source", 0);
                    startActivityForResult(intent, 1);

                } else if (item.getTitle().equals("Edit")) { // go to GameCreationActivity for edits
                    Intent intent = new Intent(this, CreateGameActivity.class);
                    intent.putExtra("selectedGame", gameJson);
                    Log.i(TAG, "Json to send to activity: " + gameJson);
                    // TODO: startActivity for result and give it code 2 and handle returned info
                    startActivity(intent);
                    finish();
                }

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }


    // receive and handle result from team selection activity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) { // coming from selecting a team for the admin that wants to join
            if (resultCode == RESULT_OK) {
                // main team meaning the team of the current player
                String selectedTeamJson = data.getStringExtra("mainTeam");

                selectedTeam = gson.fromJson(selectedTeamJson, Team.class);

                // TODO: Add error handling?
                gameTask.addTeamToGame(selectedTeam.getTeamName(), selectedGame.getGameId());

                Log.i(TAG, "selected team2 " + selectedTeam);
                Toast.makeText(this, "team " + selectedTeam + " has been added to game", Toast.LENGTH_LONG).show();
                finish();

                tvGameTeam2.setText(selectedTeam.getTeamName());
            }
        } else if (requestCode == 2) { // update info after edit

        }
    }

    @Override
    public void taskFinished(String output) {
        Log.i(TAG, "output for updating team2: " + output);

    }
}
