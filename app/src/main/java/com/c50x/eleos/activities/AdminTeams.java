package com.c50x.eleos.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.c50x.eleos.R;
import com.c50x.eleos.adapters.RvTeamAdapter;
import com.c50x.eleos.controllers.AsyncResponse;
import com.c50x.eleos.controllers.LoginTask;
import com.c50x.eleos.controllers.TeamTask;
import com.c50x.eleos.data.Team;
import com.c50x.eleos.models.RvTeamModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.widget.Toast;


public class AdminTeams extends AppCompatActivity implements AsyncResponse{

    private RecyclerView recyclerView;

    // @BindView(R.id.recycler_view)
    // RecyclerView recyclerView;

    private static final String TAG = "TeamSelectionActivity";

    private RvTeamAdapter mAdapter;
    private ArrayList<Team> adminTeams;
    private ArrayList<RvTeamModel> modelList = new ArrayList<>();
    private TeamTask teamTask;
    private LoginTask loginTask;
    private String selectedTeam;
    private Gson gson;

    private Menu mnu_team_select;
    private MenuItem mnut_done;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_team);

        // setup the views and adapters
        findViews();
        setAdapter();
        teamTask = new TeamTask(this);
        gson = new Gson();


        teamTask.loadAdminTeams(LoginTask.currentAuthUser.getHandle());


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }


    @Override
    public boolean onSupportNavigateUp(){
        mAdapter.resetSelection();
        finish();
        return true;
    }


    private void findViews() {
        recyclerView = (RecyclerView) findViewById(R.id.rv_team_list);
    }


    private void setAdapter() {

        mAdapter = new RvTeamAdapter(this, modelList);

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider_recyclerview));
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setAdapter(mAdapter);


        mAdapter.SetOnItemClickListener(new RvTeamAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, RvTeamModel model) {

                //handle item click events here

                Intent intent = new Intent(AdminTeams.this,TeamInfoActivity.class);

                intent.putExtra("selectedTeam",gson.toJson(adminTeams.get(position)));
                startActivity(intent);
          }
        });


    }


    @Override
    public void taskFinished(String output) {

        // convert
        adminTeams = gson.fromJson(output, new TypeToken<ArrayList<Team>>(){}.getType());

        // convert Team to TeamModel so it can be displayed
        for (Team team: adminTeams){

            RvTeamModel model = new RvTeamModel( "TeamName: " + team.getTeamName(),
                    "players: " +Arrays.toString(team.getTeamPlayers()));

            modelList.add(model);

        }

        //update list in adapter
        mAdapter.updateList(modelList);

    }
}
