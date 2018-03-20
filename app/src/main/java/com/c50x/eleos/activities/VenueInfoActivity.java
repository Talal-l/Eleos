package com.c50x.eleos.activities;

import android.app.Dialog;
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
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.c50x.eleos.R;
import com.c50x.eleos.controllers.AsyncResponse;
import com.c50x.eleos.controllers.GameTask;
import com.c50x.eleos.data.Game;
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
    private MenuItem mnutSave;

    private Gson gson;
    private Venue venue;
    private String managerEmail;

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


        // find views

        tvVenueName = findViewById(R.id.tv_info_venue_name);
        etVenueName = findViewById(R.id.et_info_venue_name);
        tvVenueManager = findViewById(R.id.tv_info_venue_manager);
        etVenueManager = findViewById(R.id.et_info_venue_manager);
        tvVenueLocation = findViewById(R.id.tv_info_venue_location);
        tvVenueType = findViewById(R.id.tv_info_venue_type);
        tvOpeningTime = findViewById(R.id.tv_info_venue_opening_time);
        tvNumberOfGrounds = findViewById(R.id.tv_info_num_grounds);
        tvNumberOfGrounds = findViewById(R.id.et_info_num_grounds);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void init()
    {
        Button btnmap = (Button) findViewById(R.id.btn_game_location);
        btnmap.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.i(TAG,"shit");
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
        // TODO: Show edit option if manger is verified

        // TODO: Hide save if nothing has changed

        return true;
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    // TODO: handle action bar options
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.mnut_done:
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean isServicesOK()
    {
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(VenueInfoActivity.this);

        if(available == ConnectionResult.SUCCESS) // check if user can make app requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");

        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available))
        {
            Log.d(TAG, "isServicesOK: an error accured but we can fix it");
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
