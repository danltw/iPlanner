package com.project42.iplanner.POIs;


import android.media.Rating;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.project42.iplanner.R;

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

        String poiname,add,des,start,end,day;
        Double cst,rate;
        Integer poiid,post;
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

        float ratingvalue  = rate.floatValue();

        id.setText(poiid.toString());
        name.setText(poiname);
        address.setText(add);
        postal.setText("Singapore " + post.toString());
        cost.setText("S$"+cst.toString());
        if(des.equals("No Description"))
        {
            desc.setText("No Description");
        }
        else
        {
            desc.setText(des);
        }
        open.setText("Opens at " + start);
        close.setText("Closes at " + end);
        days.setText(day);
        rating.setRating(ratingvalue);

        return view;
    }

}
