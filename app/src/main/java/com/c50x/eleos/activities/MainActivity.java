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
import com.c50x.eleos.data.AppDatabase;
import com.c50x.eleos.data.User;

public class MainActivity extends AppCompatActivity
{
    private Button logOutButton;
    private User currentUser;
    private AppDatabase db;
    private String handle;
    private String email;
    private TextView handleView;
    private TextView nameView;
    private TextView emailView;
    private User[] l;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    FragmentTransaction fragmentTransaction;

    // Create an AsyncTask class so the database operations can be done in the background
    private class DatabaseAsync extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            // Get user from database
            String source = getIntent().getStringExtra("from");
            if (source.equals("login")) {
                email = getIntent().getStringExtra("email");
                Log.w("Email is : ", email);
                if (db.userDao().loadUserWithEmail(email).length > 0) {
                    currentUser = db.userDao().loadUserWithEmail(email)[0];
                    Log.w("email for user: ", currentUser.getEmail());
                }
            } else {
                handle = getIntent().getStringExtra("handle");
                Log.w("Handle222: ", handle);
                if (db.userDao().loadUserWithHandle(handle).length > 0) {
                    currentUser = db.userDao().loadUserWithHandle(handle)[0];
                    Log.w("Handle: ", currentUser.getHandle());
                }
            }

            l = db.userDao().loadAllUsers();
            for (int i = 0; i < l.length; i++)
            {
                Log.w("handle: ", l[i].getHandle());
                Log.w("email: ", l[i].getEmail());
            }

            handleView.append(currentUser.getHandle());
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentUser = new User();

        db = AppDatabase.getDatabaseInstance(getApplicationContext());
        //new DatabaseAsync().execute();

        handleView = findViewById(R.id.activity_main_user_handle_textView);

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

                            case R.id.nav_menu_playerSearch:
                                intent = new Intent(MainActivity.this, PlayerSearchActivity.class);
                                startActivity(intent);
                                break;
                        }

                        return false;
                    }
                }
        );




        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    // for navigation menu button
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(mToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }
}



