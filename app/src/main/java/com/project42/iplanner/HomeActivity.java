package com.project42.iplanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.project42.iplanner.DBConn.DBController;
import com.project42.iplanner.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        DBController.getInstance(this.getApplicationContext()).connect();
    }

}
