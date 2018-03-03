package com.c50x.eleos.controllers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.c50x.eleos.R;
import com.c50x.eleos.data.AppDatabase;
import com.c50x.eleos.data.Team;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TeamTask{
    private AppDatabase db;
    private Context context;
    private Team teamList[];
    private String urlBase;
    private OkHttpClient client;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public TeamTask(Context appContext, Context activityContext){
        this.context = activityContext;
        db = AppDatabase.getDatabaseInstance(appContext);
        // load the server address from string.xml
        urlBase = appContext.getString(R.string.server_address);
        // create the client used to make the requests
        client = new OkHttpClient();
    }


    // common methods
    private String getRequest(String url) throws IOException{
        Request request = new Request.Builder().url(url).build();

        Response response = client.newCall(request).execute();

        return response.body().toString();
    }

    private String postRequest(String url, String json) throws IOException {

        RequestBody body = RequestBody.create(JSON,json);
        Request request = new Request.Builder().url(url).post(body).build();

        Response response = client.newCall(request).execute();

        return response.body().toString();
    }





    private class addTeam extends AsyncTask<Team,Void,String> {

        @Override
        protected String doInBackground(Team... par) {
            Team teamToAdd = par[0];

            // convert into a JSON object
            Gson gson = new Gson();
            String json = gson.toJson(teamToAdd);
            Log.i("TeamTask", "json to be sent: " + json);

            // construct the url
            Log.i("TeamTask", "using server address: " + urlBase);
            String url = urlBase + "/addTeam.php";
            Log.i("TeamTask", "url: " + url);

            // send post request
            String response;
            try {

                response = postRequest(url,json);
            }catch (Exception e){
                response = "connection broken";
            }

        return response;
    }

        protected void onPostExecute(String response){

            Gson gson = new Gson();
            // convert json to Map
            Map<String,Object> responseMap = gson.fromJson(response,
                    new TypeToken<Map<String,Object>>(){}.getType());

            // print responseMap to logCat
            Log.i("TeamTask", "response hashMap ");
            for (Map.Entry<String,Object> entry: responseMap.entrySet()){
                String key = entry.getKey();
                Object value = entry.getValue();
                Log.i("TeamTask","key: " + key + "value: " + value.toString());
            }
        }
    }


    public void addTeam(Team team){

        new addTeam().execute(team);

    }


    // get team
}
