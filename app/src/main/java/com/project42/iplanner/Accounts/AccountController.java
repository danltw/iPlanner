package com.project42.iplanner.Accounts;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project42.iplanner.AppConfig;
import com.project42.iplanner.Groups.CreateGroupChannelActivity;
import com.project42.iplanner.Home.HomeActivity;
import com.project42.iplanner.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.project42.iplanner.Utilities.ListUtils;
import com.project42.iplanner.Utilities.SharedManager;
import com.project42.iplanner.Utilities.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class AccountController extends AppCompatActivity {

    private static final String TAG = AccountController.class.getSimpleName();
    private ProgressDialog pDialog;

    private Context mCtx;
    OnCallbackResponse callback;

    public interface OnCallbackResponse {
        public void onRegResponse(String response);
        public void onLoginResponse(Account user);
        public void onUpdateResponse(String response);
    }

    public AccountController(Context context) {
        mCtx = context;
        callback = (OnCallbackResponse) mCtx;
        pDialog = new ProgressDialog(mCtx);
    }

    // Login a user
    public void loginUser(final String username, final String password) {
        showDialog(); // display loading icon
        // Tag used to cancel the request
        String tag_string_req = "req_get_user";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "User Login Response: " + response.toString());

                // Process the JSON
                try {
                    JSONArray jsonResponse = new JSONArray(response);

                    // If some errors occurred, return
                    JSONObject errResponse = jsonResponse.getJSONObject(0);
                    boolean error = errResponse.has("error");
                    if (error) {
                        hideDialog();
                        Log.d("Login Error: ", errResponse.getString("error"));
                        callback.onLoginResponse(null);
                        return;
                    }
                    else {
                        hideDialog();
                        // Direct user to home acitivity screen
                        Account a = new Account(username, jsonResponse.getJSONObject(0).getString("email"));
                        callback.onLoginResponse(a);
                    }

                } catch (JSONException e) {
                    hideDialog();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Log.e(TAG, "Login Error: " + error.getMessage());
            }
        }) {
            // method to pass user input to php page
            @Override
            protected Map<String, String> getParams() {
                // Posting params to php page
                Map<String, String> params = new HashMap<String, String>();
                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                    try {
                        params.put("method", "login");
                        params.put("username", username);
                        params.put("password", password);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return params;
            }

        };

        // Adding request to request queue
        RequestQueue queue = Volley.newRequestQueue(mCtx);
        queue.add(strReq);
    }

    // Register a user
    public void regUser(final String username, final String password, final String email) {
        // Tag used to cancel the request
        String tag_string_req = "req_add_user";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REG, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "User Register Response: " + response.toString());

                // Process the JSON
                try {
                    JSONArray jsonResponse = new JSONArray(response);

                    // If some errors occurred, return
                    JSONObject errResponse = jsonResponse.getJSONObject(0);
                    boolean error = errResponse.has("error");
                    if (error) {
                        Log.d("Register Error: ", errResponse.getString("error"));
                        callback.onRegResponse(errResponse.get("error").toString());
                        return;
                    }
                    else {
                        // Direct user to home acitivity screen
                        callback.onRegResponse(null);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Register Error: " + error.getMessage());
            }
        }) {
            // method to pass user input to php page
            @Override
            protected Map<String, String> getParams() {
                // Posting params to php page
                Map<String, String> params = new HashMap<String, String>();
                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                    try {
                        params.put("method", "register");
                        params.put("username", username);
                        params.put("password", password);
                        params.put("email", email);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return params;
            }

        };

        // Adding request to request queue
        RequestQueue queue = Volley.newRequestQueue(mCtx);
        queue.add(strReq);
    }

    // Update a user
    public void updateUser(final String username, final String oldPassword, final String newPassword, final String email) {
        // Tag used to cancel the request
        String tag_string_req = "req_update_user";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_UPDATE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "User Update Response: " + response.toString());

                // Process the JSON
                try {
                    JSONArray jsonResponse = new JSONArray(response);

                    // If some errors occurred, return
                    JSONObject errResponse = jsonResponse.getJSONObject(0);
                    boolean error = errResponse.has("error");
                    if (error) {
                        Log.d("Update Error: ", errResponse.getString("error"));
                        callback.onUpdateResponse(errResponse.get("error").toString());
                        return;
                    }
                    else {
                        // Direct user to home acitivity screen
                        callback.onUpdateResponse(null);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Update Error: " + error.getMessage());
            }
        }) {
            // method to pass user input to php page
            @Override
            protected Map<String, String> getParams() {
                // Posting params to php page
                Map<String, String> params = new HashMap<String, String>();
                if (!TextUtils.isEmpty(username)) {
                    try {
                        params.put("method", "update");
                        params.put("username", username);
                        if (!TextUtils.isEmpty(oldPassword) && !TextUtils.isEmpty(newPassword)) {
                            params.put("oldPass", oldPassword);
                            params.put("newPass", newPassword);
                        }
                        if (!TextUtils.isEmpty(email))
                            params.put("email", email);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return params;
            }

        };

        // Adding request to request queue
        RequestQueue queue = Volley.newRequestQueue(mCtx);
        queue.add(strReq);
    }

    private void showDialog() {
        if (!pDialog.isShowing()) {
            pDialog.setTitle("Verifying User...");
            pDialog.show();
        }
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
