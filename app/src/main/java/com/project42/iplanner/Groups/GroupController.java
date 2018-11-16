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
import com.project42.iplanner.Utilities.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

/** Represents a group controller.
 * @author Team42
 * @version 1.0
 */
public class GroupController {

    private static GroupController mInstance;
    private static Context mCtx;

    private GroupController() {

    }

    /** Obtains the group controller singleton reference by passing it the current activity to be used in.
     * If the _mCtx parameter is null, the current context of this class will be used instead.
     * @param _mCtx The current activity for the group controller to be used in.
     * @return The current instance of the group controller singleton class
     */
    public static GroupController getInstance(Context _mCtx) {
        if (mInstance == null) {
            mInstance = new GroupController();
        }
        if (_mCtx != null)
            mCtx = _mCtx;
        return mInstance;
    }

    /** The method to delete a group.
     * Deletion of a group requires its ID property.
     * @param grpID The ID of the group which is to be deleted.
     */
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

    /** The method to get either a group or groups.
     * Retrieval of a group or groups requires the group object.
     * @param group The group object with its groupName property set.
     */
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

    /** The method to create a group.
     * Creation of a new group requires all the parameters to be non-empty.
     * @param groupName The group name as entered by the user.
     * @param channelUrl A string of randomly generated characters that is used to distinguish each group chat channel, as assigned by Sendbird API.
     * @param memberNames A list of member's names required to track members in each group.
     * @param adminNames A list of admin's names required to track admins in each group.
     */
    // Add a single group record
    public void addGroup(final String groupName, final String channelUrl, final ArrayList<String> memberNames, final ArrayList<String> adminNames) {
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
                if (!TextUtils.isEmpty(groupName) && !TextUtils.isEmpty(channelUrl) && memberNames != null &&
                        adminNames != null) {
                    try {
                        params.put("method", "addGroup");
                        params.put("grpUrl", channelUrl);
                        params.put("grpName", groupName);
                        params.put("memberNames", ListUtils.getStrNames(memberNames));
                        params.put("adminNames", ListUtils.getStrNames(adminNames));
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

    /** The method to add new members or admins to a group.
     * This method accepts the updated list of member's or admin's names, depending on which role is to be added.
     * Adding of new members or admins requires all the parameters to be non-empty.
     * @param channelUrl A string of randomly generated characters that is used to distinguish each group chat channel, as assigned by Sendbird API.
     * @param memberNames A list of member's names required to track members in each group.
     * @param adminNames A list of admin's names required to track admins in each group.
     */
    // Update a single group record
    public void updateGroup(final String channelUrl, final ArrayList<String> memberNames, final ArrayList<String> adminNames) {
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
                if (!TextUtils.isEmpty(channelUrl) && memberNames != null &&
                        adminNames != null) {
                    try {
                        params.put("method", "updateGroup");
                        params.put("grpUrl", channelUrl);
                        params.put("memberNames", ListUtils.getStrNames(memberNames));
                        params.put("adminNames", ListUtils.getStrNames(adminNames));
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
