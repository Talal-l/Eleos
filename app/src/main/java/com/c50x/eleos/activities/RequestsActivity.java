package com.c50x.eleos.activities;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.c50x.eleos.R;
import com.c50x.eleos.adapters.RvRequestAdapter;
import com.c50x.eleos.controllers.AsyncResponse;
import com.c50x.eleos.controllers.LoginTask;
import com.c50x.eleos.controllers.TeamTask;
import com.c50x.eleos.controllers.UserTask;
import com.c50x.eleos.data.GameRequest;
import com.c50x.eleos.data.Request;
import com.c50x.eleos.data.TeamRequest;
import com.c50x.eleos.data.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.c50x.eleos.adapters.RuntimeTypeAdapterFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RequestsActivity extends AppCompatActivity implements AsyncResponse {


    private static final String TAG = "RequestsActivity";

    private RvRequestAdapter mAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Request> modelList;
    private User currentUser = LoginTask.currentAuthUser;

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


        // load requests
        UserTask userTask = new UserTask(RequestsActivity.this);
        userTask.loadUserRequests(currentUser.getHandle());


        // handle accept and decline button clicks

        mAdapter.setOnRequestResponseListener(new RvRequestAdapter.OnRequestResponseListener() {

            TeamTask teamTask = new TeamTask(RequestsActivity.this);

            @Override
            public void onRequestAcceptListener(View view, int position, Request model) {

                // TODO: send response to sender
                // TODO: Update request state in db
                teamTask.updateTeamInviteState(model.getRequestId(), Request.ACCEPTED);


                // remove model from list
                modelList.remove(model);
                Toast.makeText(RequestsActivity.this, "Accepted request", Toast.LENGTH_SHORT).show();
                mAdapter.updateList(modelList);

            }

            @Override
            public void onRequestDeclineListener(View view, int position, Request model) {

                teamTask.updateTeamInviteState(model.getRequestId(), Request.DECLINED);


                // remove model from list
                modelList.remove(model);
                Toast.makeText(RequestsActivity.this, "Declined request", Toast.LENGTH_SHORT).show();
                mAdapter.updateList(modelList);


            }
        });
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
            public void onItemClick(View view, int position, Request model) {

            }
        });


    }


    @Override
    public void taskFinished(String output) {


        HashMap<String, String> jsonResponse;
       // jsonResponse = gson.fromJson(output, new TypeToken<Map<String, String>>() {
        //}.getType());



        RuntimeTypeAdapterFactory<Request> runtimeTypeAdapterFactory = RuntimeTypeAdapterFactory
                .of(Request.class)
                .registerSubtype(GameRequest.class)
                .registerSubtype(TeamRequest.class);

        Gson gson = new GsonBuilder().registerTypeAdapterFactory(runtimeTypeAdapterFactory).create();

        Log.i(TAG, "json response: " + output);

        if (output.contains("update")) {

        } else {


            Request requests[] = gson.fromJson(output, Request[].class);

            // assuming the receiver is the current player
            for (Request request : requests) {
                if (request.getState() == Request.PENDING) { // still needs a response

                        modelList.add(request);
                        Log.i(TAG, "Adding to the list: " + request.getRequestId());
                    }
                }
            }

            mAdapter.updateList(modelList);

    }
}
