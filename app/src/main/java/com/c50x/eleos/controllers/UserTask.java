package com.c50x.eleos.controllers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.c50x.eleos.data.AppDatabase;
import com.c50x.eleos.data.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

    private class LoadAllUsers extends AsyncTask<String,String,JSONObject> {

        @Override
        protected JSONObject doInBackground(String ... par) {


            String str="http://192.168.43.171:8888/android_connect/get_all_players.php";
            URLConnection urlConn = null;
            BufferedReader bufferedReader = null;
            try
            {

                URL url = new URL(str);
                urlConn = url.openConnection();
                bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

                StringBuffer stringBuffer = new StringBuffer();
                String line;
                while ((line = bufferedReader.readLine()) != null)
                {
                    stringBuffer.append(line);
                }
                return new JSONObject(stringBuffer.toString());


            }
            catch(Exception ex)
            {
                Log.e("App", "yourDataTask", ex);
                return null;
            }
            finally
            {
                if(bufferedReader != null)
                {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }



        @Override
        protected void onPostExecute(JSONObject response)
        {
            if(response != null)
            {
                try {
                    Log.e("App", "Success: " + response.getString("yourJsonElement") );
                } catch (JSONException ex) {
                    Log.e("App", "Failure", ex);
                }
            }
        }
    }
    public void loadAllPlayers(){

        new LoadAllUsers().execute();

    }
}
