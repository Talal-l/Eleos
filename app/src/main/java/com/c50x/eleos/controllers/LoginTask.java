package com.c50x.eleos.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.c50x.eleos.R;
import com.c50x.eleos.activities.MainActivity;
import com.c50x.eleos.data.AppDatabase;
import com.c50x.eleos.data.User;

public class LoginTask {
    private AppDatabase db;
    private Context context;
    private User userList[];

    public LoginTask(Context appContext, Context activityContext){
        this.context = activityContext;
        db = AppDatabase.getDatabaseInstance(appContext);
    }

    private class AuthUsingEmail extends AsyncTask<String,Void,Boolean> {

        @Override
        protected Boolean doInBackground(String ... par) {

            String userEmail = par[0];
            String userPassword = par[1];

            // TODO check with remote server instead of local

            // Check if the user email and password match with the database
            userList = db.userDao().authUsingEmail(userEmail, userPassword);
            if (userList.length == 1)
                return true;

        return false;
    }

        protected void onPostExecute(Boolean success){

            AutoCompleteTextView emailView = ((Activity) context).findViewById(R.id.email);
            if (success) {
                Toast.makeText(context, "LogIn successful !", Toast.LENGTH_LONG).show(); // prints on the screen that log in was successful
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("from", "login"); // To tell what screen we came from

                // To load the logged in player when they move to the main screen
                intent.putExtra("email", emailView.getText().toString());
                context.startActivity(intent); // switch to main activity
            }
            else {
                // Show error message to user
                // TODO better message

                emailView.setError("Incorrect Email or Password");
                emailView.requestFocus();
            }
        }
    }

   public void authUsingEmail(String email, String password){
        new AuthUsingEmail().execute(email,password);
    }


}
