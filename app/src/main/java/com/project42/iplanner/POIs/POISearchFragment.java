package com.project42.iplanner.POIs;


import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project42.iplanner.AppConfig;
import com.project42.iplanner.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class POISearchFragment extends Fragment {

    Fragment fragment;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<POI> poiList;
    private RecyclerView.Adapter adapter;

    public POISearchFragment() {
        // Required empty public constructor
    }

    public static POISearchFragment newInstance(String param1, String param2) {
        POISearchFragment fragment = new POISearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_poisearch, container, false);
        recyclerView = view.findViewById(R.id.search_recyclerview);
        poiList = new ArrayList<>();
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
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("onQueryTextChange", newText);
                    return true;
                }
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);
                    search(query);
                    Log.d("list", poiList.toString());
                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // Not implemented here
                return false;
            default:
                break;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }

    private void search(String query) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_SEARCH, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try {
                    JSONArray poiarray = new JSONArray(response);
                    for (int i = 0; i < response.length(); i++)
                    {
                        JSONObject poi = poiarray.getJSONObject(i);
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
        }){
            @Override
            protected Map<String,String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("query", query);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
