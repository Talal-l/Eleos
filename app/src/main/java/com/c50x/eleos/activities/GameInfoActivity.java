package com.c50x.eleos.activities;

import android.arch.persistence.room.PrimaryKey;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
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
import com.c50x.eleos.utilities.Utilities;
import com.google.gson.Gson;

public class GameInfoActivity extends AppCompatActivity implements AsyncResponse
{


    private TextView tvGameName;
    private TextView tvGameTeam1;
    private TextView tvGameTeam2;
    private TextView tvGameSport;
    private TextView tvGameDate;
    private TextView tvGameTime;
    private TextView tvGameVenue;
    private TextView tvGameAdmin;

    private Gson gson;
    private Game selectedGame;
    private Menu mnuGameInfo;
    private MenuItem mnutJoin;
    private MenuItem mnutEdit;
    private MenuItem mnutDone;
    private String selectedTeam;
    private GameTask gameTask;
    private String gameJson;
    private int SelectedGameId;

    private final static String TAG = "GameInfoActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //hides keyboard upon switching to this Activity
        setContentView(R.layout.game_info);

        gson = new Gson();
        gameTask = new GameTask(GameInfoActivity.this);

        gameJson = getIntent().getStringExtra("gameId");
        Log.i(TAG,"selected game Json: " + gameJson);


        // get game object from json
        selectedGame = gson.fromJson(gameJson,Game.class);

        // set correct id
        selectedGame.setGameId(getIntent().getIntExtra("id",-1));

        // pass the gameId
        getIntent().putExtra("gameId",selectedGame.getGameId());


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




        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }



    // add menu actions to toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_actionbar, menu);
        mnuGameInfo = menu;

        mnutDone = mnuGameInfo.findItem(R.id.mnut_done);

        if (LoginTask.currentAuthUser.getHandle().equals(selectedGame.getGameAdmin())){
            mnutDone.setTitle("Edit");

        }
        else{

            mnutDone.setTitle("Join");
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnut_done:
                // select done option

                if (item.getTitle().equals("Join")){

                // go to team selection and get selected team if any
                Intent intent = new Intent(GameInfoActivity.this,TeamSelectionActivity.class);
                intent.putExtra("source",0);
                startActivityForResult(intent,1);
                }
                else if (item.getTitle().equals("Edit")){
                    Intent intent = new Intent(this,CreateGameActivity.class);
                    intent.putExtra("selectedGame", gameJson);
                    Log.i(TAG,"Json to send to activity: " + gameJson);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == 1){ // coming from selecting the main team
            if(resultCode == RESULT_OK){
                // main team meaning the a team of the current player
                selectedTeam = data.getStringExtra("mainTeam");

                // TODO: Add error handling?
                gameTask.addTeamToGame(selectedTeam,selectedGame.getGameId());

                Log.i(TAG,"selected team2 " + selectedTeam);
                Toast.makeText(this,"team " + selectedTeam + " has been added to game",Toast.LENGTH_LONG).show();
                finish();



                tvGameTeam2.setText(selectedTeam);
            }
        }
    }

    @Override
    public void taskFinished(String output) {
        Log.i(TAG,"output for updating team2: " + output);

    }
}
