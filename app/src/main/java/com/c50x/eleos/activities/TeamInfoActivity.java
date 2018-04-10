package com.c50x.eleos.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import com.c50x.eleos.utilities.InputValidation;
import com.c50x.eleos.utilities.Utilities;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

public class TeamInfoActivity extends AppCompatActivity implements AsyncResponse {


    private final static String TAG = "TeamInfoActivity";
    private Team selectedTeam;
    private MenuItem mnutDone;
    private Gson gson;
    private Venue venue;
    private User currentAuthUser;
    private String oldTeamName;
    private TextView tvRemovePlayers;
    private String playerToRemove;

    private TeamTask teamTask;
    private RvPlayerAdapter mAdapter;
    private ArrayList<User> modelList = new ArrayList<>();
    private ArrayList<String> playersToAdd;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //hides keyboard upon switching to this Activity
        setContentView(R.layout.activity_team_info);


        // init variables

        teamTask = new TeamTask(TeamInfoActivity.this);
        currentAuthUser = LoginTask.currentAuthUser;
        gson = new Gson();
        String TeamJson = getIntent().getStringExtra("selectedTeam");
        Log.i(TAG, "selected Team Json: " + TeamJson);

        // get game object from json
        selectedTeam = gson.fromJson(TeamJson, Team.class);

        oldTeamName = selectedTeam.getTeamName();

        playersToAdd = new ArrayList<>();

        // find views


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_team_info);


        // set to values



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        setAdapter();


        // load players

        teamTask.loadTeamMembers(selectedTeam.getTeamName());

        modelList = new ArrayList<>();

        setTitle(selectedTeam.getTeamName());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnut_done: // button pressed

                if (mnutDone.getTitle().equals("Add players")) {

                    mnutDone.setTitle("Save");

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
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void setAdapter() {


        mAdapter = new RvPlayerAdapter(TeamInfoActivity.this, modelList);

        // show player state in current team
        mAdapter.checkState(true);
        recyclerView.setHasFixedSize(false);

        // use a linear layout manager

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);


        recyclerView.setAdapter(mAdapter);


        mAdapter.SetOnItemClickListener(new RvPlayerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, final User model) {

                //handle item click events here

                if (currentAuthUser.getHandle().equals(selectedTeam.getTeamAdmin())) {

                    AlertDialog alertDialog = new AlertDialog.Builder(TeamInfoActivity.this).
                            setItems(R.array.admin_player_options, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    if (i == 0) {
                                    } else if (i == 1) {
                                        Toast.makeText(TeamInfoActivity.this, "Removing " + model.getHandle(), Toast.LENGTH_SHORT).show();
                                        // remove player from team
                                        TeamTask teamTask = new TeamTask(TeamInfoActivity.this);
                                        playerToRemove = model.getHandle();
                                        teamTask.removePlayerFromTeam(playerToRemove, selectedTeam.getTeamName());

                                        User modelToRemove = null;
                                        for (User model : modelList) {
                                            if (model.getHandle().equals(playerToRemove)) {
                                                modelToRemove = model;
                                                break;
                                            }
                                        }

                                        modelList.remove(modelToRemove);
                                        mAdapter.updateList(modelList);
                                    }
                                }
                            })
                            .create();


                    alertDialog.show();

                }

            }
        });


    }


    @Override
    public void taskFinished(String output) {
        Log.i(TAG, "output: " + output);

        if (output.contains("newRequest")){
            //
        }
        else if (output.contains("teamState")){

            // add members with their state
            User members[] = gson.fromJson(output,User[].class);
            modelList.addAll(Arrays.asList(members));

            mAdapter.updateList(modelList);
        }
        else if (!output.contains("null") && !output.contains("error")) {

        } else if (output.contains("error")) {
            // Auth failed because of invalid input


            if (output.contains("name")) {

            }
        } else {
            // something is wrong
            Log.i("RegActivity", "Something is wrong with server response " + "\n" +
                    "response from server: " + output);
        }

    }
}
