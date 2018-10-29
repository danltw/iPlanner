package com.project42.iplanner.Accounts;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import com.project42.iplanner.R;

public class ProfileDetailsActivity extends AppCompatActivity{

    FloatingActionButton fab = findViewById(R.id.fab);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                /*Intent intent = new Intent(ProfileDetailsActivity.this, LoginActivity.class);
                startActivity(intent);*/
            }
        });
    }

//    private void loadFragment(Fragment fragment) {
//        // load fragment
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.frame_container, fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }

//    public void onClick(View view){
//
//        Fragment fragment = new LoginUI();
//        loadFragment(fragment);
//        Intent intent = new Intent(ProfileDetailsActivity.this, LoginActivity.class);
//        startActivity(intent);
//    }


}
