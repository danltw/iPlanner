package com.project42.iplanner.Itineraries;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project42.iplanner.AppConfig;
import com.project42.iplanner.Groups.Group;
import com.project42.iplanner.R;
import com.project42.iplanner.Utilities.ListUtils;
import com.project42.iplanner.Utilities.SharedManager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import android.app.TimePickerDialog;
import android.widget.TimePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;


public class ItineraryDetailsActivity extends AppCompatActivity  implements
        View.OnClickListener{

    Spinner spinner;
    Spinner groupIdspinner;
    ArrayList<String> itineraryName;
    ArrayList<String> groupID;
    private TextView idTxt,nameTxt;
    Button btnDatePicker,btnSubmit;
    EditText txtDate;
    private int mYear, mMonth, mDay;
    EditText chooseTime,inputName;
    TimePickerDialog timePickerDialog;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    String amPm;
    TextView serverResp;

        // Creating Volley RequestQueue.
        RequestQueue requestQueue;

        // Create string variable to hold the EditText Value.
        String ItineraryNameHolder,ItinerarySpinnerNameHolder, POIidHolder, GroupIdHolder, TimeHolder,DateHolder,UserIdHolder;

        // Creating Progress dialog.
        ProgressDialog progressDialog;
        static final String REQ_TAG = "http://project42-iplanner.000webhostapp.com/postItinerary.php";
        // Storing server url into String variable.
        String HttpUrl = "http://project42-iplanner.000webhostapp.com/postItinerary.php";
        String userName = SharedManager.getInstance().getUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //REFERENCE VIEWS

        setContentView(R.layout.activity_view_itinerary_datepickerdialog);

        idTxt= (TextView) findViewById(R.id.idTxt);
        btnDatePicker=(Button)findViewById(R.id.btn_date);
        txtDate=(EditText)findViewById(R.id.in_date);
        nameTxt=(TextView) findViewById(R.id.nameTxt);
        btnSubmit=(Button)findViewById(R.id.submitBtn);
        inputName=(EditText)findViewById(R.id.select_name);
        serverResp = (TextView)findViewById(R.id.server_resp);
        // Assigning ID's to Button.
        btnSubmit = (Button) findViewById(R.id.submitBtn);

        // Creating Volley newRequestQueue .
        requestQueue = Volley.newRequestQueue(ItineraryDetailsActivity.this);

        progressDialog = new ProgressDialog(ItineraryDetailsActivity.this);

        String userName = SharedManager.getInstance().getUser();
        UserIdHolder=userName;

        btnDatePicker.setOnClickListener(this);

        chooseTime = findViewById(R.id.etChooseTime);
        chooseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(ItineraryDetailsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        chooseTime.setText(String.format("%02d:%02d", hourOfDay, minutes)+ ":" +"00" );
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });


        itineraryName=new ArrayList<>();
        spinner=(Spinner)findViewById(R.id.country_Name);
        loadSpinnerData(userName);

        spinner.setSelection(0, false);
        TextView selectedView = (TextView) spinner.getSelectedView();
        if (selectedView != null) {
            selectedView.setTextColor(getResources().getColor(R.color.transparent));
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            private boolean selectionControl = true;

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(adapterView.getItemAtPosition(spinner.getSelectedItemPosition()).equals("")){
                    ItinerarySpinnerNameHolder="";
                }

                else {
                    String country = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                    Toast.makeText(getApplicationContext(), country, Toast.LENGTH_LONG).show();
                    ItinerarySpinnerNameHolder= spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
                //spinner is disabled
//                spinner.setEnabled(false);
            }
        });


        groupID=new ArrayList<>();
        groupIdspinner=(Spinner)findViewById(R.id.group_id_spinner);
        loadGroupIdSpinnerData(userName);
        groupIdspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String country = groupIdspinner.getItemAtPosition(groupIdspinner.getSelectedItemPosition()).toString();
                Toast.makeText(getApplicationContext(), country, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here

            }
        });


        // Adding click listener to button.
        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // Showing progress dialog at user registration time.
                progressDialog.setMessage("Please Wait, We are Inserting Your Data on Server");
                progressDialog.show();

                // Calling method to get value from EditText.
                try {
                    GetValueFromEditText();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // Creating string request with post method.
                StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String ServerResponse) {

                                // Hiding the progress dialog after all task complete.
                               progressDialog.dismiss();
                                Log.d("error",ServerResponse);
                                // Showing response message coming from server.
                                Toast.makeText(ItineraryDetailsActivity.this, ServerResponse, Toast.LENGTH_LONG).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {

                                // Hiding the progress dialog after all task complete.
                                progressDialog.dismiss();

                                // Showing error message if something goes wrong.
                                Toast.makeText(ItineraryDetailsActivity.this, volleyError.toString(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {

                        // Creating Map String Params.
                        Map<String, String> params = new HashMap<String, String>();

                        // Adding All values to Params.
                        if (ItineraryNameHolder == null || ItineraryNameHolder.isEmpty())
                            params.put("itinerary_name", ItinerarySpinnerNameHolder);
                        else
                            params.put("itinerary_name_text", ItineraryNameHolder);
                        params.put("POI_id", POIidHolder);
                        params.put("group_id", GroupIdHolder);
                        params.put("visit_time", TimeHolder);
                        params.put("visit_date", DateHolder);
                        params.put("user_id", UserIdHolder);
                        return params;
                    }

                };

                // Creating RequestQueue.
                RequestQueue requestQueue = Volley.newRequestQueue(ItineraryDetailsActivity.this);

                // Adding the StringRequest object into requestQueue.
                requestQueue.add(stringRequest);

            }
  });


    }



    // Creating method to get value from EditText.
    public void GetValueFromEditText() throws ParseException {
        String userName = SharedManager.getInstance().getUser();
        TimeHolder = chooseTime.getText().toString().trim();
        //DateFormat formatter = new SimpleDateFormat("hh:mm:ss a");
        //Date time = formatter.parse(TimeHolder);

   //    ItinerarySpinnerNameHolder= spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
        ItineraryNameHolder = inputName.getText().toString().trim();
        POIidHolder = idTxt.getText().toString().trim();
        Object o = groupIdspinner.getItemAtPosition(groupIdspinner.getSelectedItemPosition());
        if (o == null)
            GroupIdHolder = "NULL";
        else
            GroupIdHolder = o.toString();
        UserIdHolder=userName;
        DateHolder=txtDate.getText().toString().trim();
        //DateFormat Dateformatter = new SimpleDateFormat("yyyy-mm-dd");
        //Date date = Dateformatter.parse(TimeHolder);

    }

        @Override
        public void onClick(View v) {

            if (v == btnDatePicker) {

                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                txtDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }

        }


    @Override
    protected void onResume() {
        super.onResume();

        //DETERMINE WHO STARTED THIS ACTIVITY
        final String sender=this.getIntent().getExtras().getString("SENDER_KEY");

        //IF ITS THE FRAGMENT THEN RECEIVE DATA
        if(sender != null)
        {
            this.receiveData();
            Toast.makeText(this, "Received", Toast.LENGTH_SHORT).show();

        }
    }




    private void receiveData()
    {

        Bundle extras = getIntent().getExtras();
        String NAME_KEY = extras.getString("NAME_KEY");
        int poiid = Integer.parseInt(NAME_KEY);
        //RECEIVE DATA VIA INTENT
//        Intent i = getIntent();
//        int poiid = i.getIntExtra("NAME_KEY", 0);
        //int year = i.getIntExtra("YEAR_KEY",0);

        //SET DATA TO TEXTVIEWS
        idTxt.setText(Integer.toString(poiid));
        //yearTxt.setText(String.valueOf(year));
    }


    private void loadSpinnerData(String userName) {
        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.POST, AppConfig.URL_ITINERARYSPINNER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    //JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonarray = new JSONArray(response);

                       // JSONArray jsonArray=jsonarray.getJSONArray("Name");
                        for(int i=0;i<jsonarray.length();i++){
                            JSONObject jsonObject1=jsonarray.getJSONObject(i);
                            String country=jsonObject1.getString("itinerary_name");
                            itineraryName.add(country);
                        }

                      spinner.setAdapter(new ArrayAdapter<String>(ItineraryDetailsActivity.this, android.R.layout.simple_spinner_dropdown_item, itineraryName));
                }catch (JSONException e){e.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
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
            }};
        int socketTimeout = 3000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }



    private void loadGroupIdSpinnerData(String userName) {
        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.POST, AppConfig.URL_GROUPIDSPINNER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    Log.d("GroupIDs display", response);
                    //JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonarray = new JSONArray(response);

                    // JSONArray jsonArray=jsonarray.getJSONArray("Name");
                    for(int i=0;i<jsonarray.length();i++){
                        JSONObject jsonObject1=jsonarray.getJSONObject(i);
                        String country=jsonObject1.getString("group_id");
                        groupID.add(country);
                    }

                    groupIdspinner.setAdapter(new ArrayAdapter<String>(ItineraryDetailsActivity.this, android.R.layout.simple_spinner_dropdown_item, groupID));
                }catch (JSONException e){e.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
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
            }};
        int socketTimeout = 3000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }




}
