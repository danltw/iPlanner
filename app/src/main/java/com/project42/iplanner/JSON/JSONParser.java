package com.project42.iplanner.JSON;

import android.content.ContentValues;
import android.util.Log;
import android.util.Pair;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class JSONParser {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    // constructor
    public JSONParser() {

    }

    // function get json from url
    // by making HTTP POST or GET mehtod
    public JSONObject makeHttpRequest(String url, String method,
                                      ContentValues params) throws IOException {

        // Making HTTP request
        try {
            final OkHttpClient client = new OkHttpClient();
            Request request = null;
            // check for request method
            if (method.equals("POST")) {
                // request method is POST
                final MediaType JSON
                        = MediaType.parse("application/json; charset=utf-8");
                final RequestBody body = RequestBody.create(JSON, params.toString());
                request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
            } else if (method.equals("GET")) {
                // request method is GET
                request = new Request.Builder()
                        .url(url)
                        .build();
            }
            final Response response = client.newCall(request).execute();
            json = response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }
        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e ){
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        // return JSON String
        return jObj;

    }
}
