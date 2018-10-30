package com.project42.iplanner.Itineraries;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project42.iplanner.R;

//import com.project42.iplanner.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ItineraryDetailsFragment extends Fragment {


    public ItineraryDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_itinerary_details, container, false);
    }

}
