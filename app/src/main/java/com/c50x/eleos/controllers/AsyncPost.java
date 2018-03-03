package com.c50x.eleos.controllers;

import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AsyncPost extends AsyncTask<String,Void,String> {

    private OkHttpClient client;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public AsyncResponse delegate = null;


    private String postRequest(String url, String json) throws IOException {

        RequestBody body = RequestBody.create(JSON,json);
        Request request = new Request.Builder().url(url).post(body).build();

        Response response = client.newCall(request).execute();

        return response.body().toString();
    }

    @Override
    protected String doInBackground(String... par) {
        String url = par[0];
        String json = par[1];

        // send post request
        String response;
        try {

            response = postRequest(url, json);
        } catch (Exception e) {
            // TODO: Send json string with error
            response = "connection broken";
        }
        return response;
    }

    protected void onPostExecute(String response) {
        delegate.taskFinished(response);
    }
}



