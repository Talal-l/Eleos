package com.c50x.eleos.controllers;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AsyncGet extends AsyncTask<String,Void,String> {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public AsyncResponse delegate = null;

    public AsyncGet(AsyncResponse delegate){
        this.delegate = delegate;
    }


    private String getRequest(String url) throws IOException {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    @Override
    protected String doInBackground(String... par) {
        String url = par[0];
        String queryKey = par[1];
        String queryValue = par[2];

        // send get request
        String response;
        try {

            HttpUrl completeUrl = new HttpUrl.Builder().host(url).
                    addQueryParameter(queryKey,queryValue).build();

            response = getRequest(completeUrl.toString());
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

