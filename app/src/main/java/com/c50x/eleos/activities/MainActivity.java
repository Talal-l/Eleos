package com.c50x.eleos.activities;

import android.arch.persistence.room.Database;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.c50x.eleos.R;
import com.c50x.eleos.data.AppDatabase;
import com.c50x.eleos.data.User;

import java.util.ArrayList;

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

            emailView.append(currentUser.getEmail());
            nameView.append(currentUser.getName());
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
        nameView = findViewById(R.id.activity_main_user_name_textView);
        emailView = findViewById(R.id.user_email_textView);






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
