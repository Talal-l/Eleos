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
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.c50x.eleos.R;
import com.c50x.eleos.data.AppDatabase;
import com.c50x.eleos.data.User;

import java.text.DateFormat;
import java.util.Calendar;

public class CreateGameActivity extends AppCompatActivity
{
    private static final String TAG = "CreateGameActivity";
    private EditText game_name;
    private EditText game_type;
    private EditText number_of_players;
    private EditText location;
    private TextView date;
    private Button time;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        date = (TextView) findViewById(R.id.game_date_input);
        time = (Button) findViewById(R.id.game_time_input);
        game_name = (EditText) findViewById(R.id.game_name_input);
        game_type = (EditText) findViewById(R.id.game_type_input);
        number_of_players = (EditText) findViewById(R.id.number_of_players_input);
        location = (EditText) findViewById(R.id.location_input);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(CreateGameActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth , mDateSetListener , year , month , day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
            {
                month = month + 1;
                Log.d(TAG, "onDateSet: date: " + year + "/" + month + "/" + dayOfMonth);

                String date2 = month + "/" + dayOfMonth + "/" +year;
                date.setText(date2);
            }
        };

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Calendar time2 = Calendar.getInstance();
                int hour = time2.get(Calendar.HOUR);
                int minute = time2.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog;
                timePickerDialog = new TimePickerDialog(CreateGameActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                    {
                        time.setText(hourOfDay + ":" + minute);
                    }
                }, hour,minute,false);
                timePickerDialog.show();

            }
        });

        configureCancelButton();
        configureConfirmButton();
    }



    private void configureCancelButton()
    {
        Button cancel_button = (Button) findViewById(R.id.Cancel_create_game_button);
        cancel_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }

    private void configureConfirmButton()
    {
        Button confirm_button = (Button) findViewById(R.id.confirm_create_game_button);
        confirm_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                if( !(gameNameIsValid(game_name.getText().toString())) )
                    game_name.setError("Empty or Incorrect Length (Between 4 and 20 characters)");

                else if( !(gameTypeIaVslid(game_type.getText().toString())) )
                    game_type.setError("Empty or did not enter 'Football'");

                else if( !(numberOfPlayersIsValid(number_of_players.getText().toString())) )
                    number_of_players.setError("Empty or players not between 11 - 15");

                else if( !(locationIsValid(location.getText().toString())) )
                    location.setError("Empty field");

                else
                {
                    Intent intent = new Intent(v.getContext(),MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public boolean gameNameIsValid(String gn)
    {
        if(gn.isEmpty() || !(gn.length() >= 4 && gn.length() <= 20))
            return false;

            else
                return true;
    }
    public boolean gameTypeIaVslid(String gt)
    {
        if(gt.isEmpty() )
            return false;

        else
            return true;
    }

    public boolean numberOfPlayersIsValid(String np)
    {
        if(np.isEmpty() || !(np.length() >= 11) && !(np.length() <= 15) )
            return false;

        else
            return true;
    }

    public boolean locationIsValid(String np)
    {
        if(np.isEmpty())
            return false;

        else
            return true;
    }
}
