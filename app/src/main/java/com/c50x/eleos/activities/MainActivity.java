package com.c50x.eleos.activities;

import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.c50x.eleos.R;
import com.c50x.eleos.adapters.RvGameAdapter;
import com.c50x.eleos.controllers.AsyncResponse;
import com.c50x.eleos.controllers.GameTask;
import com.c50x.eleos.controllers.LoginTask;
import com.c50x.eleos.data.Game;
import com.c50x.eleos.models.RvGameModel;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AsyncResponse {

    private static final String TAG = "MainActivity";
    private GameTask gameTask;
    private View menuHeader;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private LoginTask loginTask;
    private RecyclerView recyclerView;
    private TextView tvPlayerHandle;
    private TextView tvUserName;
    private Game[] loadedGames;
    private NavigationView navigationView;
    private Gson gson;
    private SwipeRefreshLayout swipeRefreshRecyclerList;
    private RvGameAdapter mAdapter;
    private ArrayList<RvGameModel> modelList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        if (LoginTask.currentAuthUser != null) { // we have a valid current user

            setContentView(R.layout.activity_main);

            // hides keyboard upon switching to this Activity
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


            // initialize variables
            loginTask = new LoginTask(this);
            gson = new Gson();
            gameTask = new GameTask(MainActivity.this);
            mAdapter = new RvGameAdapter(MainActivity.this, modelList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);


            // find views
            recyclerView = (RecyclerView) findViewById(R.id.game_recycler_view);
            swipeRefreshRecyclerList = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_recycler_list);
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
            navigationView = findViewById(R.id.nav_view);


            // TODO: Load only the games involving the player
            gameTask.loadGames();

            // recycler view setup
            recyclerView.setHasFixedSize(true);
            // use a linear layout manager
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(mAdapter);


            mAdapter.SetOnItemClickListener(new RvGameAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position, RvGameModel model) {

                    int selectedGameId = model.getGameid();
                    Log.i(TAG, "Selected Game id: " + selectedGameId);
                    Intent intent = new Intent(MainActivity.this, GameInfoActivity.class);

                    // get game from loadedGames and send it as json to view activity
                    String gameJson = null;
                    for (Game gm : loadedGames) {
                        if (gm.getGameId() == selectedGameId) {
                            gameJson = gson.toJson(gm);
                            break;
                        }
                    }
                    intent.putExtra("gameId", gameJson);
                    startActivity(intent);
                }
            });

            swipeRefreshRecyclerList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {

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
            mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

            // show menu button and make it clickable
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mDrawerLayout.addDrawerListener(mToggle);
            mToggle.syncState();



            // set nav header
            menuHeader = navigationView.getHeaderView(0);
            tvPlayerHandle = menuHeader.findViewById(R.id.tv_nav_header_player_handle);
            tvUserName = menuHeader.findViewById(R.id.tv_nav_header_name);


            // display info in nav header for manager
            if (LoginTask.currentAuthUser.isManager()) {

                navigationView.getMenu().findItem(R.id.nav_menu_venue_info).setVisible(true);
                navigationView.getMenu().findItem(R.id.nav_menu_gameCreation).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_menu_teamCreation).setVisible(false);
                tvUserName.setText(LoginTask.currentAuthUser.getName());

            }
            else{

                tvPlayerHandle.setText(LoginTask.currentAuthUser.getHandle());
            }


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
                            switch (item.getItemId()) {
                                case R.id.nav_menu_my_teams:
                                    intent = new Intent(MainActivity.this, AdminTeams.class);
                                    startActivity(intent);
                                    break;
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
                                    break;
                                case R.id.nav_menu_venue_info:
                                    intent = new Intent(MainActivity.this, VenueInfoActivity.class);
                                    startActivity(intent);
                                    break;

                            }
                            return false;
                        }
                    }
            );
        }
    }

    // when user comes back from another screen
    @Override
    protected void onResume() {
        super.onResume();
        for (int i = 0; i < navigationView.getMenu().size(); i++) {
            navigationView.getMenu().getItem(i).setChecked(false);
        }

        // TODO: Find a better solution

        gameTask.loadGames();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void taskFinished(String output) {
        // load games of player or games in venue
        if (output.contains("game")) {

            loadedGames = gson.fromJson(output, Game[].class);

            modelList = new ArrayList<>();
            if (LoginTask.currentAuthUser.isManager()) {
                for (int i = 0; i < loadedGames.length; i++) {
                    if (loadedGames[i].getVenueAddress().equals(LoginTask.currentAuthUser.getVenueLocation()))
                        modelList.add(new RvGameModel(loadedGames[i]));
                }
            }
            for (int i = 0; i < loadedGames.length; i++) {
                modelList.add(new RvGameModel(loadedGames[i]));
            }
            mAdapter.updateList(modelList);
        }
    }
}



