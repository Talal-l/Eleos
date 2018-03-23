package com.c50x.eleos.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //hides keyboard upon switching to this Activity
        setContentView(R.layout.activity_venue_info);
        if(isServicesOK())
            init();


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
                    Utilities.enableEditText(etVenueName,etVenueManager,etNumberOfGrounds);


                }
                else{
                    // validate new info

                }
                break;




            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void init()
    {
        Button btnmap = (Button) findViewById(R.id.btn_game_location);
        btnmap.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(VenueInfoActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
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


    public boolean isServicesOK()
    {
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(VenueInfoActivity.this);

        if(available == ConnectionResult.SUCCESS) // check if user can make app requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");

        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available))
        {
            Log.d(TAG, "isServicesOK: an error curred but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(VenueInfoActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }

        else
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void taskFinished(String output) {

    }
}
