package com.project42.iplanner.POIs;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project42.iplanner.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class POISearchFragment extends Fragment {


    public POISearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_poisearch, container, false);
    }

}
