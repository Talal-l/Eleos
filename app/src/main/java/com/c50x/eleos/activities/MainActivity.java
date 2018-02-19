package com.c50x.eleos.activities;

import android.arch.persistence.room.Database;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

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

    // Create an AsyncTask class so the database operations can be done in the background
    private class DatabaseAsync extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            // Get user from database
            currentUser = db.userDao().loadUserWithHandle(handle);
            return null;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logOutButton = findViewById(R.id.activity_main_logout_button);

        logOutButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // handle log out
            }
        });

    }
}
