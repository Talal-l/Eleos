package com.c50x.eleos.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.c50x.eleos.R;
import com.c50x.eleos.controllers.AsyncResponse;
import com.c50x.eleos.controllers.GameTask;
import com.c50x.eleos.controllers.LoginTask;
import com.c50x.eleos.data.Game;
import com.c50x.eleos.data.Team;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.gson.Gson;

import java.util.Calendar;

import static com.c50x.eleos.data.Request.PENDING;
import static com.c50x.eleos.data.Request.WAITING;

public class CreateGameActivity extends AppCompatActivity implements AsyncResponse {
    private static final String TAG = "CreateGameActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private Spinner spnGameSport;
    private EditText etGameName;
    private TextView tvMainTeam;
    private TextView tvGameChallengeTeam;
    private TextView tvGameLocation;
    private TextView tvGameDate;
    private TextView tvGameTime;
    private Button btnCancelCreateGame;
    private Button btnConfirmCreateGame;
    private Game newGame;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TimePickerDialog.OnTimeSetListener timeSetListener;
    private String mainTeam;
    private String mainTeamJson;
    private Team mainTeamObject;
    private String challengeTeamJson;
    private String challengeTeam;
    private Team challengeTeamObject;
    private String gameTime;
    private String gameDate;
    private String selectedGameJson;
    private Game selectedGame;
    private Menu mnuGameCreation;
    private MenuItem mnutSave;
    private Gson gson;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        setTitle("Create Game");

        if (isServicesOK())
            init();

        newGame = new Game();
        mainTeam = null;
        challengeTeam = null;
        gameDate = null;
        gameTime = null;
        gson = new Gson();


        // initializing the views
        etGameName = findViewById(R.id.et_game_name);
        spnGameSport = findViewById(R.id.spn_game_sport);
        tvGameDate = findViewById(R.id.tv_game_date);
        tvGameTime = findViewById(R.id.tv_game_time);
        tvMainTeam = findViewById(R.id.tv_main_team);
        tvGameLocation = findViewById(R.id.tv_game_location);
        tvGameChallengeTeam = findViewById(R.id.tv_game_challenged_team);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // get game if any
        selectedGameJson = getIntent().getStringExtra("selectedGame");


        if (selectedGameJson != null) { // we have a game to edit, get info from it
            // parse json
            selectedGame = gson.fromJson(selectedGameJson, Game.class);


            // save team2, this will be replaced if a new team is selected
            challengeTeam = selectedGame.getTeam2();


            // set text with info from game object

            etGameName.setText(selectedGame.getGameName());
            tvGameDate.setText(selectedGame.getStartDate());
            gameDate = selectedGame.getStartDate();
            tvGameTime.setText(selectedGame.getStartTime());
            gameTime = selectedGame.getStartTime();
            if (!selectedGame.getTeam1().isEmpty()) {
                tvMainTeam.setText(selectedGame.getTeam1());
                mainTeam = selectedGame.getTeam1();
            }
            if (!selectedGame.getTeam2().isEmpty()) {
                tvGameChallengeTeam.setText(selectedGame.getTeam2());
                challengeTeam = selectedGame.getTeam2();
            }

            setTitle("Edit Game");
        }

        // get the date from user
        tvGameDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(CreateGameActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateSetListener, year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.i(TAG, "onDateSet: date_tv: " + year + "/" + month + "/" + dayOfMonth);

                gameDate = "" + year + "-" + month + "-" + dayOfMonth;
                tvGameDate.setText(gameDate);

            }
        };


        // get time from user
        tvGameTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar time2 = Calendar.getInstance();
                final int hour = time2.get(Calendar.HOUR);
                int minute = time2.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog;
                timePickerDialog = new TimePickerDialog(CreateGameActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        tvGameTime.setText(hourOfDay + ":" + minute);

                        gameTime = hourOfDay + ":" + minute;
                    }
                }, hour, minute, false);
                timePickerDialog.show();
            }
        });


        // go to team selection screen when user selects to add their team
        tvMainTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateGameActivity.this, TeamSelectionActivity.class);
                intent.putExtra("source", 0);
                startActivityForResult(intent, 1);
            }
        });

        // go to team selection screen to display all available teams
        tvGameChallengeTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateGameActivity.this, TeamSelectionActivity.class);
                intent.putExtra("source", 1);
                startActivityForResult(intent, 2);
            }
        });
    }


    // receive and handle result from team selection activity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) { // coming from selecting the main team
            if (resultCode == RESULT_OK) {

                mainTeamJson = data.getStringExtra("mainTeam");
                mainTeamObject = gson.fromJson(mainTeamJson, Team.class);
                mainTeam = mainTeamObject.getTeamName();

                tvMainTeam.setText(mainTeam);

            }
        } else if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                challengeTeamJson = data.getStringExtra("challengeTeam");
                challengeTeamObject = gson.fromJson(challengeTeamJson, Team.class);
                challengeTeam = challengeTeamObject.getTeamName();

                tvGameChallengeTeam.setText(challengeTeam);

            }
        }
    }


    // add menu actions to toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_actionbar, menu);
        mnuGameCreation = menu;
        mnutSave = mnuGameCreation.findItem(R.id.mnut_done);
        mnutSave.setTitle("Save");

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
            case R.id.mnut_done:
                // save option

                boolean allValid = true;

                if (mainTeam == null) {

                    tvMainTeam.requestFocus();
                    tvMainTeam.setError("Select a team!");
                    allValid = false;
                }
                if (gameDate == null) {
                    tvGameDate.requestFocus();
                    tvGameDate.setError("Select game date!");
                    allValid = false;
                }
                if (gameTime == null) {
                    tvGameTime.requestFocus();
                    tvGameTime.setError("Select game time");
                    allValid = false;
                }
                // TODO: Check if location is valid
                else if (allValid) {

                    // everything is valid, start setting the game object
                    newGame.setGameName(etGameName.getText().toString());
                    Log.i(TAG, "getting current user and setting them as admin");
                    newGame.setGameAdmin(LoginTask.currentAuthUser.getHandle());
                    newGame.setSport(spnGameSport.getSelectedItem().toString());
                    newGame.setTeam1(mainTeam);
                    newGame.setStartDate(gameDate);
                    newGame.setStartTime(gameTime);
                    if (selectedGame != null) // id can't be set here if it is a new game
                        newGame.setGameId(selectedGame.getGameId());
                    if (newGame.getTeam2() == null || newGame.getTeam2().isEmpty())

                        Log.i(TAG, "mainTeam: " + mainTeam + "challengedTeam: " + challengeTeam);
                    if (challengeTeam != null) {
                        newGame.setTeam2(challengeTeam);
                        newGame.setState(PENDING);
                    } else {
                        // TODO: Show message telling the user that the game will be waiting for challenger
                        newGame.setState(WAITING);
                    }
                    // TODO: Add venue
                    //newGame.setVenueAddress();

                    GameTask gameTask = new GameTask(CreateGameActivity.this);

                    if (selectedGameJson != null) {
                        // edit existing game
                        gameTask.updateGame(newGame);


                    } else {


                        // save new game to db
                        gameTask.addGame(newGame);
                    }
                    // send game request to challenged team
                    (new GameTask(CreateGameActivity.this)).sendGameInvite(newGame);
                }

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    private void init() {
        Button btnmap = (Button) findViewById(R.id.btn_game_location);
        btnmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateGameActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean isServicesOK() {
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(CreateGameActivity.this);

        if (available == ConnectionResult.SUCCESS) // check if user can make app requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");

        else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Log.d(TAG, "isServicesOK: an error curred but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(CreateGameActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        return false;
    }
    // validation methods

    public boolean gameNameIaValid(String gn) {
        if (gn.isEmpty() || !(gn.length() >= 4 && gn.length() <= 20))
            return false;

        else
            return true;
    }


    public boolean gameTypeIaVslid(String gt) {
        if (gt.isEmpty())
            return false;

        else
            return true;
    }


    public boolean numberOfPlayersIsValid(String np) {
        String[] players = np.split(" ");

        if (players.length == 0 || players.length % 2 != 0)
            return false;

        else
            return true;
    }


    public boolean locationIsValid(String np) {
        if (np.isEmpty())
            return false;

        else
            return true;

    }

    // handle response from server
    @Override
    public void taskFinished(String output) {
        // save/check if input valid

        Log.i(TAG, "output: " + output);
        LoginTask loginTask = new LoginTask(this);
        if (!output.contains("null") && !output.contains("error")) {

            if (selectedGameJson == null)
                Toast.makeText(CreateGameActivity.this, "Game Created ", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(CreateGameActivity.this, "Game Updated", Toast.LENGTH_SHORT).show();


            Intent intent = new Intent(CreateGameActivity.this, MainActivity.class);

            // prevent back button from coming back to this screen
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            finish();

            startActivity(intent);
        } else if (output.contains("error")) {
            // another game is happening at same place and time
            tvGameDate.requestFocus();
            tvGameDate.setError("Another game is taking place");
            Log.i(TAG, "error: " + output);


        } else {

            // something is wrong
            Log.i(TAG, "Something is wrong with server response " + "\n" +
                    "response from server: " + output);
        }
    }


}
