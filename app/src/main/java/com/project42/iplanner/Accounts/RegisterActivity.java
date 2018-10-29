package com.project42.iplanner.Accounts;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.project42.iplanner.AppConfig;
import com.project42.iplanner.AppController;
import com.project42.iplanner.Home.HomeActivity;
import com.project42.iplanner.R;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends Activity {
    private static final int SUCCESS = 0;
    private static final int USERNAME_OCCUPIED = 1;
    private static final int USERNAME_INVALID = 2;
    private static final int PASSWORD_INCORRECT = 3;

    private static final String TAG = RegisterActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private SessionManager session;

    public RegisterActivity(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_register);

        final Button register = findViewById(R.id.register_button);
        final Button switch_mode = findViewById(R.id.switch_mode_tologin);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(RegisterActivity.this,
                    HomeActivity.class);
            startActivity(intent);
            finish();
        }

        if (savedInstanceState == null) {
            Fragment fragment = new RegisterUI();

//            FragmentManager manager = getSupportFragmentManager();
//            manager.popBackStack();

            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickRegister(view);
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

    private void onClickRegister(View view){
        int status;

        final String username = ((EditText)findViewById(R.id.login_username)).getText().toString();
        final String password = ((EditText)findViewById(R.id.login_password)).getText().toString();

        if (!username.isEmpty() && !password.isEmpty()) {
            //registerUser(username, password);
            AccountController ac = new AccountController(username, password, RegisterActivity.this);
            ac.regUser(username, password, "blabal");
        } else {
            Toast.makeText(getApplicationContext(),
                    "Please enter your details!", Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void registerUser(String username, String password){
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL

                        JSONObject user = jObj.getJSONObject("user");
                        String username = jObj.getString("username");

                        // Inserting row in users table



                        Toast.makeText(getApplicationContext(), "Successfully Registered!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(
                                RegisterActivity.this,
                                HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance(this).addToRequestQueue(stringRequest);
    }


    private void onClickSwitchMode(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
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
