package com.c50x.eleos.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.c50x.eleos.R;
import com.c50x.eleos.controllers.AsyncResponse;
import com.c50x.eleos.controllers.LoginTask;
import com.c50x.eleos.controllers.UserTask;
import com.c50x.eleos.utilities.InputValidation;
import com.c50x.eleos.utilities.Utilities;
import com.c50x.eleos.data.User;
import com.c50x.eleos.data.Venue;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.gson.Gson;

public class VenueInfoActivity extends AppCompatActivity implements AsyncResponse
{


    private TextView tvVenueName;
    private EditText etVenueName;
    private TextView tvVenueLocation;
    private TextView tvVenueType;
    private TextView tvVenueManager;
    private EditText etVenueManager;
    private TextView tvOpeningTime;
    private EditText etOpeningTime;
    private TextView tvNumberOfGrounds;
    private EditText etNumberOfGrounds;
    private Menu mnuVenueInfo;
    private MenuItem mnutEdit;
    private MenuItem mnutDone;

    private Gson gson;
    private Venue venue;
    private String managerEmail;
    private User currentAuthUser;
    private final static String TAG = "GameInfoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //hides keyboard upon switching to this Activity
        setContentView(R.layout.activity_venue_info);


        // init variables
        currentAuthUser = LoginTask.currentAuthUser;


        // find views

        tvVenueName = findViewById(R.id.tv_info_venue_name);
        etVenueName = findViewById(R.id.et_info_venue_name);
        tvVenueManager = findViewById(R.id.tv_info_venue_manager);
        etVenueManager = findViewById(R.id.et_info_venue_manager);
        tvVenueLocation = findViewById(R.id.tv_info_venue_location);
        tvVenueType = findViewById(R.id.tv_info_venue_type);
        tvOpeningTime = findViewById(R.id.tv_info_venue_opening_time);
        tvNumberOfGrounds = findViewById(R.id.tv_info_num_grounds);
        etNumberOfGrounds = findViewById(R.id.et_info_num_grounds);


        // set to values
        etVenueManager.setText(currentAuthUser.getName());
        etVenueName.setText(currentAuthUser.getVenueName());
        etNumberOfGrounds.setText(String.valueOf(currentAuthUser.getNumGrounds()));


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnut_done: // button pressed

                if (mnutDone.getTitle().equals("Edit")){

                mnutDone.setTitle("Save");
                // enable the EditViews
                    Utilities.enableEditText(etVenueName,etVenueManager);
                    etNumberOfGrounds.setInputType(InputType.TYPE_CLASS_PHONE);


                }
                else{
                    // validate new info
                    if (!InputValidation.venueName(etVenueName.getText().toString())){
                        etVenueName.setError("Invalid venue Name");

                    }
                    else if (!InputValidation.name(etVenueManager.getText().toString())){
                        etVenueManager.setError("Invalid name");

                    }
                    else if (!(etNumberOfGrounds.getText().toString().compareTo("0") > 0)){
                        etNumberOfGrounds.setError("can't be 0");

                    }
                    else{
                        currentAuthUser.setName(etVenueManager.getText().toString());
                        currentAuthUser.setVenueName(etVenueName.getText().toString());
                        currentAuthUser.setNumGrounds(Integer.parseInt(etNumberOfGrounds.getText().toString()));

                        UserTask userTask = new UserTask(this);
                        userTask.updateUser(currentAuthUser);

                        mnutDone.setTitle("Edit");
                        Toast.makeText(this,"Info updated",Toast.LENGTH_LONG).show();
                    }

                }
                break;




            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
        return true;
    }



    // add menu actions to toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_actionbar, menu);
        mnuVenueInfo = menu;
        mnutDone = menu.findItem(R.id.mnut_done);

            mnutDone.setTitle("Edit");
            Utilities.disableEditText(etVenueManager,etVenueName,etNumberOfGrounds);

        return true;
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }




    @Override
    public void taskFinished(String output) {
        Log.i(TAG,"output: " + output);
        if(!output.contains("null") && !output.contains("error")) {

            // go back to viewing mode


        }
        else if (output.contains("error")){
                // Auth failed because of invalid input


                if (output.contains("name")){

                    etVenueManager.setError("invalid name");
                }
                if (output.contains("venueName")){

                    etVenueName.setError("invalid venue name");
                }
        }
        else{
              // something is wrong
                    Log.i("RegActivity","Something is wrong with server response "+ "\n" +
                            "response from server: " + output);
        }

    }
}
