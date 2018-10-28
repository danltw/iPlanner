package com.project42.iplanner.Accounts;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project42.iplanner.AppConfig;
import com.project42.iplanner.Home.HomeActivity;
import com.project42.iplanner.R;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

//import info.androidhive.loginandregistration.R;
//import info.androidhive.loginandregistration.app.AppConfig;
//import info.androidhive.loginandregistration.app.AppController;
//import info.androidhive.loginandregistration.helper.SQLiteHandler;
//import info.androidhive.loginandregistration.helper.SessionManager;

public class AccountController extends AppCompatActivity {
    private static ArrayList<String> accountList = new ArrayList<String>();
    private static final int SUCCESS = 0;
    private static final int USERNAME_OCCUPIED = 1;
    private static final int USERNAME_INVALID = 2;
    private static final int PASSWORD_INCORRECT = 3;
    private static final int UNKNOWN_ERROR = 4;
    private static final String TAG = LoginActivity.class.getSimpleName();
    private ProgressDialog pDialog = new ProgressDialog(this);
    private SessionManager session;
    //private SQLiteHandler db;


    private String username;
    private String password;
    private ArrayList<String> profileSettings = new ArrayList<String>();


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login_ui);



        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
    }

    public AccountController(String username, String password){
        this.username = username;
        this.password = password;
    }

    public static int createAccount(String username, String password){
        if (accountExists(username)){
            return USERNAME_OCCUPIED;                                       //Username already exists
        }
        if (username.length()>45 || password.length()>45){
            return USERNAME_INVALID;                                       //Password or username too long
        }

//        new Account(username, password);
//        new AccountController(username, password);
        accountList.add(username);

        return SUCCESS;
    }

    public int deleteAccount(String username, String password){
        if (!accountExists(username)) return USERNAME_INVALID;
        if (!verifyUser(username, password)) return PASSWORD_INCORRECT;

        //pass all checks, then:
        Account.deleteAccount(username);
        accountList.remove(username);
        return SUCCESS;
    }

    public int login(String username, String password){
        //if (!accountExists(username)) return USERNAME_INVALID;
        if (!verifyUser(username, password)) return PASSWORD_INCORRECT;

        //new AccountController(username, password);
        return SUCCESS;
    }

    private static boolean accountExists(String username){
        return (accountList.contains(username));
    }

//    public static boolean verifyUser(String username, String password){
//        String realPassword;
//
//        Account account = Account.getAccount(username);
//        realPassword = account.getPassword(username);
//
//        return (password.equals(realPassword));
//    }

    private boolean verifyUser(final String email, final String password) {
        boolean status = false;

        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();
        StringRequest stringRequest;
        stringRequest = new StringRequest(Method.POST,
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

                        // Inserting row in users table
                        //db.addUser(username, email);

                        // Launch main activity
                        Intent intent = new Intent(AccountController.this,
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
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };



        Volley.newRequestQueue(this).add(stringRequest);
        return status;
    }

    public Account getAccountDetails(){
        //return both email and phone number by returning entire account object
        if (accountExists(this.username) && verifyUser(this.username, this.password)){
            return Account.getAccount(username);
        }

        return null;
    }
    public int updateAccount(String email, String phoneNumber){
        if (!accountExists(this.username)) return USERNAME_INVALID;
        if (!verifyUser(this.username, this.password)) return PASSWORD_INCORRECT;

        Account account = Account.getAccount(this.username);
        account.setEmail(this.username, email);
        account.setPhoneNumber(this.username, phoneNumber);

        return SUCCESS;
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
