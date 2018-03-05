package com.c50x.eleos.activities;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.c50x.eleos.R;
import com.c50x.eleos.controllers.AsyncResponse;
import com.c50x.eleos.controllers.LoginTask;
import com.c50x.eleos.data.AppDatabase;
import com.c50x.eleos.data.User;
import com.google.gson.Gson;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.WindowManager;

import static com.c50x.eleos.activities.LoginActivity.EXPECTED_MIN_RESPONSE_LENGTH;

public class MainActivity extends AppCompatActivity implements AsyncResponse
{
    private Button logOutButton;
    private AppDatabase db;
    private String handle;
    private String email;
    private TextView handleView;
    private TextView nameView;
    private TextView emailView;
    private User[] l;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private LoginTask loginTask;
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

        // needed to access the auth methods
        loginTask = new LoginTask(this);

        if (LoginTask.currentAuthUser != null) { // we have a token but no user is loaded
            // set the global current user using the token
            loginTask.authUsingToken(token);
        }


        handleView = findViewById(R.id.activity_main_user_handle_textView);

        handleView.setText(token);

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
        if (!output.contains("null")) { // user is valid
            // load data from json to current user
            Gson gson = new Gson();
            LoginTask.currentAuthUser = gson.fromJson(output,User.class);
            Log.i("mainActivity_taskF", "current user handle: " + LoginTask.currentAuthUser.getHandle());

        }
    }
}



