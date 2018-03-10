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

import com.c50x.eleos.R;
import com.c50x.eleos.controllers.AsyncResponse;
import com.c50x.eleos.controllers.GameTask;
import com.c50x.eleos.controllers.LoginTask;
import com.c50x.eleos.data.Game;

import java.util.ArrayList;
import java.util.Calendar;

public class CreateGameActivity extends AppCompatActivity implements AsyncResponse {
    private static final String TAG = "CreateGameActivity";

    private Spinner spnGameSport;

    private EditText etGameName;

    private TextView tvMainTeam;
    private TextView tvGameLocation;
    private TextView tvGameDate;
    private TextView tvGameTime;

    private Button btnCancelCreateGame;
    private Button btnConfirmCreateGame;

    private Game newGame;

    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TimePickerDialog.OnTimeSetListener timeSetListener;
    private String mainTeamToAdd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        newGame = new Game();

        // initializing the views
        etGameName = findViewById(R.id.et_game_name);
        spnGameSport = findViewById(R.id.spn_game_sport);
        tvGameDate = findViewById(R.id.tv_game_date);
        tvGameTime = findViewById(R.id.tv_game_time);
        etGameName = findViewById(R.id.et_game_name);
        spnGameSport = findViewById(R.id.spn_game_sport);
        tvMainTeam = findViewById(R.id.tv_main_team);
        tvGameLocation = findViewById(R.id.tv_game_location);
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

                String date2 = month + "/" + dayOfMonth + "/" + year;
                tvGameDate.setText(date2);

                newGame.setStartDate(date2);
            }
        };


        // get time from user
        tvGameTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar time2 = Calendar.getInstance();
                int hour = time2.get(Calendar.HOUR);
                int minute = time2.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog;
                timePickerDialog = new TimePickerDialog(CreateGameActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        tvGameTime.setText(hourOfDay + ":" + minute);
                        newGame.setStartTime(hourOfDay + ":" + minute);
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
                startActivityForResult(intent,1);
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
                if (!(gameNameIaValid(etGameName.getText().toString())))
                    etGameName.setError("Empty or Incorrect Length (Between 4 and 20 characters)");

                // TODO: Check if location is valid
                else {
                    newGame.setGameName(etGameName.getText().toString());

                    Log.i(TAG, "getting current user and setting them as admin");
                    newGame.setGameAdmin(LoginTask.currentAuthUser.getHandle());
                    newGame.setSport(spnGameSport.getSelectedItem().toString());

                    // TODO: Save game to database
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
                // TODO: Get team name and display it in tvMainTeam and prepare it to be saved to db
                String mainTeam = data.getStringExtra("mainTeam");
                tvMainTeam.setText("Your Team: "+ mainTeam);

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

            // TODO: Show message that game was created

            Intent intent = new Intent(CreateGameActivity.this, MainActivity.class);

            // prevent back button from coming back to this screen
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            finish();

            startActivity(intent);
        } else if (output.contains("error")) {
            // another game is happening at same place an date_tv/time_btn
            Log.i(TAG, "error: " + output);


        } else {

            // something is wrong
            Log.i(TAG, "Something is wrong with server response " + "\n" +
                    "response from server: " + output);
        }
    }
}
