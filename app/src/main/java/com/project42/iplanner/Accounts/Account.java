package com.project42.iplanner.Accounts;

import android.accounts.AccountAuthenticatorActivity;
import android.app.ProgressDialog;
import android.content.Context;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project42.iplanner.AppConfig;
import com.project42.iplanner.DBConn.DBController;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Account {

    private String username;
    private String password;
    private String email;
    private String phoneNumber;


    Account(String username, String password){
        //Maybe we make this class purely static, there doesn't have to be permanent account objects.
        //They can just be created on demand from the AccountController and deleted after passing information.
        this.username = username;
        this.password = password;

    }

    static void deleteAccount(String username){
        //remove account from database
    }

    static Account getAccount(String username){
        //getAccount is called by account controller to retrieve the account object from database
        Account account = null;

        //do some sql shit here

        return account;
    }


    String getPassword(String username){
        String password = null;
        //sql implementation
        return password;
    }

    void setEmail(String username, String email){

    }

    void setPhoneNumber(String username, String phoneNumber){

    }

//    private void loadProducts() {
//
//        /*
//         * Creating a String Request
//         * The request type is GET defined by first parameter
//         * The URL is defined in the second parameter
//         * Then we have a Response Listener and a Error Listener
//         * In response listener we will get the JSON response as a String
//         * */
//        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
//        progressDialog.setMessage("Loading...");
//        progressDialog.show();
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.URL_ITINERARY ,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                        try {
//                            //converting the string to json array object
//                            JSONArray array = new JSONArray(response);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            progressDialog.dismiss();
//                        }
//                        progressDialog.dismiss();
//                    }
//
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                        progressDialog.dismiss();
//                    }
//                });
//
//        //adding our stringrequest to queue
//        Volley.newRequestQueue().add(stringRequest);
//    }
}
