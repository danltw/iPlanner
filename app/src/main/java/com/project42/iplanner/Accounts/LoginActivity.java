package com.project42.iplanner.Accounts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project42.iplanner.AppConfig;
import com.project42.iplanner.AppController;
import com.project42.iplanner.Home.HomeActivity;
import com.project42.iplanner.R;
import com.project42.iplanner.Utilities.SharedManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

public class LoginActivity extends AppCompatActivity {
    private static final int SUCCESS = 0;
    private static final int USERNAME_OCCUPIED = 1;
    private static final int USERNAME_INVALID = 2;
    private static final int PASSWORD_INCORRECT = 3;
    private static ArrayList<String> accountList = new ArrayList<String>();

    private static final String TAG = LoginActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private SessionManager session;
    //JSONParser jsonParser = new JSONParser();



    public LoginActivity(){

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login_ui);

        final Button login_button = findViewById(R.id.login_button);
        final Button switch_mode = findViewById(R.id.switch_mode_toreg);

        if (SharedManager.getInstance(LoginActivity.this).getUser() != null) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        if (savedInstanceState == null) {
            Fragment fragment = new LoginUI();

            FragmentManager manager = getSupportFragmentManager();
            manager.popBackStack();

            login_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickLogin(view);
                }
            });
            switch_mode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickSwitchMode(view);
                }
            });
        }
    }

    private void onClickLogin(View view){
        int status;

        final String username = ((EditText)findViewById(R.id.login_username)).getText().toString();
        final String password = ((EditText)findViewById(R.id.login_password)).getText().toString();
        //AccountController accountController = new AccountController(username, password);

        if (!username.isEmpty() && !password.isEmpty()) {
            // login user
            //verifyUser(username, password);
            AccountController ac = new AccountController(username, password, LoginActivity.this);
            ac.loginUser(username, password);

        } else {
            // Prompt user to enter credentials
            Toast.makeText(getApplicationContext(),
                    "Please enter your credentials!", Toast.LENGTH_LONG)
                    .show();
        }

//        status=0; //placeholder to test transition to home page
//        verifyUser(username, password);


        //stringResponseHandler(status, username, password);
    }

    private void stringResponseHandler(int status, String username, String password) {
        if (username.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Please input your credentials!", Toast.LENGTH_LONG).show();
        }


        if (status == SUCCESS){
            Toast.makeText(this, "Logging in...", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
        else if (status == USERNAME_INVALID){
            Toast.makeText(this, "Wrong username!", Toast.LENGTH_LONG).show();
        }
        else if (status == PASSWORD_INCORRECT){
            Toast.makeText(this, "Wrong password!", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "(DEBUG) Uncaught error!", Toast.LENGTH_LONG).show();
        }
    }

    private void onClickSwitchMode(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }




    private void verifyUser(final String username, final String password) {
        //boolean status = false;

        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();
        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);

                        //String username = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String username = user.getString("username");

                        //Keep track of which users are logged in
                        //db.addUser(username, email);
                        accountList.add(username);

                        // Launch main activity
                        Intent intent = new Intent(LoginActivity.this,
                                HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);

                return params;
            }

        };



        AppController.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}

