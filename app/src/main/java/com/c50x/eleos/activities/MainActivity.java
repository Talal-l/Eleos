package com.c50x.eleos.activities;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.c50x.eleos.R;
import com.c50x.eleos.adapters.RvGameAdapter;
import com.c50x.eleos.controllers.AsyncResponse;
import com.c50x.eleos.controllers.GameTask;
import com.c50x.eleos.controllers.LoginTask;
import com.c50x.eleos.data.Game;
import com.c50x.eleos.data.User;
import com.c50x.eleos.models.RvGameModel;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AsyncResponse
{
    private Button logOutButton;
    private String handle;
    private String email;
    private TextView handleView;
    private TextView nameView;
    private TextView emailView;
    private GameTask gameTask;
    private User[] l;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private LoginTask loginTask;
    private RecyclerView recyclerView;
    private Game[] loadedGames;

    private Gson gson;
    private static final String TAG = "MainActivity";

    private SwipeRefreshLayout swipeRefreshRecyclerList;
    private RvGameAdapter mAdapter;

    private ArrayList<RvGameModel> modelList = new ArrayList<>();

    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Load token from shared preferences
        SharedPreferences pref = getSharedPreferences("token_file",Context.MODE_PRIVATE);
        String token = pref.getString("token","null");
        Log.i("mainActivity","shared: " + token);

        // check if token exist
        if (token.contains("null")){ // user not logged in
            // go to login activity
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            finish();
            startActivity(intent);
            Log.i("mainActivity","BYE: " + token);
        }

        else{ // token exist

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //hides keyboard upon switching to this Activity
        setContentView(R.layout.activity_main);

        gson = new Gson();

        // needed to access the auth methods
        loginTask = new LoginTask(this);

        if (LoginTask.currentAuthUser != null) { // we have a token but no user is loaded
            // set the global current user using the token
            loginTask.authUsingToken(token);
        }

        gameTask = new GameTask(MainActivity.this);

        gameTask.loadGames();



        // Game list

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshRecyclerList = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_recycler_list);





        mAdapter = new RvGameAdapter(MainActivity.this, modelList);

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);


        recyclerView.setAdapter(mAdapter);


        mAdapter.SetOnItemClickListener(new RvGameAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, RvGameModel model) {

                //handle item click events here
                Toast.makeText(MainActivity.this, "Hey " + model.getTitle(), Toast.LENGTH_SHORT).show();

            }
        });

            mAdapter.SetOnItemClickListener(new RvGameAdapter.OnItemClickListener()
            {
                @Override
                public void onItemClick(View view, int position, RvGameModel model)
                {
                    int selectedGameId = model.getGameid();
                    Log.i(TAG,"Selected Game id: " + selectedGameId);
                    Intent intent = new Intent(MainActivity.this,GameInfoActivity.class);

                    // get game from loadedGames and send it as json to view activity
                    String gameJson = null;
                    for (Game gm: loadedGames){
                        if (gm.getGameId() == selectedGameId){
                            gameJson = gson.toJson(gm);
                            break;
                        }
                    }
                    intent.putExtra("gameId",gameJson);
                    startActivity(intent);

                }
            });

        swipeRefreshRecyclerList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                // Do your stuff on refresh
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (swipeRefreshRecyclerList.isRefreshing())
                            swipeRefreshRecyclerList.setRefreshing(false);

                        gameTask.loadGames();

                    }
                }, 100);

            }
        });



        // for navigation menu
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open , R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
       // fragmentTransaction = getSupportFragmentManager().beginTransaction();
        mToggle.syncState();


        // switch to selected activity when selected from menu

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        // create intent to use to switch to other activities
                        Intent intent;
                        // set item as selected to persist highlight
                        item.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();
                        switch (item.getItemId()){
                            case R.id.nav_menu_gameCreation:
                                intent = new Intent(MainActivity.this, CreateGameActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.nav_menu_teamCreation:
                                intent = new Intent(MainActivity.this, CreateTeamActivity.class);
                                startActivity(intent);
                                break;

                            case R.id.nav_menu_logout:
                                loginTask.clearToken();
                                intent = new Intent(MainActivity.this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                finish();
                                startActivity(intent);
                        }

                        return false;
                    }
                }
        );

        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
        }

    // for navigation menu button
   @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(mToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void taskFinished(String output) {
        // Info associated with token is ready
        Log.i("mainActivity_taskF", "output: " + output);

        Gson gson = new Gson();
        if (!output.contains("null") && !output.contains("game")) { // user is valid
            // load data from json to current user
            LoginTask.currentAuthUser = gson.fromJson(output,User.class);
            Log.i("mainActivity_taskF", "current user handle: " + LoginTask.currentAuthUser.getHandle());

        }
        if (output.contains("game")){

            loadedGames = gson.fromJson(output,Game[].class);

            modelList = new ArrayList<>();
            for (int i = 0; i < loadedGames.length; i++){
                modelList.add(new RvGameModel(loadedGames[i]));
            }
            Log.i("MainActivity","adding to game list");
            mAdapter.updateList(modelList);
        }
    }
}



