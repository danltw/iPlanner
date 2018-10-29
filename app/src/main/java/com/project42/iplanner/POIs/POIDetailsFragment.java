package com.project42.iplanner.POIs;


import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Rating;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project42.iplanner.AppConfig;
import com.project42.iplanner.Itineraries.ItineraryDetailsActivity;
import com.project42.iplanner.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class POIDetailsFragment extends Fragment {



    public POIDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_poidetails, container, false);
        TextView id = (TextView) view.findViewById(R.id.poi_details_id);
        TextView name = (TextView) view.findViewById(R.id.poi_details_name);
        TextView address = (TextView) view.findViewById(R.id.poi_details_address);
        TextView postal = (TextView) view.findViewById(R.id.poi_details_postal);
        TextView cost = (TextView) view.findViewById(R.id.poi_details_cost);
        RatingBar rating = (RatingBar) view.findViewById(R.id.poi_details_rating);
        TextView desc = (TextView) view.findViewById(R.id.poi_details_desc);
        TextView open = (TextView) view.findViewById(R.id.poi_details_start);
        TextView close = (TextView) view.findViewById(R.id.poi_details_end);
        TextView days = (TextView) view.findViewById(R.id.poi_details_openingdays);

        String username,poiname, add, des, start, end, day;
        Double cst, rate;
        Integer poiid, post;
        username = "1";
        poiid = getArguments().getInt("selected_poi_id");
        poiname = getArguments().getString("selected_poi_name");
        add = getArguments().getString("selected_poi_address");
        post = getArguments().getInt("selected_poi_postalcode");
        rate = getArguments().getDouble("selected_poi_rating");
        des = getArguments().getString("selected_poi_desc");
        cst = getArguments().getDouble("selected_poi_cost");
        start = getArguments().getString("selected_poi_starthrs");
        end = getArguments().getString("selected_poi_endhrs");
        day = getArguments().getString("selected_poi_openingdays");

        float ratingvalue = rate.floatValue();

        id.setText(poiid.toString());
        name.setText(poiname);
        address.setText(add);
        postal.setText("Singapore " + post.toString());
        cost.setText("S$" + cst.toString());
        if (des.equals("No Description")) {
            desc.setText("No Description");
        } else {
            desc.setText(des);
        }
        open.setText("Opens at " + start);
        close.setText("Closes at " + end);
        days.setText(day);
        rating.setRating(ratingvalue);



        FloatingActionButton sendBtn = (FloatingActionButton) view.findViewById(R.id.floatingActionButtonAdd);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendData();
            }
        });

        FloatingActionButton bookmarkBtn = (FloatingActionButton) view.findViewById(R.id.floatingActionButtonBookmark);

        bookmarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bookmarkData(username,poiid.toString(),poiname,add,des);
            }
        });

        return view;

    }

    private void sendData()
    {
        //INTENT OBJ
        Intent i = new Intent(getActivity().getBaseContext(),
                ItineraryDetailsActivity.class);

//        Bundle bundle = new Bundle();
//        bundle.putInt("selected_poi_id",x );
//        i.putExtra("xy", bundle);

        //PACK DATA
//        String poiname;
//        poiname = getArguments().getString("selected_poi_name");
        int poiid;
        poiid = getArguments().getInt("selected_poi_id");

        Bundle extras = new Bundle();
        extras.putString("NAME_KEY", poiid + "");
        i.putExtras(extras);
        i.putExtra("SENDER_KEY", "MyFragment");
//        i.putExtra("NAME_KEY",poiid);
        // i.putExtra("YEAR_KEY", Integer.valueOf(.getSelectedItem().toString()));

        //RESET WIDGETS
//        poiname.setText("");
//        launchYearSpinner.setSelection(0);

        //START ACTIVITY
        getActivity().startActivity(i);
    }

    private void bookmarkData(String userId, String poiId,String loc_name, String loc_address,String loc_desc) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_ADDBOOKMARKS, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Toast.makeText(getActivity(), "Bookmark Added", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                Toast.makeText(getActivity(), "Error adding bookmark, please try again", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String,String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", userId);
                params.put("pid", poiId);
                params.put("lname", loc_name);
                params.put("ladd", loc_address);
                params.put("ldesc", loc_desc);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
}
