package com.c50x.eleos.controllers;

import android.content.Context;
import android.os.AsyncTask;

import com.c50x.eleos.data.AppDatabase;
import com.c50x.eleos.data.User;

public class LoginTask {
    private AppDatabase db;
    private User userToAuth;
    private int authState;
    private User userList[];


    private class AuthUsingEmail extends AsyncTask<Void,Void,Void> {
        AuthUsingEmail(Context context, User user) {
            db = AppDatabase.getDatabaseInstance(context);
            userToAuth = user;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            // TODO check with remote server instead of local

            // Check if the user email and password match with the database
            userList = db.userDao().authUsingEmail(userToAuth.getEmail(), userToAuth.getPassword());
            return null;
        }
    }
        private class AuthUsingHandle extends AsyncTask<Void,Void,Void> {
        AuthUsingHandle(Context context, User user) {
            db = AppDatabase.getDatabaseInstance(context);
            userToAuth = user;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            // TODO check with remote server instead of local

            // Check if the user email and password match with the database
            userList = db.userDao().authUsingHandle(userToAuth.getHandle(), userToAuth.getPassword());
            return null;
        }
    }
}
