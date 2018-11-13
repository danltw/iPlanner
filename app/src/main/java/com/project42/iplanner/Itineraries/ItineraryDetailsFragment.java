package com.project42.iplanner.Itineraries;


import android.app.ProgressDialog;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project42.iplanner.AppConfig;
import com.project42.iplanner.POIs.POIAdapter;
import com.project42.iplanner.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ItineraryDetailsFragment extends Fragment {
    Fragment detailsFragment;
    private  View view;
    private RecyclerView mRecyclerView;
    private TimeLineAdapter mTimeLineAdapter;
    private List<Timeline> mDataList = new ArrayList<>();

    int id,locationID;
    String name,date,poiId,time,locationName;
    private LinearLayoutManager linearLayoutManager;
    //private FrameLayoutManager FrameLayout;
    private DividerItemDecoration dividerItemDecoration;

    public ItineraryDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_itinerary_details, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        ButterKnife.bind(this,view);

        mRecyclerView =  view.findViewById(R.id.itineraryDetailsRecycler);
        if(((AppCompatActivity)getActivity()).getSupportActionBar()!=null)
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        List<ItineraryFragment> your_array_list = new ArrayList<ItineraryFragment>();


        mTimeLineAdapter = new TimeLineAdapter(getActivity(), mDataList);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), linearLayoutManager.getOrientation());

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setAdapter(mTimeLineAdapter);


        Bundle args = this.getArguments();
        if (args != null) {
          id = args.getInt("selected_itinerary_id");
          name =  args.getString("selected_itinerary_name");
          date =   args.getString("selected_visit_date");
          poiId =   args.getString("selected_POI_id");
          time =   args.getString("selected_visit_time");
        }


        //String[] SinglePOIid = poiId.split(",");
        String[] SingleTime = time.split(",");

       // initView();
        loadItinerary();
        mRecyclerView.setAdapter(mTimeLineAdapter);
        return view;
    }



    private void loadFragment(Fragment fragment)
    {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void loadItinerary() {
         List<Timeline> mDataList = new ArrayList<>();
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



        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_ITINERARYDETAIL,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.d("Return response", response);
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            String[] SingleTime = time.split(",");
                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject result = array.getJSONObject(i);

                               int id = result.getInt("location_id");
                               String name = result.getString("location_name");
                               String Single =SingleTime[i];



                                Log.d("Return name", name);
                                //Timeline timelineObject = new Timeline(id,name);

                                //adding the product to product list
                                mDataList.add(new Timeline(id,name,Single));

                            }


                            //creating adapter object and setting it to recyclerview

                            TimeLineAdapter mTimeLineAdapter = new TimeLineAdapter(getActivity(), mDataList);
                            mTimeLineAdapter.notifyDataSetChanged();
                            mRecyclerView.setAdapter(mTimeLineAdapter);


                        } catch (JSONException e) {
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
        @Override
        protected Map<String, String> getParams(){

            // Creating Map String Params.
            Map<String, String> params = new HashMap<String, String>();

            // Adding All values to Params.
            params.put("locationID", poiId.toString());
            Log.d("POST LOC ID",poiId);

            return params;
        }
    };

        //adding our stringrequest to queue
        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }




    private void initView() {
      //  setDataListItems();
        mTimeLineAdapter = new TimeLineAdapter(getActivity(),mDataList);
        mRecyclerView.setAdapter(mTimeLineAdapter);
    }

//    private void setDataListItems(){
//
//        DateFormat Dateformatter = new SimpleDateFormat("yyyy-mm-dd");
//        Date date = null;
//        try {
//            date = Dateformatter.parse("2018-01-02");
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        mDataList.add(new Timeline(id, name,name)
//
//        );
       // mDataList.add(new POI(poiId, name,address,postalcode,rating,cost,startHrs,endHrs,open,desc,uvi,psi));


    //}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Menu
        switch (item.getItemId()) {
            //When home is clicked
            case android.R.id.home:
                //onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }








}
