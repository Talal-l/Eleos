package com.c50x.eleos.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

import com.c50x.eleos.R;
import com.c50x.eleos.activities.MainActivity;
import com.c50x.eleos.data.AppDatabase;
import com.c50x.eleos.data.User;

public class RegisterTask extends AsyncTask<User, Void,Integer>{

    private AppDatabase db;
    private Context activityContext;
    private User newUser;

    public final static Integer USER_WITH_EMAIL_EXIST = 1;
    public final static Integer USER_WITH_HANDLE_EXIST = 2;
    public final static Integer USER_WITH_HANDLE_AND_EMAIL_EXIST = 3;


    public RegisterTask(Context appContext, Context activityContext) {
        db = AppDatabase.getDatabaseInstance(appContext);
        this.activityContext = activityContext;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(User ... users) {

        newUser = users[0];



        // TODO: Do this in one query
        User[] userWithHandle;
        User[] userWithEmail;

        // Check if handle exist
        userWithHandle = db.userDao().loadUserWithHandle(newUser.getHandle());

        // Check if email exist
        userWithEmail=  db.userDao().loadUserWithEmail(newUser.getEmail());


        if (userWithHandle.length != 0 && userWithEmail.length !=0)
            return USER_WITH_HANDLE_AND_EMAIL_EXIST;
       else if (userWithEmail.length != 0)
            return  USER_WITH_EMAIL_EXIST;
       else if (userWithHandle.length != 0)
           return USER_WITH_HANDLE_EXIST;
       else { // User has valid info

            db.userDao().saveUser(newUser);
        }


        return 0;
    }

    @Override
    protected void onPostExecute(Integer state) {
        super.onPostExecute(state);

        EditText emailView = ((Activity) activityContext).findViewById(R.id.email);
        EditText handleView = ((Activity) activityContext).findViewById(R.id.handle);

        if (state.equals(USER_WITH_HANDLE_AND_EMAIL_EXIST)){

            // Show error for email and handle

            emailView.setError("Email taken");
            handleView.setError("Handle taken");

        }
        else if (state.equals(USER_WITH_EMAIL_EXIST)){

            // Show error for email

            emailView.setError("Email taken");
        }
        else if (state.equals(USER_WITH_HANDLE_EXIST)){

            // Show error for handle

            handleView.setError("Handle taken");

        }
        else {


            // Go to main screen

            Intent intent = new Intent(activityContext, MainActivity.class);
            intent.putExtra("from","registration");
            intent.putExtra("handle",newUser.getHandle());
            activityContext.startActivity(intent);


        }
    }
}
