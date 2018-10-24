package com.project42.iplanner.Itineraries;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project42.iplanner.AppConfig;
import com.project42.iplanner.R;
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

import java.util.ArrayList;
import java.util.Calendar;

public class ItineraryDetailsActivity extends AppCompatActivity  implements
        View.OnClickListener{

    Spinner spinner;
    ArrayList<String> CountryName;


    private TextView idTxt,nameTxt;
    Button btnDatePicker;
    EditText txtDate;
    private int mYear, mMonth, mDay;
    EditText chooseTime;
    TimePickerDialog timePickerDialog;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    String amPm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //REFERENCE VIEWS

        setContentView(R.layout.activity_view_itinerary_datepickerdialog);

        idTxt= (TextView) findViewById(R.id.idTxt);
        btnDatePicker=(Button)findViewById(R.id.btn_date);
        txtDate=(EditText)findViewById(R.id.in_date);
        nameTxt=(TextView) findViewById(R.id.nameTxt);

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
                        chooseTime.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });


        CountryName=new ArrayList<>();
        spinner=(Spinner)findViewById(R.id.country_Name);
        loadSpinnerData(AppConfig.URL_ITINERARYSPINNER);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String country=   spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                Toast.makeText(getApplicationContext(),country,Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });
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

                                txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
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

    /*
       OPEN FRAGMENT
        */
//    private void openFragment()
//    {
//        //PASS OVER THE BUNDLE TO OUR FRAGMENT
//        POIDetailsFragment myFragment = new POIDetailsFragment();
//        //THEN NOW SHOW OUR FRAGMENT
//        getSupportFragmentManager().beginTransaction().replace(R.id.container,myFragment).commit();
//    }


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


    private void loadSpinnerData(String url) {
        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject=new JSONObject(response);

                        JSONArray jsonArray=jsonObject.getJSONArray("Name");
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            String country=jsonObject1.getString("itinerary_name");
                            CountryName.add(country);
                        }

                    spinner.setAdapter(new ArrayAdapter<String>(ItineraryDetailsActivity.this, android.R.layout.simple_spinner_dropdown_item, CountryName));
                }catch (JSONException e){e.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }
}
