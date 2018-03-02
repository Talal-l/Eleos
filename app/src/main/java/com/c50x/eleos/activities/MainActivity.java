package com.c50x.eleos.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.c50x.eleos.R;
import com.c50x.eleos.data.AppDatabase;
import com.c50x.eleos.data.User;
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
            }
            else {
                handle = getIntent().getStringExtra("handle");
                Log.w("Handle222: ", handle);
                if (db.userDao().loadUserWithHandle(handle).length > 0) {
                    currentUser = db.userDao().loadUserWithHandle(handle)[0];
                    Log.w("Handle: ", currentUser.getHandle());
                }
            }

            l = db.userDao().loadAllUsers();
            for (int i = 0; i < l.length; i++){
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
        new DatabaseAsync().execute();

        handleView = findViewById(R.id.activity_main_user_handle_textView);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //hides keyboard upon switching to this Activity

        setContentView(com.c50x.eleos.R.layout.activity_registration);


    }
/*
        // for navigation menu
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            //configureCreateGameButton();
        }
        Button create_game_button = (Button)findViewById(R.id.createGame_tab);
        create_game_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(MainActivity.this , CreateGameActivity.class));
            }
        });*/
    }

/*
    // for navigation menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.navigation_menu , menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(id == R.id.createGame_tab)
            return true;

        return super.onOptionsItemSelected(item);
    }

*/
}
