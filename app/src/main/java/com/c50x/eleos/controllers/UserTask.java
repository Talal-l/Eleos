package com.c50x.eleos.controllers;

import android.content.Context;
import android.os.AsyncTask;

import com.c50x.eleos.data.AppDatabase;
import com.c50x.eleos.data.User;

public class UserTask {
    private AppDatabase db;
    private Context context;
    private User userList[];


    public UserTask(Context appContext, Context activityContext){
        this.context = activityContext;
        db = AppDatabase.getDatabaseInstance(appContext);
    }

    private class LoadAllUsers extends AsyncTask<Void,Void,Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        protected void onPostExecute(Long rowNum){

        }
    }
    public void loadAllPlayers(){

        new LoadAllUsers().execute();

    }
}
