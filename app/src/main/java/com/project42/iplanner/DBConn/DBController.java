package com.project42.iplanner.DBConn;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.project42.iplanner.AppConfig;
import com.project42.iplanner.AppController;

import static com.android.volley.VolleyLog.TAG;
//import static com.project42.iplanner.AppController.TAG;

public class DBController {

    private final String username = "root";

    private final String password = "";

    private final String host = "127.0.0.1";

    private final String port = "8080";

    private static DBController mInstance;

    private static Context mCtx;

    public static DBController getInstance(Context _mCtx) {
        if (mInstance == null)
            mInstance = new DBController();

        mCtx = _mCtx;
        return mInstance;
    }

    private DBController() {
    }

    public void connect() {

        // Tag used to cancel the request
        String tag_string_req = "req_login";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.CONN_TESTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response);

                if (!response.equalsIgnoreCase("Connected liao la cb!")) {
                    return;
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
            }
        });

        // Adding request to request queue
        AppController.getInstance(mCtx.getApplicationContext()).addToRequestQueue(strReq);
    }
}
