package com.project42.iplanner.Itineraries;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project42.iplanner.AppConfig;
import com.project42.iplanner.POIs.POI;
import com.project42.iplanner.POIs.POIAdapter;
import com.project42.iplanner.POIs.POIDetailsFragment;
import com.project42.iplanner.POIs.RecyclerItemClickListener;
import com.project42.iplanner.R;
import com.project42.iplanner.Utilities.SharedManager;
import com.project42.iplanner.Utilities.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import android.app.ActionBar;


/**
 * A simple {@link Fragment} subclass.
 */
public class ItineraryFragment extends Fragment {

    Fragment fragment;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<Itinerary> itineraryList;
    private RecyclerView.Adapter adapter;
    public ItineraryFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_itinerary, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);



        ButterKnife.bind(this,view);

        recyclerView = view.findViewById(R.id.itinerary_recylcerView);
        itineraryList = new ArrayList<>();

        //loadItinerary();
        Log.d("Response",itineraryList.toString());
        adapter = new ItineraryAdapter(getActivity(), itineraryList);

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
                        fragment = new ItineraryDetailsFragment();
                        Bundle args = new Bundle();
                        args.putInt("selected_itinerary_id",itineraryList.get(position).getItineraryID());
                        args.putString("selected_itinerary_name",itineraryList.get(position).getItineraryName());
                        args.putString("selected_visit_date",itineraryList.get(position).getVisitDate().toString());
                        args.putString("selected_POI_id",itineraryList.get(position).getPOIID());
                        args.putString("selected_visit_time",itineraryList.get(position).getVisitTime().toString());
                        fragment.setArguments(args);
                        Log.d("Passing Values", args.toString());
                        loadFragment(fragment);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
        String userName = SharedManager.getInstance().getUser();
        loadItinerary(userName);
        return view;
    }
    private void loadFragment(Fragment fragment)
    {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void loadItinerary(String userName) {

        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_ITINERARY ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Return response", response);
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);


                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject result = array.getJSONObject(i);

                                String dateStr = result.getString("visit_date");
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                Date birthDate = sdf.parse(dateStr);




                                //adding the product to product list
                                itineraryList.add(new Itinerary(
                                        result.getInt("itinerary_id"),
                                        result.getString("POI_id"),
                                        result.getString("itinerary_name"),
                                        birthDate,
                                        result.getString("visit_time")
                                        // result.getString("created").toString()
//                                        product.getDouble("rating"),
//                                        product.getDouble("price"),
//                                        product.getString("image")
                                ));

                            }




                            //creating adapter object and setting it to recyclerview
                            ItineraryAdapter adapter = new ItineraryAdapter(getActivity(), itineraryList);
                            recyclerView.setAdapter(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        } catch (ParseException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                        progressDialog.dismiss();
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressDialog.dismiss();
                    }
                })

        {
            // method to pass user input to php page
            @Override
            protected Map<String, String> getParams() {
                // Posting params to php page
                Map<String, String> params = new HashMap<String, String>();
                if (userName != null) {
                    try {
                        params.put("userName", String.valueOf(userName));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return params;
            }}
            ;

        //adding our stringrequest to queue
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }


}
