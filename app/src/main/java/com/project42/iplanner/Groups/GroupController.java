package com.project42.iplanner.Groups;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project42.iplanner.AppConfig;
import com.project42.iplanner.Utilities.ListUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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

    // Get a single / multiple group records
    public void getGroup(final Group group) {
        // Tag used to cancel the request
        String tag_string_req = "req_get_group";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GROUP, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Group Retrieval Response: " + response.toString());
                ArrayList<Group> retrievedGroups = new ArrayList();

                // Process the JSON
                try {
                    JSONArray jsonResponse = new JSONArray(response);

                    // If no results or some errors occurred, return
                    JSONObject errResponse = jsonResponse.getJSONObject(0);
                    boolean error = errResponse.has("error");
                    if (error) {
                        Log.d("Group Retrieval Error: ", errResponse.getString("error"));
                        return;
                    }
                    else {
                        // If successful: Loop through the array elements
                        for (int i = 0; i < jsonResponse.length(); i++) {
                            JSONObject grp = jsonResponse.getJSONObject(i);
                            String grpID = grp.getString("group_id");
                            String groupName = grp.getString("group_name");
                            String memberIDs = grp.getString("member_ids");
                            String adminIDs = grp.getString("admin_ids");
                            Group group = new Group(Integer.valueOf(grpID), groupName,
                                    ListUtils.parseString(memberIDs), ListUtils.parseString(adminIDs));
                            retrievedGroups.add(group);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Group Retrieval Error: " + error.getMessage());
            }
        }) {
            // method to pass user input to php page
            @Override
            protected Map<String, String> getParams() {
                // Posting params to php page
                Map<String, String> params = new HashMap<String, String>();
                params.put("method", "getGroup");
                if (group != null)
                    params.put("grpName", group.getGroupName());
                return params;
            }

        };

        // Adding request to request queue
        RequestQueue queue = Volley.newRequestQueue(mCtx);
        queue.add(strReq);
    }

    // Add a single group record
    public void addGroup(final Group group) {
        // Tag used to cancel the request
        String tag_string_req = "req_add_group";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GROUP, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Group Insertion Response: " + response.toString());

                // Process the JSON
                try {
                    JSONArray jsonResponse = new JSONArray(response);

                    // If some errors occurred, return
                    JSONObject errResponse = jsonResponse.getJSONObject(0);
                    boolean error = errResponse.has("error");
                    if (error) {
                        Log.d("Group Insertion Error: ", errResponse.getString("error"));
                        return;
                    }
                    else {
                        // Todo: If successful: display message to inform user that group is created
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Group Insertion Error: " + error.getMessage());
            }
        }) {
            // method to pass user input to php page
            @Override
            protected Map<String, String> getParams() {
                // Posting params to php page
                Map<String, String> params = new HashMap<String, String>();
                if (group != null) {
                    try {
                        params.put("method", "addGroup");
                        params.put("grpName", group.getGroupName());
                        params.put("memberIDs", ListUtils.parseIntList(group.getMembers()));
                        params.put("adminIDs", ListUtils.parseIntList(group.getAdmins()));
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

    // Update a single group record
    public void updateGroup(final Group group) {
        // Tag used to cancel the request
        String tag_string_req = "req_update_group";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GROUP, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Group Update Response: " + response.toString());

                // Process the JSON
                try {
                    JSONArray jsonResponse = new JSONArray(response);

                    // If some errors occurred, return
                    JSONObject errResponse = jsonResponse.getJSONObject(0);
                    boolean error = errResponse.has("error");
                    if (error) {
                        Log.d("Group Update Error: ", errResponse.getString("error"));
                        return;
                    }
                    else {
                        // Todo: If successful: display message to inform user that group is updated
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Group Update Error: " + error.getMessage());
            }
        }) {
            // method to pass user input to php page
            @Override
            protected Map<String, String> getParams() {
                // Posting params to php page
                Map<String, String> params = new HashMap<String, String>();
                if (group != null) {
                    try {
                        params.put("method", "updateGroup");
                        params.put("grpID", String.valueOf(group.getGroupID()));
                        params.put("grpName", group.getGroupName());
                        params.put("memberIDs", ListUtils.parseIntList(group.getMembers()));
                        params.put("adminIDs", ListUtils.parseIntList(group.getAdmins()));
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

    public void deleteGroup(final int grpID) {
        // Tag used to cancel the request
        String tag_string_req = "req_delete_group";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GROUP, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Group Deletion Response: " + response.toString());

                // Process the JSON
                try {
                    JSONArray jsonResponse = new JSONArray(response);

                    // If some errors occurred, return
                    JSONObject errResponse = jsonResponse.getJSONObject(0);
                    boolean error = errResponse.has("error");
                    if (error) {
                        Log.d("Group Deletion Error: ", errResponse.getString("error"));
                        return;
                    }
                    else {
                        // Todo: If successful: display message to inform user that group is deleted
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Group Deletion Error: " + error.getMessage());
            }
        }) {
            // method to pass user input to php page
            @Override
            protected Map<String, String> getParams() {
                // Posting params to php page
                Map<String, String> params = new HashMap<String, String>();
                if (grpID >= 0) {
                    try {
                        params.put("method", "deleteGroup");
                        params.put("grpID", String.valueOf(grpID));
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
}
