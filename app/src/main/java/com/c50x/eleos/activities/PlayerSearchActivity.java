package com.c50x.eleos.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
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
    private MenuItem mnut_done;
    private Menu actionMenu;


    private static final String TAG = "PlayerSearchActivity";
    private RvPlayerAdapter mAdapter;

    private ArrayList<RvPlayerModel> modelList = new ArrayList<>();
    private UserTask userTask;
    private ArrayList<String> playersToAdd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_search);

        userTask = new UserTask(PlayerSearchActivity.this);
        playersToAdd = new ArrayList<>();



        // ButterKnife.bind(this);
        findViews();

        // get all users when activity starts
        userTask.searchPlayer("");

        initToolbar("player search");
        setAdapter();

    }

    private void findViews() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    }

    public void initToolbar(String title) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_actionbar, menu);
        actionMenu = menu;
        mnut_done = menu.findItem(R.id.mnut_done);

        // hide done when nothing is selected
        mnut_done.setVisible(false);


        // Retrieve the SearchView and plug it into SearchManager
        //final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        final SearchView searchView = (SearchView) (findViewById(R.id.player_actionbar_search));

        SearchManager searchManager = (SearchManager) this.getSystemService(this.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));

        // set the search bar to always on
        searchView.setIconifiedByDefault(false);


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
                ArrayList<RvPlayerModel> filterList = new ArrayList<>();
                if (s.length() > 0) {
                    for (int i = 0; i < modelList.size(); i++) {
                        if (modelList.get(i).getTitle().toLowerCase().contains(s.toString().toLowerCase())) {
                            filterList.add(modelList.get(i));
                            mAdapter.updateList(filterList);
                        }
                        else{
                            filterList.remove(modelList.get(i));
                            mAdapter.updateList(filterList);
                        }
                    }
                } else {
                    mAdapter.updateList(modelList);
                }
                return false;
            }
        });


        return true;
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnut_done:
                // select done option
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

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
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

                    // at least one player is selected so show the done option
                    mnut_done.setVisible(true);
                }

                else{

                    playersToAdd.remove(model.getTitle());
                    Log.i(TAG,"remove from array: " + Arrays.toString(playersToAdd.toArray()));

                    // if not players are selected remove the done option

                    if (playersToAdd.size() == 0)
                        mnut_done.setVisible(false);
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
