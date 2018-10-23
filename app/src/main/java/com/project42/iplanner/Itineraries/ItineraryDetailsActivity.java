package com.project42.iplanner.Itineraries;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.project42.iplanner.R;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

public class ItineraryDetailsActivity extends AppCompatActivity  implements
        View.OnClickListener{
    private TextView nameTxt;
    Button btnDatePicker;
    EditText txtDate;
    private int mYear, mMonth, mDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //REFERENCE VIEWS

        setContentView(R.layout.activity_view_itinerary_datepickerdialog);

        nameTxt= (TextView) findViewById(R.id.nameTxt);
        btnDatePicker=(Button)findViewById(R.id.btn_date);
        txtDate=(EditText)findViewById(R.id.in_date);

        btnDatePicker.setOnClickListener(this);
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
        nameTxt.setText(Integer.toString(poiid));
        //yearTxt.setText(String.valueOf(year));
    }
}
