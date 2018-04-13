package com.c50x.eleos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.c50x.eleos.R;
import com.c50x.eleos.adapters.RvTeamAdapter;
import com.c50x.eleos.controllers.AsyncResponse;
import com.c50x.eleos.controllers.LoginTask;
import com.c50x.eleos.controllers.TeamTask;
import com.c50x.eleos.data.Team;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;


public class TeamSelectionActivity extends AppCompatActivity implements AsyncResponse {

    private static final String TAG = "TeamSelectionActivity";

    // @BindView(R.id.recycler_view)
    // RecyclerView recyclerView;
    String selectedTeamJson;
    private RecyclerView recyclerView;
    private RvTeamAdapter mAdapter;
    private Gson gson;
    private ArrayList<Team> modelList = new ArrayList<>();
    private TeamTask teamTask;
    private LoginTask loginTask;
    private Team selectedTeam;
    private int sourceOfRequest; // main team or challenged team option

    private Menu mnu_team_select;
    private MenuItem mnut_done;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_team);

        gson = new Gson();

        // setup the views and adapters
        findViews();
        setAdapter();
        teamTask = new TeamTask(TeamSelectionActivity.this);

        // is it a search for the admin teams or other teams?
        sourceOfRequest = getIntent().getIntExtra("source", 0);

        // load current players teams
        if (sourceOfRequest == 0)
            teamTask.loadAdminTeams(LoginTask.currentAuthUser.getHandle());
            // load all teams
        else if (sourceOfRequest == 1)
            teamTask.loadAllTeams();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    // add menu actions to toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_actionbar, menu);
        mnu_team_select = menu;
        mnut_done = mnu_team_select.findItem(R.id.mnut_done);

        // hide done when nothing is selected
        mnut_done.setVisible(false);

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        mAdapter.resetSelection();
        finish();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnut_done:
                // select done option

                selectedTeamJson = gson.toJson(selectedTeam);
                if (sourceOfRequest == 0) { // coming from select main team
                    Intent intent = new Intent();


                    // send object to calling activity


                    intent.putExtra("mainTeam", selectedTeamJson);
                    setResult(RESULT_OK, intent);
                    finish();
                } else if (sourceOfRequest == 1) {
                    Intent intent = new Intent();
                    // send object to calling activity
                    intent.putExtra("challengeTeam", selectedTeamJson);
                    setResult(RESULT_OK, intent);
                    finish();
                }


                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }


    private void findViews() {
        recyclerView = (RecyclerView) findViewById(R.id.rv_team_list);
    }


    private void setAdapter() {

        mAdapter = new RvTeamAdapter(TeamSelectionActivity.this, modelList);

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(TeamSelectionActivity.this, R.drawable.divider_recyclerview));
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setAdapter(mAdapter);


        mAdapter.SetOnItemClickListener(new RvTeamAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Team model) {

                //handle item click events here

                // show done action when an item is selected
                if (mAdapter.getCurrentSelectionPosition() > -1) {
                    mnut_done.setVisible(true);
                    // set the selected team
                    selectedTeam = modelList.get(mAdapter.getCurrentSelectionPosition());

                    Log.i(TAG, "selected team: " + selectedTeam.getTeamName());
                } else {
                    mnut_done.setVisible(false);
                    // unset the selected team
                    selectedTeam = null;
                }

                //Log.i("teamActivity","selected team: " + selectedTeam);

                Toast.makeText(TeamSelectionActivity.this, "Hey " + model.getTeamName(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    public void taskFinished(String output) {
        Gson gson = new Gson();

        // convert from json string
        ArrayList<Team> adminTeams = gson.fromJson(output, new TypeToken<ArrayList<Team>>() {
        }.getType());

        modelList.addAll(adminTeams);

        //update list in adapter
        mAdapter.updateList(modelList);

    }
}
