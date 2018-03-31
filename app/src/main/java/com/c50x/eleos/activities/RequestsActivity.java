package com.c50x.eleos.activities;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.c50x.eleos.R;
import com.c50x.eleos.adapters.RvRequestAdapter;
import com.c50x.eleos.controllers.AsyncResponse;
import com.c50x.eleos.data.Request;
import com.c50x.eleos.data.Team;
import com.c50x.eleos.models.RvRequestModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class RequestsActivity extends AppCompatActivity implements AsyncResponse {


    private static final String TAG = "RequestsActivity";

    private RvRequestAdapter mAdapter;
    private RecyclerView recyclerView;
    private ArrayList<RvRequestModel> modelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        modelList = new ArrayList<>();

        // show back button in action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // setup the views and adapters
        findViews();
        setAdapter();

        // test for request display

        Team testTeam = new Team();
        testTeam.setTeamName("testTeam");
        testTeam.setSport("football");
        testTeam.setTeamAdmin("testTeamAdmin");

        modelList.add(new RvRequestModel(testTeam));
        mAdapter.updateList(modelList);

    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


    private void findViews() {
        recyclerView = (RecyclerView) findViewById(R.id.rv_requests_list);
    }


    private void setAdapter() {

        mAdapter = new RvRequestAdapter(RequestsActivity.this, modelList);

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(RequestsActivity.this, R.drawable.divider_recyclerview));
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setAdapter(mAdapter);


        mAdapter.SetOnItemClickListener(new RvRequestAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, RvRequestModel model) {

            }
        });


    }


    @Override
    public void taskFinished(String output) {
        Gson gson = new Gson();

        // convert
        ArrayList<Request> requests = gson.fromJson(output, new TypeToken<ArrayList<Request>>() {
        }.getType());

        // convert Team to TeamModel so it can be displayed
        for (Request team : requests) {

        }

        //update list in adapter
        mAdapter.updateList(modelList);

    }


}
