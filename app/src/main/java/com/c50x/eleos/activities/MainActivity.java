package com.c50x.eleos.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.c50x.eleos.R;
import com.c50x.eleos.adapters.RvGameAdapter;
import com.c50x.eleos.controllers.AsyncResponse;
import com.c50x.eleos.controllers.GameTask;
import com.c50x.eleos.controllers.LoginTask;
import com.c50x.eleos.data.Game;
import com.c50x.eleos.data.Team;
import com.c50x.eleos.data.User;
import com.c50x.eleos.data.Venue;
import com.google.gson.Gson;

import java.util.ArrayList;

import static com.c50x.eleos.data.Request.PENDING;

public class MainActivity extends AppCompatActivity implements AsyncResponse {

    private static final String TAG = "MainActivity";
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    private GameTask gameTask;
    private View menuHeader;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private LoginTask loginTask;
    private RecyclerView recyclerView;
    private TextView tvPlayerHandle;
    private TextView tvUserName;
    private com.c50x.eleos.data.Game[] loadedGames;
    private NavigationView navigationView;
    private Gson gson;
    private SwipeRefreshLayout swipeRefreshRecyclerList;
    private RvGameAdapter mAdapter;
    private ArrayList<Game> modelList = new ArrayList<>();
    private CardView user_card_img;
    private ImageView user_img;
    private Team teamToAdd;
    private Game gameToJoin;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        currentUser = LoginTask.currentAuthUser;

        if (currentUser != null) { // we have a valid current user

            setContentView(R.layout.activity_main);

            // hides keyboard upon switching to this Activity
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


            // initialize variables
            loginTask = new LoginTask(this);
            gson = new Gson();
            gameTask = new GameTask(MainActivity.this);
            mAdapter = new RvGameAdapter(MainActivity.this, modelList);
            modelList = new ArrayList<>();

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


            mAdapter.setOnItemClickListener(new RvGameAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position, Game model) {

                    int selectedGameId = model.getGameId();
                    Log.i(TAG, "Selected Game id: " + selectedGameId);
                    Intent intent = new Intent(MainActivity.this, GameInfoActivity.class);

                    // get game from loadedGames and send it as json to view activity
                    String gameJson = null;
                    for (com.c50x.eleos.data.Game gm : loadedGames) {
                        if (gm.getGameId() == selectedGameId) {
                            gameJson = gson.toJson(gm);
                            break;
                        }
                    }
                    intent.putExtra("gameId", gameJson);
                    startActivity(intent);
                }
            });

            // handle join button click
            mAdapter.setOnJoinClickListener(new RvGameAdapter.OnJoinClickListener() {
                @Override
                public void onJoinClick(View view, int position, Game model) {

                    // go to team selection for admin to select one of their teams
                    Intent intent = new Intent(MainActivity.this, TeamSelectionActivity.class);
                    startActivityForResult(intent, 1);

                    gameToJoin = model;


                }
            });

            // handle location textView click
            mAdapter.setOnLocationClickListener(new RvGameAdapter.OnLocationClickListener() {
                @Override
                public void onLocationClick(View view, int position, Game model) {
                    Log.i(TAG, "venue Coordinate: " + model.getVenue().getVenueCoordinate());

                    if (model.getVenue().getVenueCoordinate() != null) {

                        String gameVenueJson = gson.toJson(model.getVenue(), Venue.class);

                        Intent intent = new Intent(MainActivity.this, MapActivity.class);
                        intent.putExtra("gameVenue", gameVenueJson);
                        startActivity(intent);
                    }

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
                            mAdapter.updateList(modelList);

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
            user_card_img = menuHeader.findViewById(R.id.card_view_user_image);
            user_img = menuHeader.findViewById(R.id.img_user);
            tvPlayerHandle = menuHeader.findViewById(R.id.tv_nav_header_player_handle);
            tvUserName = menuHeader.findViewById(R.id.tv_nav_header_name);

            user_card_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, EditPlayerInfoActivity.class));
                }
            });

            // display info in nav header for manager
            if (currentUser.isManager()) {

                Menu navMenu = navigationView.getMenu();
                navMenu.findItem(R.id.nav_menu_venue_info).setVisible(true);
                navMenu.findItem(R.id.nav_menu_gameCreation).setVisible(false);
                navMenu.findItem(R.id.nav_menu_my_games).setVisible(false);
                navMenu.findItem(R.id.nav_menu_teamCreation).setVisible(false);
                navMenu.findItem(R.id.nav_menu_my_teams).setVisible(false);
                tvUserName.setText(currentUser.getName());

            } else {

                tvPlayerHandle.setText(currentUser.getHandle());
                tvUserName.setText(currentUser.getName());
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
                                case R.id.nav_menu_my_games:
                                    intent = new Intent(MainActivity.this, AdminGamesActivity.class);
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
                                case R.id.nav_menu_requests:
                                    intent = new Intent(MainActivity.this, RequestsActivity.class);
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
        } else {
            Intent intent = new Intent(this, LoadingActivity.class);
            startActivity(intent);
            finish();
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

        mAdapter.updateList(modelList);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) { // coming from team selection activity
            if (resultCode == RESULT_OK) {
                String dataJson;
                dataJson = data.getStringExtra("mainTeam");
                teamToAdd = gson.fromJson(dataJson, Team.class);

                // change state to pending
                gameToJoin.setState(PENDING);
                gameToJoin.setTeam2(teamToAdd.getTeamName());
                // add team2 to game
                gameTask.updateGame(gameToJoin);

                // send join request to game admin
                gameTask.sendJoinRequest(teamToAdd, gameToJoin);


            }
        }
    }

    @Override
    public void taskFinished(String output) {

        Log.i(TAG, "json response: " + output);
        // load games of player or games in venue
        if (!output.contains("game updated") && !output.contains("newRequest")) {

            loadedGames = gson.fromJson(output, com.c50x.eleos.data.Game[].class);


            for (Game game : loadedGames) {
                if (game.getVenue().getVenueName() != null) {
                    if (game.getVenue().getVenueName().equals(currentUser.getVenueName())) {

                        modelList.add(game);
                    }
                }
            }
        }else {
            for (int i = 0; i < loadedGames.length; i++) {
                modelList.add(loadedGames[i]);
            }
            mAdapter.updateList(modelList);
        }
    }
}



