package com.c50x.eleos.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.c50x.eleos.R;
import com.c50x.eleos.adapters.RvPlayerAdapter;
import com.c50x.eleos.controllers.AsyncResponse;
import com.c50x.eleos.controllers.LoginTask;
import com.c50x.eleos.controllers.TeamTask;
import com.c50x.eleos.data.Team;
import com.c50x.eleos.data.User;
import com.c50x.eleos.data.Venue;
import com.c50x.eleos.models.RvPlayerModel;
import com.c50x.eleos.utilities.InputValidation;
import com.c50x.eleos.utilities.Utilities;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;

public class TeamInfoActivity extends AppCompatActivity implements AsyncResponse {


    private final static String TAG = "TeamInfoActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private TextView tvTeamName;
    private EditText etTeamName;
    private Spinner spnSport;
    private TextView tvSport;
    private TextView tvRating;
    private Team selectedTeam;
    private MenuItem mnutDone;
    private Gson gson;
    private Venue venue;
    private User currentAuthUser;
    private String oldTeamName;
    private TextView tvRemovePlayers;

    private RvPlayerAdapter mAdapter;
    private ArrayList<RvPlayerModel> modelList = new ArrayList<>();
    private ArrayList<String> playersToAdd;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //hides keyboard upon switching to this Activity
        setContentView(R.layout.activity_team_info);


        // init variables

        gson = new Gson();
        String TeamJson = getIntent().getStringExtra("selectedTeam");
        Log.i(TAG, "selected Team Json: " + TeamJson);

        // get game object from json
        selectedTeam = gson.fromJson(TeamJson, Team.class);

        oldTeamName = selectedTeam.getTeamName();

        playersToAdd = new ArrayList<>();

        // find views

        tvTeamName = findViewById(R.id.tv_info_view_team_name);
        etTeamName = findViewById(R.id.et_info_team_name);
        spnSport = findViewById(R.id.spn_info_team_sport);
        tvSport = findViewById(R.id.tv_info_view_team_sport);
        tvRating = findViewById(R.id.tv_info_team_rating);
        tvRemovePlayers = findViewById(R.id.tv_info_team_remove_players);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_team_info);



        // set to values

        tvTeamName.setText(selectedTeam.getTeamName());
        tvSport.setText(selectedTeam.getSport());
//        tvRating.setText(selectedTeam.getTeamRating());



        etTeamName.setText(selectedTeam.getTeamName());

        // set spinner
        String sport = selectedTeam.getSport();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sports, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSport.setAdapter(adapter);
        if (sport != null) {
            int spinnerPosition = adapter.getPosition(sport);
            spnSport.setSelection(spinnerPosition);
        }

        // set player list

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        setAdapter();


        // load players

            String matchingUsers[] = selectedTeam.getTeamPlayers();


            User tempUser = new User();
            modelList = new ArrayList<>();
            for (int i = 0; i < matchingUsers.length; i++){
                Log.i(TAG, "Match: " + matchingUsers[i]);
                tempUser.setHandle(matchingUsers[i]);
                modelList.add(new RvPlayerModel(tempUser));
            }
            mAdapter.updateList(modelList);




        tvRemovePlayers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<String> newPlayerList = new ArrayList<>();
                for (String player: selectedTeam.getTeamPlayers()){
                    if (!playersToAdd.contains(player)){
                        newPlayerList.add(player);
                    }
                }
                selectedTeam.setTeamPlayers(newPlayerList.toArray(new String[0]));
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnut_done: // button pressed

                if (mnutDone.getTitle().equals("Edit")) {

                    mnutDone.setTitle("Save");
                    etTeamName.setVisibility(TextView.VISIBLE);
                    tvTeamName.setVisibility(TextView.GONE);

                    spnSport.setVisibility(View.VISIBLE);
                    tvSport.setVisibility(View.GONE);
                    tvRemovePlayers.setVisibility(View.VISIBLE);


                } else {
                    // validate new info
                    if (!InputValidation.name(etTeamName.getText().toString())) {
                        etTeamName.setError("Invalid venue Name");

                    } else {
                        selectedTeam.setTeamName(etTeamName.getText().toString());
                        selectedTeam.setSport(spnSport.getSelectedItem().toString());


                        TeamTask teamTask = new TeamTask(this);
                        teamTask.updateTeam(selectedTeam,oldTeamName);

                        mnutDone.setTitle("Edit");
                        Toast.makeText(this, "Info updated", Toast.LENGTH_LONG).show();
                        finish();

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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_actionbar, menu);
        mnutDone = menu.findItem(R.id.mnut_done);

        mnutDone.setTitle("Edit");
        Utilities.disableViews(etTeamName);
        spnSport.setClickable(false);

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

   private void setAdapter() {



        mAdapter = new RvPlayerAdapter(TeamInfoActivity.this, modelList);

        recyclerView.setHasFixedSize(false);

        // use a linear layout manager

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);


        recyclerView.setAdapter(mAdapter);


        mAdapter.SetOnItemClickListener(new RvPlayerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, RvPlayerModel model) {

                //handle item click events here
                Toast.makeText(TeamInfoActivity.this, "Hey " + model.getTitle(), Toast.LENGTH_SHORT).show();


            }
        });


        mAdapter.SetOnCheckedListener(new RvPlayerAdapter.OnCheckedListener() {
            @Override
            public void onChecked(View view, boolean isChecked, int position, RvPlayerModel model) {

                Toast.makeText(TeamInfoActivity.this, (isChecked ? "Checked " : "Unchecked ") + model.getTitle(), Toast.LENGTH_SHORT).show();

                if (isChecked) {
                    playersToAdd.add(model.getTitle());
                    Log.i(TAG,"add to array: " + Arrays.toString(playersToAdd.toArray()));

                }

                else{

                    playersToAdd.remove(model.getTitle());
                    Log.i(TAG,"remove from array: " + Arrays.toString(playersToAdd.toArray()));

                    // if no players are selected remove the done option
                }

            }
        });


    }


    @Override
    public void taskFinished(String output) {
        Log.i(TAG, "output: " + output);
        if (!output.contains("null") && !output.contains("error")) {

            // go back to viewing mode


        } else if (output.contains("error")) {
            // Auth failed because of invalid input


            if (output.contains("name")) {

                etTeamName.setError("invalid name");
            }
        } else {
            // something is wrong
            Log.i("RegActivity", "Something is wrong with server response " + "\n" +
                    "response from server: " + output);
        }

    }
}
