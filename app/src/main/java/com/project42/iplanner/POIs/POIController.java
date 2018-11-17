package com.project42.iplanner.POIs;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project42.iplanner.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Represents a POI controller.
 * @author Team42
 * @version 1.0
 */
public class POIController {
    private static POIController mInstance;
    private static Context mCtx;
    private Double currentpsi = 0.0;
    private Double currentuvi = 0.0;
    private String pid = "null";
    private List<POI> poiList;
    private RecyclerView.Adapter adapter;

    /**
     * Creates an empty POI controller with no property initialized
     */
    public POIController() {

    }

    /** Obtains the POI controller singleton reference by passing it the current activity to be used in.
     * If the _mCtx parameter is null, the current context of this class will be used instead.
     * @param _mCtx The current activity for the group controller to be used in.
     * @return The current instance of the POI controller singleton class
     */
    public static POIController getInstance(Context _mCtx) {
        mCtx = _mCtx;
        if (mInstance == null)
            mInstance = new POIController();
        return mInstance;
    }

    /** The method to get UVI for the current time from GovTech's API.
     * Retrieval of UVI at the current time instance.
     * @return A list of UVIs.
     */
    public List getAllUVI()
    {
        poiList = new ArrayList<>();
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(now);
        Log.d("Date", dateStr);
        Log.d("URL", AppConfig.URL_UVI+dateStr);
        //String URL = URL_PSI + sdf.format(dateStr).toString();
        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.URL_UVI+dateStr,
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
        RequestQueue requestQueue = Volley.newRequestQueue(mCtx);

        //adding the string request to request queue
        requestQueue.add(stringRequest);
        return poiList;
    }

    /** The method to get PSI for the current time from GovTech's PSI.
     * Retrieval of PSI at the current time instance.
     */
    public void getAllPSI()
    {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(now);
        Log.d("Date", dateStr);
        Log.d("URL", AppConfig.URL_PSI+dateStr);
        //String URL = URL_PSI + sdf.format(dateStr).toString();
        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.URL_PSI+dateStr,
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
                            getData(currentuvi,currentpsi,pid);
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
        RequestQueue requestQueue = Volley.newRequestQueue(mCtx);

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }

    /** The method to get all POIs, based on the UVI and PSI.
     * Retrieval of POIs which match the UVI and PSI criteria.
     * @param cuvi The UVI at current time instance.
     * @param cpsi The PSI at current time instance.
     * @param poiid The ID of the POI which matches the UVI and PSi values.
     */
    public void getData(Double cuvi, Double cpsi, String poiid) {
        final ProgressDialog progressDialog = new ProgressDialog(mCtx);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_RECOMMENDED, new Response.Listener<String>()
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
                        String postalcode = poi.getString("postalCode");
                        Double rating = poi.getDouble("rating");
                        Double cost = poi.getDouble("cost");
                        String startHrs = poi.getString("startHrs");
                        String endHrs = poi.getString("endHrs");
                        String open = poi.getString("openingDays");
                        String desc = poi.getString("description");
                        Double uvi = poi.getDouble("UVI");
                        Double psi = poi.getDouble("PSI");
                        String imgpath = poi.getString("imgpath");
                        POI poiObject = new POI(id,name,address,postalcode,rating,cost,startHrs,endHrs,open,desc,uvi,psi,imgpath);
                        poiList.add(poiObject);
                        Log.d("JSON Data", poi.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
                adapter = new POIAdapter(mCtx, poiList);
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
                params.put("pid",poiid);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mCtx);
        requestQueue.add(stringRequest);
    }

    /** The method to add a POI into bookmarks.
     * Bookmarking of a POI requires all the parameters to be non-empty.
     * @param userId The ID of the user whom adds the bookmark.
     * @param poiId The ID of the POI that is selected to be bookmarked.
     * @param loc_name The name of the POI that is selected to be bookmarked.
     * @param loc_address The address of the POI that is selected to be bookmarked.
     */
    public void bookmarkData(String userId, String poiId,String loc_name, String loc_address,String loc_desc) {
        final ProgressDialog progressDialog = new ProgressDialog(mCtx);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_ADDBOOKMARKS, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Toast.makeText(mCtx, "Bookmark Added", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                Toast.makeText(mCtx, "Error adding bookmark, please try again", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(mCtx);
        requestQueue.add(stringRequest);
    }
}
