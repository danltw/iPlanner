package com.project42.iplanner.Accounts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.project42.iplanner.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public boolean verifyUser(String username, String tryPassword){
        //called by Login_Activity to check if password matches username

        boolean userVerified = false;

        String password = null;

        return userVerified;
    }
}
