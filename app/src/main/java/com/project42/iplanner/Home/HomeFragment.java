package com.project42.iplanner.Home;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.project42.iplanner.AppConfig;
import com.project42.iplanner.POIs.POI;
import com.project42.iplanner.POIs.POIAdapter;
import com.project42.iplanner.POIs.POIDetailsFragment;
import com.project42.iplanner.POIs.POISearchFragment;
import com.project42.iplanner.POIs.RecyclerItemClickListener;
import com.project42.iplanner.R;

public class HomeFragment extends Fragment {

    Fragment fragment;

    // Progress Dialog
    private ProgressDialog pDialog;

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    private static final String TAG = HomeFragment.class.getSimpleName();
    private static final String URL = AppConfig.URL_RECOMMENDED;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<POI> poiList;
    private RecyclerView.Adapter adapter;
    private POIAdapter mAdapter;
    Menu menu = null;
    SearchView searchView = null;
    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//         Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        poiList = new ArrayList<>();

        getData();
        Log.d("Response",poiList.toString());
        adapter = new POIAdapter(getActivity(), poiList);

        linearLayoutManager = new LinearLayoutManager(getActivity());

        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        fragment = new POIDetailsFragment();
                        Bundle args = new Bundle();
                        args.putInt("selected_poi_id",poiList.get(position).getLocationID());
                        args.putString("selected_poi_name",poiList.get(position).getLocationName());
                        args.putString("selected_poi_address",poiList.get(position).getAddress());
                        args.putInt("selected_poi_postalcode",poiList.get(position).getPostalCode());
                        args.putDouble("selected_poi_rating",poiList.get(position).getRating());
                        args.putDouble("selected_poi_cost",poiList.get(position).getCost());
                        args.putString("selected_poi_starthrs",poiList.get(position).getStartHrs());
                        args.putString("selected_poi_endhrs",poiList.get(position).getEndHrs());
                        args.putString("selected_poi_openingdays",poiList.get(position).getOpeningDays());
                        if(poiList.get(position).getDescription().equals(""))
                        {
                            poiList.get(position).setDescription("No Description");
                        }
                        args.putString("selected_poi_desc",poiList.get(position).getDescription());
                        args.putDouble("selected_poi_uvi",poiList.get(position).getUVI());
                        args.putDouble("selected_poi_psi",poiList.get(position).getPSI());
                        fragment.setArguments(args);
                        Log.d("Passing Values", args.toString());
                        loadFragment(fragment);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu)
    {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                try {
                    JSONArray array = response.getJSONArray("poi");

                    for(int i=0; i<array.length();i++)
                    {
                        JSONObject jsonObject = array.getJSONObject(i);
                        POI poi = new POI(0,null,null,0,0.0,0.0,null,null,null,null,0.0,0.0);
                        poi.setLocationID(jsonObject.getInt("locationID"));
                        poi.setLocationName(jsonObject.getString("locationName"));
                        poi.setCost(jsonObject.getDouble("cost"));
                        poi.setRating(jsonObject.getDouble("rating"));
                        poiList.add(poi);
                    }
                }
                catch (JSONException e)
                {
                            e.printStackTrace();
                            progressDialog.dismiss();
                }
                    adapter.notifyDataSetChanged();
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_search, menu);
//
//        // Associate searchable configuration with the SearchView
//        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
//        searchView = (SearchView) menu.findItem(R.id.action_search)
//                .getActionView();
//        searchView.setSearchableInfo(searchManager
//                .getSearchableInfo(getActivity().getComponentName()));
//        searchView.setMaxWidth(Integer.MAX_VALUE);
//
//        // listening to search query text change
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                // filter recycler view when query submitted
//                mAdapter.getFilter().filter(query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String query) {
//                // filter recycler view when text is changed
//                mAdapter.getFilter().filter(query);
//                return false;
//            }
//        });
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

//    // Every time when you press search button on keypad an Activity is recreated which in turn calls this function
//    protected void onNewIntent(Intent intent) {
//        // Get search query and create object of class AsyncFetch
//        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//            String query = intent.getStringExtra(SearchManager.QUERY);
//            if (searchView != null) {
//                searchView.clearFocus();
//            }
//            new AsyncFetch(query).execute();
//
//        }
//    }

    private void getData() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++)
                    {
                        JSONObject poi = response.getJSONObject(i);
                        Integer id = poi.getInt("locationID");
                        String name = poi.getString("locationName");
                        String address = poi.getString("address");
                        Integer postalcode = poi.getInt("postalCode");
                        Double rating = poi.getDouble("rating");
                        Double cost = poi.getDouble("cost");
                        String startHrs = poi.getString("startHrs");
                        String endHrs = poi.getString("endHrs");
                        String open = poi.getString("openingDays");
                        String desc = poi.getString("description");
                        Double uvi = poi.getDouble("UVI");
                        Double psi = poi.getDouble("PSI");
                        POI poiObject = new POI(id,name,address,postalcode,rating,cost,startHrs,endHrs,open,desc,uvi,psi);
                        poiList.add(poiObject);
                        Log.d("JSON Data", poi.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
                adapter.notifyDataSetChanged();
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
        requestQueue.add(jsonArrayRequest);
    }
//
//    // Create class AsyncFetch
//    private class AsyncFetch extends AsyncTask<String, String, String> {
//
//        ProgressDialog pdLoading = new ProgressDialog(getActivity());
//        HttpURLConnection conn;
//        URL url = null;
//        String searchQuery;
//
//        public AsyncFetch(String searchQuery){
//            this.searchQuery=searchQuery;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            //this method will be running on UI thread
//            pdLoading.setMessage("\tLoading...");
//            pdLoading.setCancelable(false);
//            pdLoading.show();
//
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            try {
//
//                // Enter URL address where your php file resides
//                url = new URL("http://project42-iplanner.000webhostapp.com/get_search_poi.php");
//
//            } catch (MalformedURLException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//                return e.toString();
//            }
//            try {
//
//                // Setup HttpURLConnection class to send and receive data from php and mysql
//                conn = (HttpURLConnection) url.openConnection();
//                conn.setReadTimeout(READ_TIMEOUT);
//                conn.setConnectTimeout(CONNECTION_TIMEOUT);
//                conn.setRequestMethod("POST");
//
//                // setDoInput and setDoOutput to true as we send and recieve data
//                conn.setDoInput(true);
//                conn.setDoOutput(true);
//
//                // add parameter to our above url
//                Uri.Builder builder = new Uri.Builder().appendQueryParameter("searchQuery", searchQuery);
//                String query = builder.build().getEncodedQuery();
//
//                OutputStream os = conn.getOutputStream();
//                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
//                writer.write(query);
//                writer.flush();
//                writer.close();
//                os.close();
//                conn.connect();
//
//            } catch (IOException e1) {
//                // TODO Auto-generated catch block
//                e1.printStackTrace();
//                return e1.toString();
//            }
//
//            try {
//
//                int response_code = conn.getResponseCode();
//
//                // Check if successful connection made
//                if (response_code == HttpURLConnection.HTTP_OK) {
//
//                    // Read data sent from server
//                    InputStream input = conn.getInputStream();
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
//                    StringBuilder result = new StringBuilder();
//                    String line;
//
//                    while ((line = reader.readLine()) != null) {
//                        result.append(line);
//                    }
//
//                    // Pass data to onPostExecute method
//                    return (result.toString());
//
//                } else {
//                    return("Connection error");
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//                return e.toString();
//            } finally {
//                conn.disconnect();
//            }
//
//
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//
//            //this method will be running on UI thread
//            pdLoading.dismiss();
//            List<POI> data=new ArrayList<>();
//
//            pdLoading.dismiss();
//            if(result.equals("no rows")) {
//                adapter.notifyDataSetChanged();
//                pdLoading.dismiss();
//                Toast.makeText(getActivity(), "No Results found for entered query", Toast.LENGTH_LONG).show();
//            }else{
//
//                try {
//
//                    JSONArray jArray = new JSONArray(result);
//
//                    // Extract data from json and store into ArrayList as class objects
//                    for (int i = 0; i < jArray.length(); i++) {
//                        JSONObject json_data = jArray.getJSONObject(i);
//                        POI poiData = new POI(0,null,null,0,0.0,0.0,null,null,null,null,0.0,0.0);
//                        poiData.setLocationID(json_data.getInt("location_id"));
//                        poiData.setLocationName(json_data.getString("location_name"));
//                        poiData.setCost(json_data.getDouble("location_cost"));
//                        poiData.setRating(json_data.getDouble("location_rating"));
//                        data.add(poiData);
//                    }
//
//                    adapter.notifyDataSetChanged();
//                    pdLoading.dismiss();
//
//                } catch (JSONException e) {
//                    // You to understand what actually error is and handle it appropriately
//                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
//                    Toast.makeText(getActivity(), result.toString(), Toast.LENGTH_LONG).show();
//                }
//
//            }
//
//        }
//
//    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}