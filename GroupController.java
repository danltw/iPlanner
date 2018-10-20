package com.project42.iplanner.Groups;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project42.iplanner.AppConfig;
import com.project42.iplanner.AppController;
import com.project42.iplanner.Home.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class GroupController {

    private static GroupController mInstance;
    private static Context mCtx;

    private GroupController() {

    }

    public static GroupController getInstance(Context _mCtx) {
        mCtx = _mCtx;
        if (mInstance == null)
            mInstance = new GroupController();
        return mInstance;
    }

    public void addGroup(final Group group) {
        // Tag used to cancel the request
        String tag_string_req = "req_add_group";

        Map<String, String> params = new HashMap<String, String>();
        params.put("grpID", String.valueOf(group.getGroupID()));
        params.put("grpName", group.getGroupName());

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, AppConfig.URL_GROUP, new JSONObject(params), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String success = response.getString("grpID");
                            String failure = response.getString("grpName");
                            Log.d("DATA RESPONSE", success + " " + failure);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            Log.d("JSN RESPONSE", response.toString());
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        error.printStackTrace();
                    }
                }) {
            /*@Override
            protected Map<String, String> getParams() {


                return params;
            }*/
        };


        // Adding request to request queue
        RequestQueue queue = Volley.newRequestQueue(mCtx);
        queue.add(jsonObjectRequest);
    }
}
