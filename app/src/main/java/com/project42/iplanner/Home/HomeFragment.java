package com.project42.iplanner.Home;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintDocumentAdapter;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private RadioGroup sortGrp;

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    private static final String TAG = HomeFragment.class.getSimpleName();
    private static final String URL = AppConfig.URL_RECOMMENDED;
    private static final String URL_UVI = AppConfig.URL_UVI;
    private static final String URL_PSI = AppConfig.URL_PSI;

    private Double currentpsi;
    private Double currentuvi;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<POI> poiList;
    private RecyclerView.Adapter adapter;
    private POIAdapter mAdapter;
    LinearLayout linearLayout1;
    Menu menu = null;
    SearchView searchView = null;
    private PopupWindow mPopupWindow;
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
        linearLayout1 = (LinearLayout) view.findViewById(R.id.linear_layout1);
        recyclerView = view.findViewById(R.id.recycler_view);
        poiList = new ArrayList<>();
        currentuvi = 0.0;
        currentpsi = 0.0;
        getAllUVI();
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

        ImageButton sortBtn = (ImageButton) view.findViewById(R.id.floatingActionButtonSort);
        ImageButton filterBtn = (ImageButton) view.findViewById(R.id.floatingActionButtonFilter);

        sortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater1 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                // Inflate the custom layout/view
                View customView = inflater1.inflate(R.layout.sort_layout,null);
                mPopupWindow = new PopupWindow(customView,ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                mPopupWindow.showAtLocation(linearLayout1,Gravity.CENTER,0,0);
                if(Build.VERSION.SDK_INT>=21){
                    mPopupWindow.setElevation(5.0f);
                }

                sortGrp = (RadioGroup) customView.findViewById(R.id.sortGrp);

                final RadioButton atoz = (RadioButton) customView.findViewById(R.id.atoz);
                final RadioButton ztoa = (RadioButton) customView.findViewById(R.id.ztoa);
                final RadioButton costup = (RadioButton) customView.findViewById(R.id.costup);
                final RadioButton costdown = (RadioButton) customView.findViewById(R.id.costdown);

                Button applyBtn = (Button) customView.findViewById(R.id.sort_apply);

                applyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int selectedId = sortGrp.getCheckedRadioButtonId();
                        String sortType = "default";
                        if(selectedId == atoz.getId())
                        {
                            sortType = "atoz";
                            sortList(poiList,sortType);
                            mPopupWindow.dismiss();
                        }
                        else if(selectedId == ztoa.getId())
                        {
                            sortType = "ztoa";
                            sortList(poiList,sortType);
                            mPopupWindow.dismiss();
                        }
                        else if(selectedId == costup.getId())
                        {
                            sortType = "costup";
                            sortList(poiList,sortType);
                            mPopupWindow.dismiss();
                        }
                        else if(selectedId == costdown.getId())
                        {
                            sortType = "costdown";
                            sortList(poiList,sortType);
                            mPopupWindow.dismiss();
                        }
                        else
                        {
                            sortType = "default";
                            sortList(poiList,sortType);
                            mPopupWindow.dismiss();
                        }
                    }
                });

                Button cancelBtn = (Button) customView.findViewById(R.id.sort_cancel);
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPopupWindow.dismiss();
                    }
                });
            }
        });
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }


    private void getData(Double cuvi, Double cpsi) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>()
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
                params.put("uv_index", cuvi.toString());
                Log.d("POST UV", cuvi.toString());
                params.put("ps_index", cpsi.toString());
                Log.d("POST PSI", cpsi.toString());
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

    public void sortList(List<POI> unsortedList, String type)
    {
        if(type.equals("atoz"))
        {
            Collections.sort(unsortedList, new Comparator<POI>() {
                @Override
                public int compare(POI e1, POI e2) {
                    String name1 = ((POI) e1).getLocationName();
                    String name2 = ((POI) e2).getLocationName();

                    // ascending order
                    return name1.compareTo(name2);
                }
            });
            poiList = unsortedList;
            adapter.notifyDataSetChanged();
        }
        else if(type.equals("ztoa"))
        {
            Collections.sort(unsortedList, new Comparator<POI>() {
                @Override
                public int compare(POI e1, POI e2) {
                    String name1 = ((POI) e1).getLocationName();
                    String name2 = ((POI) e2).getLocationName();
                    return name2.compareTo(name1);
                }
            });
            poiList = unsortedList;
            adapter.notifyDataSetChanged();
        }
        else if(type.equals("costup"))
        {
            Collections.sort(unsortedList, new Comparator<POI>() {
                @Override
                public int compare(POI e1, POI e2) {
                    Double c1 = ((POI) e1).getCost();
                    Double c2 = ((POI) e2).getCost();

                    // ascending order
                    return c1.compareTo(c2);

                    // descending order
                    //return id2.compareTo(id1);
                }
            });
            poiList = unsortedList;
            adapter.notifyDataSetChanged();
        }
        else if(type.equals("costdown"))
        {
            Collections.sort(unsortedList, new Comparator<POI>() {
                @Override
                public int compare(POI e1, POI e2) {
                    Double c1 = ((POI) e1).getCost();
                    Double c2 = ((POI) e2).getCost();

                    return c2.compareTo(c1);
                }
            });
            poiList = unsortedList;
            adapter.notifyDataSetChanged();
        }
        else {
            poiList = unsortedList;
            adapter.notifyDataSetChanged();
        }
    }

    private void getAllUVI()
    {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(now);
        Log.d("Date", dateStr);
        Log.d("URL", URL_UVI+dateStr);
        //String URL = URL_PSI + sdf.format(dateStr).toString();
        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_UVI+dateStr,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            List<Double> uviList = new ArrayList<>();
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            //we have the array named items inside the object
                            //so here we are getting that json array
                            JSONArray itemsArray = obj.getJSONArray("items");
                            Log.d("Items Array", itemsArray.toString());
                            for (int i = 0; i < itemsArray.length(); i++) {
                                JSONObject itemsObj = itemsArray.getJSONObject(i);
                                JSONArray indexArray = itemsObj.getJSONArray("index");
                                Log.d("Index Array", indexArray.get(i).toString());
                                for(int k = 0; k < indexArray.length(); k++)
                                {
                                    JSONObject indexObject = indexArray.getJSONObject(k);
                                    Double uv = indexObject.getDouble("value");
                                    uviList.add(uv);
                                }
                            }
                            currentuvi = uviList.get(uviList.size()-1);
                            getAllPSI();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.toString());
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }

    private void getAllPSI()
    {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(now);
        Log.d("Date", dateStr);
        Log.d("URL", URL_PSI+dateStr);
        //String URL = URL_PSI + sdf.format(dateStr).toString();
        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_PSI+dateStr,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            List<Double> psiList = new ArrayList<>();
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            //we have the array named items inside the object
                            //so here we are getting that json array
                            JSONArray itemsArray = obj.getJSONArray("items");
                            Log.d("Items Array", itemsArray.toString());
                            for (int i = 0; i < itemsArray.length(); i++) {
                                JSONObject itemsObj = itemsArray.getJSONObject(i);
                                JSONObject readingsObj = itemsObj.getJSONObject("readings");
                                for(int k = 0; k < readingsObj.length(); k++)
                                {
                                    JSONObject psiObject = readingsObj.getJSONObject("psi_twenty_four_hourly");
//                                    Double n_psi = psiObject.getDouble("north");
//                                    Double s_psi = psiObject.getDouble("south");
//                                    Double e_psi = psiObject.getDouble("east");
//                                    Double w_psi = psiObject.getDouble("west");
//                                    Double cen_psi = psiObject.getDouble("central");
                                    Double nat_psi = psiObject.getDouble("national");
                                    psiList.add(nat_psi);
                                }
                            }
                            currentpsi = psiList.get(psiList.size()-1);
                            getData(currentuvi,currentpsi);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.toString());
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }
}