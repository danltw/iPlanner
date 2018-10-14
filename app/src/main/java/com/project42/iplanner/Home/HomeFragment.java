package com.project42.iplanner.Home;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.project42.iplanner.JSON.JSONParser;
import com.project42.iplanner.POIs.POI;
import com.project42.iplanner.SingletonClass.MyApplication;
import com.project42.iplanner.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    private static final String TAG = HomeFragment.class.getSimpleName();
    private static final String URL = "http://project42-iplanner.000webhostapp.com/get_recommended_poi.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_POI = "poi";
    private static final String TAG_LOCATION_ID = "location_id";
    private static final String TAG_LOCATION_NAME = "location_name";
    private static final String TAG_LOCATION_COST = "location_cost";
    private static final String TAG_LOCATION_RATING = "location_rating";

    // POI JSONArray
    JSONArray poi = null;

    private RecyclerView recyclerView;
    private List<POI> poiList;
    private POIAdapter pAdapter;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//         Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        pAdapter = new POIAdapter(getActivity(), poiList);

        new LoadRecommendedPOI().execute();

        return view;
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    class POIAdapter extends RecyclerView.Adapter<POIAdapter.MyViewHolder> {
        private Context context;
        private List<POI> poiList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView name, price, rating;
//            public ImageView thumbnail;

            public MyViewHolder(View view) {
                super(view);
                name = view.findViewById(R.id.title);
                price = view.findViewById(R.id.price);
                rating = view.findViewById(R.id.rating);
//                thumbnail = view.findViewById(R.id.thumbnail);
            }
        }


        public POIAdapter(Context context, List<POI> poiList) {
            this.context = context;
            this.poiList = poiList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recommended_poi_row, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final POI poi = poiList.get(position);
            holder.name.setText(poi.getLocationName());
            holder.price.setText(Double.toString(poi.getCost()));
            holder.rating.setText(Double.toString(poi.getRating()));

//            Glide.with(context)
//                    .load(poi.getImage())
//                    .into(holder.thumbnail);
        }

        @Override
        public int getItemCount() {
            return poiList.size();
        }
    }

    class LoadRecommendedPOI extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading POIs. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All poi from url
         */
        protected String doInBackground(String... args) {
            ContentValues params = new ContentValues();
            // getting JSON string from URL
            JSONObject json = null;
            try {
                json = jParser.makeHttpRequest(URL, "GET", params);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ArrayList<POI> recpoi = new ArrayList<POI>();
            // Check your log cat for JSON response
            Log.d("Recommended POI: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // poi found
                    // Getting Array of poi
                    poi = json.getJSONArray(TAG_POI);

                    // looping through All poi
                    for (int i = 0; i < poi.length(); i++) {
                        JSONObject c = poi.getJSONObject(i);

                        // Storing each json item in variable
                        recpoi.get(i).setLocationID(c.getInt(TAG_LOCATION_ID));
                        recpoi.get(i).setLocationName(c.getString(TAG_LOCATION_NAME));
                        recpoi.get(i).setCost(c.getDouble(TAG_LOCATION_COST));
                        recpoi.get(i).setRating(c.getDouble(TAG_LOCATION_RATING));

                        poiList.add(recpoi.get(i));
                    }
                } else {
                    // no poi found

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all POI
            pDialog.dismiss();
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(8), true));
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(pAdapter);
                    recyclerView.setNestedScrollingEnabled(false);
                }
            });
        }
    }
}
