package com.c50x.eleos.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.content.Intent;
import android.util.Log;
import android.view.View;
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

import java.util.Calendar;

public class CreateGameActivity extends AppCompatActivity implements AsyncResponse {
    private static final String TAG = "CreateGameActivity";

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
    private String challengeTeam;
    private String gameTime;
    private String gameDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        newGame = new Game();

        mainTeam = null;
        challengeTeam = null;
        gameDate = null;
        gameTime = null;

        // initializing the views
        etGameName = findViewById(R.id.et_game_name);
        spnGameSport = findViewById(R.id.spn_game_sport);
        tvGameDate = findViewById(R.id.tv_game_date);
        tvGameTime = findViewById(R.id.tv_game_time);
        etGameName = findViewById(R.id.et_game_name);
        spnGameSport = findViewById(R.id.spn_game_sport);
        tvMainTeam = findViewById(R.id.tv_main_team);
        tvGameLocation = findViewById(R.id.tv_game_location);
        tvGameChallengeTeam = findViewById(R.id.tv_game_challenged_team);
        btnCancelCreateGame = findViewById(R.id.btn_cancel_create_game);
        btnConfirmCreateGame = findViewById(R.id.btn_confirm_create_game);




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

                        gameTime = hourOfDay +":" + minute;
                    }
                }, hour, minute, false);
                timePickerDialog.show();
            }
        });


        // go to team selection screen when user selects to add their team
        tvMainTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateGameActivity.this,TeamSelectionActivity.class);
                intent.putExtra("source",0);
                startActivityForResult(intent,1);
            }
        });

        // go to team selection screen to display all available teams
        tvGameChallengeTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateGameActivity.this,TeamSelectionActivity.class);
                intent.putExtra("source",1);
                startActivityForResult(intent,2);
            }
        });


        btnCancelCreateGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btnConfirmCreateGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean allValid = true;
                if (!(gameNameIaValid(etGameName.getText().toString()))) {
                    etGameName.setError("Empty or Incorrect Length (Between 4 and 20 characters)");
                    allValid = false;
                }


                if (mainTeam == null){

                    tvMainTeam.requestFocus();
                    tvMainTeam.setError("Select a team!");
                    allValid = false;
                }
                if (gameDate == null){
                    tvGameDate.requestFocus();
                    tvGameDate.setError("Select game date!");
                    allValid = false;
                }
                if (gameTime == null){
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

                    Log.i(TAG,"mainTeam: " + mainTeam + "challengedTeam: " + challengeTeam);
                    if (challengeTeam != null){
                        newGame.setTeam2(challengeTeam);
                    }
                    else {
                        // TODO: Show message telling the user that the game will be waiting for challenger
                    }
                    // TODO: Add venue
                    //newGame.setVenueAddress();

                    GameTask gameTask = new GameTask(CreateGameActivity.this);
                    gameTask.addGame(newGame);
                }
            }
        });
    }


    // receive and handle result from team selection activity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == 1){ // coming from selecting the main team
            if(resultCode == RESULT_OK){
                mainTeam = data.getStringExtra("mainTeam");
                tvMainTeam.setText("Your Team: "+ mainTeam);

            }
        }
        else if(requestCode == 2){
            if (resultCode == RESULT_OK){
                challengeTeam = data.getStringExtra("challengeTeam");
                tvGameChallengeTeam.setText("Challenged Team: " + challengeTeam);

            }
        }
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
        String []players = np.split(" ");

        if ( players.length == 0 ||players.length %2 != 0)
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

            Toast.makeText(CreateGameActivity.this, "Game Created ",Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(CreateGameActivity.this, MainActivity.class);

            // prevent back button from coming back to this screen
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            finish();

            startActivity(intent);
        } else if (output.contains("error")) {
            // another game is happening at same place an date_tv/time_btn
            tvGameDate.requestFocus();
            tvGameDate.setError("Another game is happening");
            tvMainTeam.setError("Another game is happening");
            Log.i(TAG, "error: " + output);


        } else {

            // something is wrong
            Log.i(TAG, "Something is wrong with server response " + "\n" +
                    "response from server: " + output);
        }
    }
}
