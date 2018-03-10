package com.c50x.eleos.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;

import com.c50x.eleos.R;
import com.c50x.eleos.adapters.RvPlayerAdapter;
import com.c50x.eleos.controllers.AsyncResponse;
import com.c50x.eleos.controllers.UserTask;
import com.c50x.eleos.data.User;
import com.c50x.eleos.models.RvPlayerModel;
import com.google.gson.Gson;

import android.widget.Button;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.support.v7.widget.SearchView;
import android.app.SearchManager;
import android.widget.EditText;
import android.graphics.Color;
import android.text.InputFilter;
import android.text.Spanned;


public class PlayerSearchActivity extends AppCompatActivity implements AsyncResponse{

    private RecyclerView recyclerView;

    // @BindView(R.id.recycler_view)
    // RecyclerView recyclerView;

    //@BindView(R.id.toolbar)
    //Toolbar toolbar;
    private Toolbar toolbar;

    private static final String TAG = "PlayerSearchActivity";
    private RvPlayerAdapter mAdapter;

    private ArrayList<RvPlayerModel> modelList = new ArrayList<>();
    private UserTask userTask;
    private ArrayList<String> playersToAdd;


    private Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_search);

        userTask = new UserTask(PlayerSearchActivity.this);
        playersToAdd = new ArrayList<>();

        confirmButton = findViewById(R.id.player_search_confirm);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // if selected return with result
                if (playersToAdd.size() > 0){
                    Intent intent = new Intent();
                    intent.putExtra("players",playersToAdd);
                    setResult(RESULT_OK,intent);
                    finish();

                }
                else {
                    Toast.makeText(PlayerSearchActivity.this,
                            "no player selected, press cancel to go back", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // ButterKnife.bind(this);
        findViews();
        initToolbar("player search");
        setAdapter();

    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    }

    public void initToolbar(String title) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_search, menu);


        // Retrieve the SearchView and plug it into SearchManager
        //final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        final SearchView searchView = (SearchView) (menu.findItem(R.id.action_search).getActionView());

        SearchManager searchManager = (SearchManager) this.getSystemService(this.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));

        //changing edittext color
        EditText searchEdit = ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text));
        searchEdit.setTextColor(Color.WHITE);
        searchEdit.setHintTextColor(Color.WHITE);
        searchEdit.setBackgroundColor(Color.TRANSPARENT);
        searchEdit.setHint("Search");

        InputFilter[] fArray = new InputFilter[2];
        fArray[0] = new InputFilter.LengthFilter(40);
        fArray[1] = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                for (int i = start; i < end; i++) {

                    if (!Character.isLetterOrDigit(source.charAt(i)))
                        return "";
                }

                return null;


            }
        };
        searchEdit.setFilters(fArray);
        View v = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
        v.setBackgroundColor(Color.TRANSPARENT);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                Log.i(TAG,"Query to submit: " + s);
                userTask.searchPlayer(s);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
               return false;
            }
        });


        return true;
    }


    private void setAdapter() {



        mAdapter = new RvPlayerAdapter(PlayerSearchActivity.this, modelList);

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);


        recyclerView.setAdapter(mAdapter);


        mAdapter.SetOnItemClickListener(new RvPlayerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, RvPlayerModel model) {

                //handle item click events here
                Toast.makeText(PlayerSearchActivity.this, "Hey " + model.getTitle(), Toast.LENGTH_SHORT).show();


            }
        });


        mAdapter.SetOnCheckedListener(new RvPlayerAdapter.OnCheckedListener() {
            @Override
            public void onChecked(View view, boolean isChecked, int position, RvPlayerModel model) {

                Toast.makeText(PlayerSearchActivity.this, (isChecked ? "Checked " : "Unchecked ") + model.getTitle(), Toast.LENGTH_SHORT).show();

                if (isChecked) {
                    playersToAdd.add(model.getTitle());
                    Log.i(TAG,"add to array: " + Arrays.toString(playersToAdd.toArray()));
                }

                else{

                    playersToAdd.remove(model.getTitle());
                    Log.i(TAG,"remove from array: " + Arrays.toString(playersToAdd.toArray()));
                }






            }
        });


    }


    @Override
    public void taskFinished(String output) {
        Gson gson = new Gson();
        Log.i(TAG,"output: " + output);

            if (output.contains("handle")){

            User[] matchingUsers = gson.fromJson(output,User[].class);

            modelList = new ArrayList<>();
            for (int i = 0; i < matchingUsers.length; i++){
                Log.i(TAG, "Match: " + matchingUsers[i].getHandle());
                modelList.add(new RvPlayerModel(matchingUsers[i]));
            }
            mAdapter.updateList(modelList);
        }

    }
}
