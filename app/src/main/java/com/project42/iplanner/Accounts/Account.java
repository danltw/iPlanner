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

    private String databaseName = "id7469667_iplanner";
    private String tableAccounts = "account";

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
//                            //converti ng the string to json array object
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
//        //Volley.newRequestQueue().add(stringRequest);
//    }
}




//public class asdf extends SQLiteOpenHelper {
//
//    private static final String TAG = SQLiteHandler.class.getSimpleName();
//
//    // All Static variables
//    // Database Version
//    private static final int DATABASE_VERSION = 1;
//
//    // Database Name
//    private static final String DATABASE_NAME = "android_api";
//
//    // Login table name
//    private static final String TABLE_USER = "user";
//
//    // Login Table Columns names
//    private static final String KEY_ID = "id";
//    private static final String KEY_NAME = "name";
//    private static final String KEY_EMAIL = "email";
//    private static final String KEY_UID = "uid";
//    private static final String KEY_CREATED_AT = "created_at";
//
//    public SQLiteHandler(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }
//
//    // Creating Tables
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
//                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
//                + KEY_EMAIL + " TEXT UNIQUE," + KEY_UID + " TEXT,"
//                + KEY_CREATED_AT + " TEXT" + ")";
//        db.execSQL(CREATE_LOGIN_TABLE);
//
//        Log.d(TAG, "Database tables created");
//    }
//
//    // Upgrading database
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        // Drop older table if existed
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
//
//        // Create tables again
//        onCreate(db);
//    }
//
//    /**
//     * Storing user details in database
//     * */
//    public void addUser(String name, String email, String uid, String created_at) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_NAME, name); // Name
//        values.put(KEY_EMAIL, email); // Email
//        values.put(KEY_UID, uid); // Email
//        values.put(KEY_CREATED_AT, created_at); // Created At
//
//        // Inserting Row
//        long id = db.insert(TABLE_USER, null, values);
//        db.close(); // Closing database connection
//
//        Log.d(TAG, "New user inserted into sqlite: " + id);
//    }
//
//    /**
//     * Getting user data from database
//     * */
//    public HashMap<String, String> getUserDetails() {
//        HashMap<String, String> user = new HashMap<String, String>();
//        String selectQuery = "SELECT  * FROM " + TABLE_USER;
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//        // Move to first row
//        cursor.moveToFirst();
//        if (cursor.getCount() > 0) {
//            user.put("name", cursor.getString(1));
//            user.put("email", cursor.getString(2));
//            user.put("uid", cursor.getString(3));
//            user.put("created_at", cursor.getString(4));
//        }
//        cursor.close();
//        db.close();
//        // return user
//        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());
//
//        return user;
//    }
//
//    /**
//     * Re crate database Delete all tables and create them again
//     * */
//    public void deleteUsers() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        // Delete All Rows
//        db.delete(TABLE_USER, null, null);
//        db.close();
//
//        Log.d(TAG, "Deleted all user info from sqlite");
//    }
//
//}