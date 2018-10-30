package com.project42.iplanner.Bookmarks;
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
import static android.support.design.widget.BaseTransientBottomBar.LENGTH_LONG;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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
    ArrayList<POI> bookmarkList;
    CoordinatorLayout coordinatorLayout;

    //private ProgressDialog pDialog;

    private static final String TAG = BookmarkFragment.class.getSimpleName();
    private static final String URL = "http://project42-iplanner.000webhostapp.com/bookmark.php";

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
        String userName1 = SharedManager.getInstance(getActivity()).getUser();
        getData(userName1);

        //populateRecyclerView();
        enableSwipeToDeleteAndUndo();

        return view;
    }

    private void getData(String userName)
    {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        //final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
        final StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>()  {
            @Override
            public void onResponse(String response)
            {
                try {
                    Log.d("Bookmark Response", response);
                    //JSONObject obj = response;

                    /*JSONObject obj = response;
                    String bmID = obj.get("bookmark_id").toString();
                    String locName = obj.get("location_name").toString();
                    String userID = obj.get("location_name").toString();
                    Log.d("Data: ", bmID + " " + locName + " " + userID);*/

                    JSONArray array = new JSONArray(response);
                    //JSONArray array = (JSONArray) response;
                    for(int i=0; i< array.length();i++)
                    {
                        JSONObject jsonObject = array.getJSONObject(i);
                        POI bookmark1 = new POI(0,null,null,0,0.0,0,null,null,null,null, 0.0, 0.0);

                        int location_id = jsonObject.getInt("POI_id");
                        String location_name = jsonObject.getString("location_name");
                        String location_address = jsonObject.getString("location_address");
                        //int location_postalCode = jsonObject.getInt("location_postalcode");
                        /*double rating = jsonObject.getDouble("location_rating");
                        double cost = jsonObject.getDouble("location_cost");
                        String start_hrs = jsonObject.getString("start_hrs");
                        String end_hrs = jsonObject.getString("end_hrs");
                        String opening_days = jsonObject.getString("opening_days");

                        double uvi = jsonObject.getDouble("UVI");
                        double psi = jsonObject.getDouble("PSI");*/
                        //bookmark1.setPoi(new POI(location_id,location_name,location_address,location_postalCode,rating,cost,start_hrs,end_hrs,opening_days,location_desc,uvi,psi));

                        //String location_desc = jsonObject.getString("location_desc");
                        bookmark1.setLocationID(location_id);
                        bookmark1.setLocationName(location_name);
                        bookmark1.setAddress(location_address);
                        bookmark1.setPostalCode(0);
                        bookmark1.setRating(0);
                        bookmark1.setCost(0);
                        bookmark1.setStartHrs(null);
                        bookmark1.setEndHrs(null);
                        bookmark1.setOpeningDays(null);
                        bookmark1.setDescription(null);
                        bookmark1.setUVI(0);
                        bookmark1.setPSI(0);

                        bookmarkList.add(bookmark1);
                    }
                    RecyclerViewAdapter adapter1 = new RecyclerViewAdapter(getActivity(), bookmarkList);
                    recyclerView.setAdapter(adapter1);

                }

                catch (JSONException e)
                {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
                //mAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressDialog.dismiss();
            }
        }) {
            // method to pass user input to php page
            @Override
            protected Map<String, String> getParams() {
                // Posting params to php page
                Map<String, String> params = new HashMap<String, String>();
                if (!TextUtils.isEmpty(userName)) {
                    try {
                        params.put("method", "getBookmark");
                        params.put("userName", String.valueOf(userName));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonObjectRequest);
    }

    private void enableSwipeToDeleteAndUndo(){

        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getActivity()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();
                //final String item = mAdapter.getData().get(position);
                POI item = mAdapter.getData().get(position);


                mAdapter.removeItem(viewHolder.getAdapterPosition());

                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Item was removed from the list.", LENGTH_LONG);

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
                //Toast.makeText(getActivity(), "undo", Toast.LENGTH_LONG).show();
                deleteBookmark(position);

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);



    }

    public void deleteBookmark(final int index) {
        // Tag used to cancel the request
        String tag_string_req = "Delete Bookmark";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                "http://project42-iplanner.000webhostapp.com/bookmark.php", new Response.Listener<String>() {

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
