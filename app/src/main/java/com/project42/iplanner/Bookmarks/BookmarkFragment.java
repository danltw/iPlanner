package com.project42.iplanner.Bookmarks;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.project42.iplanner.POIs.POI;
import com.project42.iplanner.R;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;




/**
 * A simple {@link Fragment} subclass.
 */

public class BookmarkFragment extends Fragment
{
    RecyclerView recyclerView;
    RecyclerViewAdapter mAdapter;
   // ArrayList<String> stringArrayList = new ArrayList<>();
    List<Bookmark> bookmarkList;
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

        getData();

        //populateRecyclerView();
        enableSwipeToDeleteAndUndo();

        return view;
    }

    private void getData()
    {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                try {
                    JSONArray array = response.getJSONArray("bookmark");

                    for(int i=0; i<array.length();i++)
                    {
                        JSONObject jsonObject = array.getJSONObject(i);
                        Bookmark bookmark1 = new Bookmark(0, null);
                        //poi.setLocationID(jsonObject.getInt("locationID"));
                        bookmark1.setBookmarkID(jsonObject.getInt("BookmarkID"));
                        /*bookmark1.setPoi(new POI(jsonObject.getInt("location_id"),jsonObject.getString("location_name"),jsonObject.getString("location_address")
                                ,jsonObject.getInt("location_postalcode"),jsonObject.getDouble("location_rating"),jsonObject.getDouble("location_cost")
                                ,jsonObject.getString("start_hrs"),jsonObject.getString("end_hrs"),jsonObject.getString("opening_days")
                                ,jsonObject.getString("location_desc") ,jsonObject.getDouble("UVI"),jsonObject.getDouble("PSI")));*/
                        int location_id = jsonObject.getInt("location_id");
                        String location_name = jsonObject.getString("location_name");
                        String location_address = jsonObject.getString("location_address");
                        int location_postalCode = jsonObject.getInt("location_postalcode");
                        double rating = jsonObject.getDouble("location_rating");
                        double cost = jsonObject.getDouble("location_cost");
                        String start_hrs = jsonObject.getString("start_hrs");
                        String end_hrs = jsonObject.getString("end_hrs");
                        String opening_days = jsonObject.getString("opening_days");
                        String location_desc = jsonObject.getString("location_desc");
                        double uvi = jsonObject.getDouble("UVI");
                        double psi = jsonObject.getDouble("PSI");
                        bookmark1.setPoi(new POI(location_id,location_name,location_address,location_postalCode,rating,cost,start_hrs,end_hrs,opening_days,location_desc,uvi,psi));

                        bookmarkList.add(bookmark1);
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
                mAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressDialog.dismiss();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonObjectRequest);
    }


/*    private void populateRecyclerView() {
        stringArrayList.add("Item 1");
        stringArrayList.add("Item 2");
        stringArrayList.add("Item 3");
        stringArrayList.add("Item 4");
        stringArrayList.add("Item 5");
        stringArrayList.add("Item 6");
        stringArrayList.add("Item 7");
        stringArrayList.add("Item 8");
        stringArrayList.add("Item 9");
        stringArrayList.add("Item 10");

        mAdapter = new RecyclerViewAdapter(stringArrayList);
        recyclerView.setAdapter(mAdapter);


    }*/

    private void enableSwipeToDeleteAndUndo() {

        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getActivity()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();
                final String item = mAdapter.getData().get(position);

                mAdapter.removeItem(position);

                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Item was removed from the list.", LENGTH_LONG);

                snackbar.setAction("UNDO", new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view) {
                        mAdapter.restoreItem(item, position);
                        recyclerView.scrollToPosition(position);

                    }
                });

                snackbar.setActionTextColor(Color.YELLOW).show();
                //snackbar.show();
                //Toast.makeText(getActivity(), "undo", Toast.LENGTH_LONG).show();


            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }


}
