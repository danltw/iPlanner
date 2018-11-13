package com.project42.iplanner.Bookmarks;
import com.project42.iplanner.Accounts.LoginActivity;
import com.project42.iplanner.AppConfig;
import com.project42.iplanner.Groups.Group;
import com.project42.iplanner.POIs.POI;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.volley.toolbox.StringRequest;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Toast;

import static android.support.design.widget.BaseTransientBottomBar.LENGTH_LONG;
import static com.android.volley.VolleyLog.TAG;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.project42.iplanner.Utilities.ListUtils;
import com.project42.iplanner.Utilities.SharedManager;
import com.project42.iplanner.Utilities.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.project42.iplanner.R;

/**
 * A simple {@link Fragment} subclass.
 */

public class BookmarkFragment extends Fragment
{
    RecyclerView recyclerView;
    RecyclerViewAdapter mAdapter;
   // ArrayList<String> stringArrayList = new ArrayList<>();
    ArrayList<Bookmark> bookmarkList;
    //Bookmark bookmarkInfo;
    CoordinatorLayout coordinatorLayout;

    //private ProgressDialog pDialog;

    private static final String TAG = BookmarkFragment.class.getSimpleName();
    private static final String URL = AppConfig.URL_BOOKING;

    /*public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }*/


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);
        //View view = getActivity().findViewById()
        //view.setContentView(R.layout.fragment_bookmark);
        //recyclerView = view.findViewById(R.id.recycler_view);

        //View viewMyLayout = inflater.inflate(R.layout.fragment_bookmark, null);
        //setContentView(R.layout.activity_main);
        recyclerView = view.findViewById(R.id.recyclerView);
        coordinatorLayout = view.findViewById(R.id.coordinatorLayout);
        bookmarkList = new ArrayList<>();
        //bookmarkInfo = new Bookmark();
        String userName1 = SharedManager.getInstance().getUser();

        getBookmark(userName1);

        //populateRecyclerView();
        enableSwipeToDeleteAndUndo();

        return view;
    }


    public void getBookmark(final String userName1) {
        // Tag used to cancel the request
        String tag_string_req = "req_get_bookmark";

        StringRequest strReq = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                // Process the JSON
                try {
                    JSONArray jsonResponse = new JSONArray(response);

                    // If no results or some errors occurred, return
                    JSONObject errResponse = jsonResponse.getJSONObject(0);
                    boolean error = errResponse.has("error");
                    if (error) {
                        Log.d("BM Retrieval Error: ", errResponse.getString("error"));
                        return;
                    }
                    else {
                        Log.d(TAG, "Bookmark Retrieval Response: " + response.toString());
                        ArrayList<POI> retrievedPOI = new ArrayList();
                        // If successful: Loop through the array elements
                        for (int i = 0; i < jsonResponse.length(); i++) {
                            JSONObject obj = jsonResponse.getJSONObject(i);

                            //String userName = userName1;
                            int location_id = obj.getInt("POI_id");
                            String location_name = obj.getString("location_name");
                            String location_address = obj.getString("location_address");
                            int BMID = obj.getInt("bookmark_id");

                            /*String grpID = grp.getString("group_id");
                            String groupName = grp.getString("group_name");
                            String memberIDs = grp.getString("member_ids");
                            String adminIDs = grp.getString("admin_ids");*/
                            POI poi = new POI(Integer.valueOf(location_id),
                                    location_name, location_address,0 ,0.0,0.0,null,null,null,null,0.0,0,null);
                            retrievedPOI.add(poi);
                            Bookmark bm1 = new Bookmark(0,null);
                            bm1.setPoi(poi);
                            bm1.setBookmarkID(BMID);
                           bookmarkList.add(bm1);
                        }
                        mAdapter = new RecyclerViewAdapter(getActivity(), retrievedPOI);
                        mAdapter.notifyDataSetChanged();
                        recyclerView.setAdapter(mAdapter);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "BM Retrieval Error: " + error.getMessage());
            }
        }) {
            // method to pass user input to php page
            @Override
            protected Map<String, String> getParams() {
                // Posting params to php page
                Map<String, String> params = new HashMap<String, String>();
                params.put("method", "getBookmark");
                Log.d("method POST", "getBookmark");
                if (userName1 != null)
                    params.put("userName1", String.valueOf(userName1));
                Log.d("Username POST", String.valueOf(userName1));

                return params;
            }

        };

        // Adding request to request queue
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(strReq);
    }

    private void enableSwipeToDeleteAndUndo(){

        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getActivity()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();
                //final String item = mAdapter.getData().get(position);
                //POI item = mAdapter.getData().get(position);

                //mAdapter.removeItem(viewHolder.getAdapterPosition());
                mAdapter.removeItem(position);
                int index = bookmarkList.get(position).getBookmarkID();
                deleteBookmark(index);




                /*Snackbar snackbar = Snackbar.make(coordinatorLayout, "Item was removed from the list.", LENGTH_LONG);

                snackbar.setAction("UNDO", new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view) {
                        //mAdapter.restoreItem(item, position);
                        recyclerView.scrollToPosition(position);

                    }
                });

                snackbar.setActionTextColor(Color.YELLOW).show();
                //snackbar.show();
                //Toast.makeText(getActivity(), "undo", Toast.LENGTH_LONG).show();*/


            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);

    }

    public void deleteBookmark(final int index) {
        // Tag used to cancel the request
        String tag_string_req = "Delete Bookmark";

        StringRequest strReq = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, " Response: " + response.toString());

                // Process the JSON
                try {
                    JSONArray jsonResponse = new JSONArray(response);

                    // If some errors occurred, return
                    JSONObject errResponse = jsonResponse.getJSONObject(0);
                    boolean error = errResponse.has("error");
                    if (error) {
                        Log.d("BM Deletion Error: ", errResponse.getString("error"));
                        return;
                    }
                    else {
                            Toast.makeText(getActivity(), "Bookmark was removed from the list.", LENGTH_LONG).show();

                        // Todo: If successful: display message to inform user that group is deleted
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "BM Deletion Error: " + error.getMessage());
            }
        }) {
            // method to pass user input to php page
            @Override
            protected Map<String, String> getParams() {
                // Posting params to php page
                Map<String, String> params = new HashMap<String, String>();
                if (index >= 0) {
                    try {
                        params.put("method", "deleteBookmark");
                        params.put("index", String.valueOf(index));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return params;
            }

        };

        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(strReq);

    }

}
