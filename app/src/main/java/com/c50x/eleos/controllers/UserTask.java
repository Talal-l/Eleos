package com.c50x.eleos.controllers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.c50x.eleos.data.AppDatabase;
import com.c50x.eleos.data.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class UserTask {
    private AppDatabase db;
    private Context context;
    private User userList[];


    public UserTask(Context appContext, Context activityContext){
        this.context = activityContext;
        db = AppDatabase.getDatabaseInstance(appContext);
    }

    private class LoadAllUsers extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String ... par) {
            HttpURLConnection urlConnection;
            try{

                URL url = new URL("http://192.168.1.101/printAll.php");;;;
                urlConnection = (HttpURLConnection) url.openConnection();

                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();


                // Read the stream into a string

                String line;
                while ((line = br.readLine()) != null){
                    sb.append(line+"\n");
                }
                br.close();
                return sb.toString();


            }
            catch(Exception ex) {
                Log.e("App", "yourDataTask", ex);
                return null;
            }
            finally{
            }
        }


        @Override
        protected void onPostExecute(String response)
        {
            if(response != null)
            {
                try {
                    Log.e("App", "Success: " + response);
                } catch (Exception ex) {
                    Log.e("App", "Failure", ex);
                }
            }
        }
    }
    public void loadAllPlayers(){

        new LoadAllUsers().execute();

    }
}
