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
import android.widget.TextView;
import android.widget.TimePicker;

import com.c50x.eleos.R;
import com.c50x.eleos.controllers.AsyncResponse;
import com.c50x.eleos.controllers.GameTask;
import com.c50x.eleos.controllers.LoginTask;
import com.c50x.eleos.data.Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class CreateGameActivity extends AppCompatActivity implements AsyncResponse {
    private static final String TAG = "CreateGameActivity";
    private EditText game_name_et;
    private EditText game_type_et;
    private TextView game_players_tv;
    private EditText location_et;
    private TextView date_tv;
    private Button time_btn;
    private Button cancel_btn;
    private Button confirm_btn;
    private Game newGame;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TimePickerDialog.OnTimeSetListener timeSetListener;
    private ArrayList<String> playersToAdd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        newGame = new Game();

        // initializing the views
        game_name_et = findViewById(R.id.game_name_input);
        game_type_et = findViewById(R.id.game_type_input);
        date_tv = findViewById(R.id.game_date_input);
        time_btn = findViewById(R.id.game_time_input);
        game_name_et = findViewById(R.id.game_name_input);
        game_type_et = findViewById(R.id.game_type_input);
        game_players_tv = findViewById(R.id.game_players_input);
        location_et = findViewById(R.id.location_input);
        cancel_btn = findViewById(R.id.cancel_create_game_button);
        confirm_btn = (Button) findViewById(R.id.confirm_create_game_button);

        // get the date from user
        date_tv.setOnClickListener(new View.OnClickListener() {
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
                date_tv.setText(date2);

                newGame.setStartDate(date2);
            }
        };

        // get time from user
        time_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar time2 = Calendar.getInstance();
                int hour = time2.get(Calendar.HOUR);
                int minute = time2.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog;
                timePickerDialog = new TimePickerDialog(CreateGameActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        time_btn.setText(hourOfDay + ":" + minute);
                        newGame.setStartTime(hourOfDay + ":" + minute);
                    }
                }, hour, minute, false);
                timePickerDialog.show();

            }
        });

        // go to player search
        game_players_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateGameActivity.this,PlayerSearchActivity.class);
                startActivityForResult(intent,1);

            }
        });



        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(gameNameIaValid(game_name_et.getText().toString())))
                    game_name_et.setError("Empty or Incorrect Length (Between 4 and 20 characters)");

                else if (!(gameTypeIaVslid(game_type_et.getText().toString())))
                    game_type_et.setError("Empty or did not enter 'Football'");

                else if (!(locationIsValid(location_et.getText().toString())))
                    location_et.setError("Empty field");

                else {
                    newGame.setGameName(game_name_et.getText().toString());
                    Log.i(TAG, "getting current user and setting them as admin");
                    newGame.setGameAdmin(LoginTask.currentAuthUser.getHandle());
                    newGame.setSport(game_type_et.getText().toString());

                    // save info in database
                    playersToAdd.add(LoginTask.currentAuthUser.getHandle());
                    newGame.setGamePlayers(playersToAdd.toArray(new String[0]));
                    GameTask gameTask = new GameTask(CreateGameActivity.this);
                    gameTask.addGame(newGame);

                }
            }
        });

    }

    // get player search result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                playersToAdd = data.getStringArrayListExtra("players");
                Log.i(TAG,"players from search: " + Arrays.toString(playersToAdd.toArray()));
                game_players_tv.setText(Arrays.toString(playersToAdd.toArray()));

            }
        }

    }
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

    @Override
    public void taskFinished(String output) {
        // save/check if input valid

        Log.i(TAG, "output: " + output);
        LoginTask loginTask = new LoginTask(this);
        if (!output.contains("null") && !output.contains("error")) {

            // TODO: SHow message that game was created

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
